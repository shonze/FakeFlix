#include "../src/header/WriteToTxt.h"
#include "../src/header/Post.h"
#include "../src/header/Patch.h"
#include "../src/header/Help.h"
#include "../src/header/Utilities.h"

#include <gtest/gtest.h>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
#include <unordered_map>

// Test Suite: WriteToMemoryTest
TEST(WriteToMemoryTest, AddNewPerson) {
    std::string filename = "test_add_new_person.txt";

    std::ofstream outFile(filename, std::ios::trunc); // Ensure the file is empty before testing
    outFile.close();

    MemoryWriteable* writer = new WriteToTxt(filename);

    std::vector<std::string> movieIds = {"101", "102", "103"};

    ILoadable* loader = new Loader(filename);

    Post post(writer,loader); // Post for new users
    EXPECT_EQ(post.execute("1", movieIds), "201 Created\n");

    std::vector<std::string> movieIds2 = {"101", "102", "103"};

    Patch patch(writer,loader); // Patch for existing user
    EXPECT_EQ(patch.execute("1", movieIds2), "204 No Content\n");

    std::vector<std::string> movieIds3 = {"101", "102"};

    Post post2(writer,loader); // Post for new users
    EXPECT_EQ(post2.execute("2", movieIds3), "201 Created\n");

    // Check final file content
    std::string expected = "1 101 102 103\n2 101 102";
    EXPECT_EQ(Utilities::readFileContents(filename), expected);
}

TEST(setters, AddNewPerson) {
    std::string filename = "test_add_new_person.txt";

    std::ofstream outFile(filename, std::ios::trunc); // Ensure the file is empty before testing
    outFile.close();

    MemoryWriteable* writer = new WriteToTxt(filename);
    ILoadable* loader = new Loader(filename);

    std::vector<std::string> movieIds = {"101", "102", "103"};

    Post post(writer,loader); // Post for new users
    EXPECT_EQ(post.execute("1", movieIds), "201 Created\n");

    std::vector<std::string> movieIds2 = {"101", "102"};

    Patch patch(writer,loader); // Patch for existing user
    EXPECT_EQ(patch.execute("1", movieIds2), "204 No Content\n");

    std::vector<std::string> movieIds3 = {"101", "102", "103"};

    Post post2(writer,loader); // Post for new users
    EXPECT_EQ(post2.execute("2", movieIds3), "201 Created\n");

    // Check final file content
    std::string expected = "1 101 102 103\n2 101 102 103";
    EXPECT_EQ(Utilities::readFileContents(filename), expected);
}

TEST(checkint, makesureallgood) {
    std::string filename = "test_add_new_person.txt";

    std::ofstream outFile(filename, std::ios::trunc); // Ensure the file is empty before testing
    outFile.close();

    MemoryWriteable* writer = new WriteToTxt(filename);
    ILoadable* loader = new Loader(filename);

    std::vector<std::string> movieIds = {"10c1", "102", "103"};

    Post post(writer,loader); // Post for new users
    EXPECT_EQ(post.execute("1c", movieIds), "400 Bad Request\n");

    std::vector<std::string> movieIds2 = {"10c1", "102"};

    Patch patch(writer,loader); // Patch for existing user
    EXPECT_EQ(patch.execute("1", movieIds2), "400 Bad Request\n");

    std::vector<std::string> movieIds3 = {"10c1", "10.2", "103"};

    Post post2(writer,loader); // Post for new users
    EXPECT_EQ(post2.execute("2", movieIds3), "400 Bad Request\n");

    // Check final file content (should remain empty)
    std::string expected = "";
    EXPECT_EQ(Utilities::readFileContents(filename), expected);
}

TEST(checkint, makesureallgood2) {
    std::string filename = "test_add_new_person.txt";

    std::ofstream outFile(filename, std::ios::trunc); // Ensure the file is empty before testing
    outFile.close();

    MemoryWriteable* writer = new WriteToTxt(filename);
    ILoadable* loader = new Loader(filename);

    std::vector<std::string> movieIds = {"10c1", "102", "103"};

    Post post(writer,loader); // Post for new users
    EXPECT_EQ(post.execute("", movieIds), "400 Bad Request\n");

    std::vector<std::string> movieIds2 = {"", "102"};

    Patch patch(writer,loader); // Patch for existing user
    EXPECT_EQ(patch.execute("1", movieIds2), "400 Bad Request\n");

    std::vector<std::string> movieIds3 = {
        "10c1", "10.2", "-103", "s12", 
        "111111111111111111111111111111111111111111", 
        "-122222h2222222222222222222", "102", "104f"
    };

    Post post2(writer,loader); // Post for new users
    EXPECT_EQ(post2.execute("2", movieIds3), "400 Bad Request\n");

    // Check final file content (should remain empty)
    std::string expected = "";
    EXPECT_EQ(Utilities::readFileContents(filename), expected);
}

TEST(WriteToMemoryTest, HandleEmptyFile) {
    std::string filename = "test_handle_empty_file.txt";
    std::ofstream outFile(filename, std::ios::trunc); // Ensure the file is empty
    outFile.close();

    WriteToTxt* writer = new WriteToTxt(filename);
    ILoadable* loader = new Loader(filename);

    std::vector<std::string> movieIds = {"301"};

    Post post(writer,loader); // Post for new users
    EXPECT_EQ(post.execute("3", movieIds), "201 Created\n"); // Add person to an empty file

    // Check final file content
    std::string expected = "3 301";
    EXPECT_EQ(Utilities::readFileContents(filename), expected);
}