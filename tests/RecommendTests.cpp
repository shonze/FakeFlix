#include <gtest/gtest.h>
#include <fstream>
#include <sstream>
#include <stdexcept>
#include <vector>
#include <string>
#include <cstdio>

#include "../src/header/Recommend.h"
#include "../src/header/Loader.h"
#include "../src/header/RecommendAlgorithmTestable.h"
#include "../src/header/Utilities.h"

// Test for valit print
TEST(RecommendTest, Execute_ValidInput) {
    Utilities::writeTestFile("sanity_test.txt",
    "1 100 101 102\n2 100 103 104");
    
    const std::string userId = "1";
    const std::vector<std::string> movieIds = {"103"};

    ILoadable* loader = new Loader("sanity_test.txt");

    Recommend recommend(loader);

    // Capture the output of the recommend function
    std::string recommendation = recommend.execute(userId, movieIds);

    // Compare the recommend output with the expected output
    EXPECT_EQ(recommendation, "200 Ok\n\n104 \n");
}
// Test for hard problem. Integration beetween algorithm to loader.
TEST(RecommendTest, Execute_ComplexDatabase) {
    Utilities::writeTestFile("real_test.txt",
    "1 100 101 102 103\n"
    "2 101 102 104 105 106\n"
    "3 100 104 105 107 108\n"
    "4 101 105 106 107 109 110\n"
    "5 100 102 103 105 108 111\n"
    "6 100 103 104 110 112 113\n"
    "7 102 105 106 107 108 109 110\n"
    "8 101 104 105 106 107 109 111 114\n"
    "9 100 103 105 107 112 113 115\n"
    "10 100 102 105 106 107 109 110 116\n");
    
    const std::string userId = "1";
    const std::vector<std::string> movieIds = {"104"};

    ILoadable* loader = new Loader("real_test.txt");

    Recommend recommend(loader);

    // Capture the output of the recommend function
    std::string recommendation = recommend.execute(userId, movieIds);

    // Compare the recommend output with the expected output
    EXPECT_EQ(recommendation,"200 Ok\n\n105 106 107 110 112 113 108 109 111 114 \n");
    remove("real_test.txt");
}
// Test for error when user ID is not found
TEST(RecommendTest, Execute_ErrorTestInvalidUserId) {
    Utilities::writeTestFile("sanity_test.txt",
    "1 100 101 102\2 103 104");

    const std::string userId = "3"; // No such ID
    const std::vector<std::string> movieIds = {"104"};

    ILoadable* loader = new Loader("sanity_test.txt");

    Recommend recommend(loader);


    // Capture the output of the recommend function
    std::string recommendation = recommend.execute(userId, movieIds);

    // Compare the recommend output with the expected output
    EXPECT_EQ(recommendation, "404 Not Found\n");
    remove("sanity_test.txt");
}
// Test for setMoviesWatched() with valid input
TEST(RecommendAlgorithmTest, SetMoviesWatched_ValidUserId) {
    std::map<std::string, std::vector<std::string>> movieMap = {
        {"1", {"100", "101", "102"}},
        {"2", {"103", "104"}},
    };
    RecommendAlgorithmTestable algorithm("1", "103", movieMap);

    algorithm.setMoviesWatched();

    // Verify the watched movies are set correctly
    EXPECT_EQ(algorithm.moviesWatched.size(), 3);
    EXPECT_EQ(algorithm.moviesWatched[0], "100");
    EXPECT_EQ(algorithm.moviesWatched[1], "101");
    EXPECT_EQ(algorithm.moviesWatched[2], "102");
}

// Test for setWeights()
TEST(RecommendAlgorithmTest, SetWeights_Calculation) {
    std::map<std::string, std::vector<std::string>> movieMap = {
        {"1", {"100", "101", "102"}},
        {"2", {"101", "102", "103"}},
        {"3", {"102", "103", "104"}},
    };
    RecommendAlgorithmTestable algorithm("1", "105", movieMap);

    algorithm.setMoviesWatched();
    algorithm.setWeights();

    // Check weights
    EXPECT_EQ(algorithm.weights["2"], 2); // Shares 101, 102
    EXPECT_EQ(algorithm.weights["3"], 1); // Shares 102
    EXPECT_EQ(algorithm.weights["1"], 0); // No overlap with itself
}

// Test for setGrades()
TEST(RecommendAlgorithmTest, SetGrades_Calculation) {
    std::map<std::string, std::vector<std::string>> movieMap = {
        {"1", {"100", "101", "102"}},
        {"2", {"101", "102", "103","105"}},
        {"3", {"102", "103", "104","105"}},
    };
    RecommendAlgorithmTestable algorithm("1", "105", movieMap);

    algorithm.setMoviesWatched();
    algorithm.setWeights();
    algorithm.setGrades();

    // Check grades
    EXPECT_EQ(algorithm.moviesGrade["103"], 3); // Weighted by 2 (user2) + 1 (user3)
    EXPECT_EQ(algorithm.moviesGrade["104"], 1); // Weighted by 1 (user3)
    EXPECT_EQ(algorithm.moviesGrade.find("105"), algorithm.moviesGrade.end()); // Not in map
}

// Test for getBest()
TEST(RecommendAlgorithmTest, GetBest_Sorting) {
    // Put random values that are valid since we only care about getBest function
    std::map<std::string, std::vector<std::string>> movieMap = {
        {"1", {"100"}},
    };
    RecommendAlgorithmTestable algorithm("1", "12", movieMap); 

    // Mock moviesGrade
    algorithm.moviesGrade = {
        {"1", 10},
        {"2", 15},
        {"3", 5},
        {"4", 15}, // Tie with movie2
        {"5", 20},
    };

    auto recommendations = algorithm.getBest();

    // Check sorting order
    EXPECT_EQ(recommendations.size(), 5);
    EXPECT_EQ(recommendations[0], "5"); 
    EXPECT_EQ(recommendations[1], "2"); // Tie resolved by moveId
    EXPECT_EQ(recommendations[2], "4"); 
    EXPECT_EQ(recommendations[3], "1");
    EXPECT_EQ(recommendations[4], "3");
}

// Test for Algorithm() end-to-end
TEST(RecommendAlgorithmTest, Algorithm_EndToEnd) {
    std::map<std::string, std::vector<std::string>> movieMap = {
        {"1", {"100", "101", "102"}},
        {"2", {"101", "102", "103", "105"}}, // weight = 2
        {"3", {"102", "103", "104"}}, // havent watched 105
        {"4", {"100", "103", "104", "105", "106"}}, // weight = 1
    };
    RecommendAlgorithm algorithm("1", "105", movieMap);

    std::vector<std::string> recommended;
    algorithm.Algorithm(recommended);

    // Verify the recommendations
    EXPECT_EQ(recommended.size(), 3);
    EXPECT_EQ(recommended[0], "103"); 
    EXPECT_EQ(recommended[1], "104");
    EXPECT_EQ(recommended[2], "106");
}
// Test example shown in pdf
TEST(RecommendAlgorithmTest, Algorithm_Hard_EndToEnd) {
    std::map<std::string, std::vector<std::string>> movieMap = {
        {"1", {"100", "101", "102", "103"}},
        {"2", {"101", "102", "104", "105", "106"}},
        {"3", {"100", "104", "105", "107", "108"}},
        {"4", {"101", "105", "106", "107", "109", "110"}},
        {"5", {"100", "102", "103", "105", "108", "111"}},
        {"6", {"100", "103", "104", "110", "112", "113"}},
        {"7", {"102", "105", "106", "107", "108", "109", "110"}},
        {"8", {"101", "104", "105", "106", "107", "109", "111", "114"}},
        {"9", {"100", "103", "105", "107", "112", "113", "115"}},
        {"10", {"100", "102", "105", "106", "107", "109", "110", "116"}}
    };
    RecommendAlgorithmTestable algorithm("1", "104", movieMap);

    std::vector<std::string> recommended;
    algorithm.Algorithm(recommended);

    // Check weights
    EXPECT_EQ(algorithm.weights["2"], 2); 
    EXPECT_EQ(algorithm.weights["3"], 1); 
    EXPECT_EQ(algorithm.weights["4"], 1); 
    EXPECT_EQ(algorithm.weights["5"], 3); 
    EXPECT_EQ(algorithm.weights["6"], 2); 
    EXPECT_EQ(algorithm.weights["7"], 1); 
    EXPECT_EQ(algorithm.weights["8"], 1); 
    EXPECT_EQ(algorithm.weights["9"], 2); 
    EXPECT_EQ(algorithm.weights["10"], 2); 

    // Check grades
    EXPECT_EQ(algorithm.moviesGrade["105"], 4);  
    EXPECT_EQ(algorithm.moviesGrade["106"], 3);
    EXPECT_EQ(algorithm.moviesGrade["107"], 2); 
    EXPECT_EQ(algorithm.moviesGrade["108"], 1); 
    EXPECT_EQ(algorithm.moviesGrade["109"], 1); 
    EXPECT_EQ(algorithm.moviesGrade["110"], 2); 
    EXPECT_EQ(algorithm.moviesGrade["111"], 1); 
    EXPECT_EQ(algorithm.moviesGrade["112"], 2); 
    EXPECT_EQ(algorithm.moviesGrade["113"], 2); 
    EXPECT_EQ(algorithm.moviesGrade["114"], 1); 

    // Verify the recommendations
    EXPECT_EQ(recommended.size(), 10);
    EXPECT_EQ(recommended[0], "105"); 
    EXPECT_EQ(recommended[1], "106");
    EXPECT_EQ(recommended[2], "107");
    EXPECT_EQ(recommended[3], "110");
    EXPECT_EQ(recommended[4], "112");
    EXPECT_EQ(recommended[5], "113");
    EXPECT_EQ(recommended[6], "108");
    EXPECT_EQ(recommended[7], "109");
    EXPECT_EQ(recommended[8], "111");
    EXPECT_EQ(recommended[9], "114");
}