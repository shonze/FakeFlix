#include <gtest/gtest.h>
#include <fstream>

#include "../src/header/Loader.h"
#include "../src/header/ILoadable.h"
#include "../src/header/Utilities.h"
#include "../src/header/Utilities.h"

// Test for valid input
TEST(LoaderTest, ValidInput) {
    // Create the test file
    Utilities::writeTestFile("valid_input.txt", 
            "user1 MovieA MovieB MovieC\n"
            "user2 MovieX MovieY\n"
            "user3 MovieM\n");
    // Creating the Loader        
    Loader* loader = new Loader("valid_input.txt");
    // Getting the map
    auto result = loader->load();

    /* Checking if the results are correct */
    ASSERT_EQ(result.size(), 3);
    EXPECT_EQ(result["user1"], (std::vector<std::string>{"MovieA", "MovieB", "MovieC"}));
    EXPECT_EQ(result["user2"], (std::vector<std::string>{"MovieX", "MovieY"}));
    EXPECT_EQ(result["user3"], (std::vector<std::string>{"MovieM"}));
}
// Test for empty file
TEST(LoaderTest, EmptyFile) {
    // Create the test file
    Utilities::writeTestFile("empty_file.txt", "");
    // Creating the Loader 
    Loader* loader = new Loader("empty_file.txt");
    // Getting the map
    auto result = loader->load();

    /* Checking if the results are correct */
    EXPECT_TRUE(result.empty());
    remove("empty_file.txt");
}

// Test for missing file
TEST(LoaderTest, MissingFile) {
    // Creating the Loader 
    Loader* loader = new Loader("nonexistent.txt");

    /* Checking if the results are correct */
    EXPECT_THROW(loader->load(), std::runtime_error);
}
