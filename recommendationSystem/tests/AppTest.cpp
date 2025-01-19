#include <gtest/gtest.h>
#include <sstream>
#include <iostream>
#include <fstream>
#include <string>
#include <thread>
#include <unistd.h>  // For close()
#include <sys/socket.h> // For socket()
#include <arpa/inet.h>  // For sockaddr_in, inet_addr
#include <chrono>       // For sleep functionality
#include <thread>       // For std::this_thread::sleep_for

#include "../src/header/ICommand.h"
#include "../src/header/AppTestable.h"
#include "../src/header/Post.h"
#include "../src/header/Patch.h"
#include "../src/header/Delete.h"
#include "../src/header/Recommend.h"
#include "../src/header/MemoryWriteable.h"
#include "../src/header/Loader.h"
#include "../src/header/WriteToTxt.h"
#include "../src/header/Help.h"
#include "../src/header/Utilities.h"

#define IP "127.0.0.1"
#define PORT 3000

// Clients to simulate the client requests
void runClient(std::vector<std::string> commands, std::vector<std::string> &server_output)
{
    char buffer[1024] = {0};
    
    // Create the client socket
    int clientSocket = socket(AF_INET, SOCK_STREAM, 0);
    if (clientSocket < 0) {
        std::cerr << "Socket creation failed!" << std::endl;
        return;
    }

    sockaddr_in serverAddress;
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(PORT);
    serverAddress.sin_addr.s_addr = inet_addr(IP);

    // Connect to the server
    if (connect(clientSocket, (struct sockaddr *)&serverAddress, sizeof(serverAddress)) < 0) {
        std::cerr << "Connection failed!" << std::endl;     
        close(clientSocket);
        return;
    }

    // Sleep to simulate parallel clients
    std::this_thread::sleep_for(std::chrono::seconds(1));

    for (size_t i = 0; i < commands.size(); ++i)
    {
        std::string command = commands.at(i);

        // Send the command
        send(clientSocket, command.c_str(), command.size(), 0);

        // Clear the buffer
        memset(buffer, 0, sizeof(buffer));

        // Receive the data and append it to the list
        int bytes_read = recv(clientSocket, buffer, sizeof(buffer), 0);
        if(bytes_read == -1){
            continue;
        }
        std::string output_str(buffer, bytes_read);
        server_output.push_back(output_str);
    }

    // Close the socket
    close(clientSocket);
}

std::string VectorToString(std::vector<std::string> strings){
    std::string output;
    for(auto string : strings){
        output.append(string);
    }
    return output;
}

TEST(AppTests, HelpCommand)
{
    std::vector<std::string> inputs = {
        "help",
    };

    std::vector<std::string> output;
    runClient(inputs, output);

    std::string compare = "DELETE, arguments: [userid] [movieid1] [movieid2] ...\n"
                          "GET, arguments: [userid] [movieid]\n"
                          "PATCH, arguments: [userid] [movieid1] [movieid2] ...\n"
                          "POST, arguments: [userid] [movieid1] [movieid2] ...\n"
                          "help\n";
    EXPECT_EQ(VectorToString(output), compare);
}

TEST(AppTests, TestDataFileContent)
{
    // Basic input
    std::vector<std::string> inputs = {
        "POST 1 100 101 102 103",
        "POST 2 101 102 104 105 106",
        "POST 3 100 104 105 107 108",
        "POST 4 101 105 106 107 109 110",
        "POST 5 100 102 103 105 108 111",
        "POST 6 100 103 104 110 112 113",
        "POST 7 102 105 106 107 108 109 110",
        "POST 8 101 104 105 106 107 109 111 114",
        "POST 9 100 103 105 107 112 113 115",
        "POST 10 100 102 105 106 107 109 110 116",
    };

    std::vector<std::string> output;
    runClient(inputs, output);

    std::string compare = "201 Created\n201 Created\n201 Created\n201 Created\n"
                          "201 Created\n201 Created\n201 Created\n201 Created\n201 Created\n201 Created\n";
    EXPECT_EQ(VectorToString(output), compare);
}

TEST(AppTests, ReusableData)
{
    std::vector<std::string> inputs = {
        "GET 1 104"};

    std::vector<std::string> output;
    runClient(inputs, output);

    std::string compare = "200 Ok\n\n105 106 107 110 112 113 108 109 111 114 \n";
    EXPECT_EQ(VectorToString(output), compare);
}

TEST(AppTests, TestDataFileContentWithSpecialCases)
{
    std::vector<std::string> inputs = {
        "PATCH 8 114",            // Double 114 watched by 8
        "PATCH 4 111  112   113", // Two spaces or more between movies
        "GET 1 104"};

    std::vector<std::string> output;
    runClient(inputs, output);

    std::string compare = "204 No Content\n204 No Content\n200 Ok\n"
                          "\n105 106 107 110 112 113 108 109 111 114 \n";
    EXPECT_EQ(VectorToString(output), compare);
}

TEST(AppTests, InvalidCommands)
{
    std::vector<std::string> inputs = {
        "flop",
        "recommend",
        "POST 100",
        "DELETE 1",
        "PATCH 2",
        "GET 2",
        "PATCH 1 107 C",        // Last id is not a number
        "POST 11 123 12f",
        "GET 1 1D",
        "DELETE 1 101 F",
        "POST 1 109\t110\v111", // There is a tab between the movies
        "help 12",
        "GET c 12",
        "DELETE 1r 12",
        "POST 1S 12",
        "PATCH E 2",

        // From here its logical problems, meaning 404 should be returned

        "POST 1 104",            // POST an existing user
        "DELETE 11 12",          // DELETE a nonexisting user
        "DELETE 1 100 101 102 1076", // 1076 was not watched by 1
        "GET 11 12", 
        "PATCH 11 213"           // PATCH a nonexisting user
    };

    std::vector<std::string> output;
    runClient(inputs, output);

    std::string compare = "";
    for(int i = 0;i < inputs.size();i++){
        if(i < 16){
            compare.append("400 Bad Request\n");
        } else{
            compare.append("404 Not Found\n");
        }
    }

    EXPECT_EQ(VectorToString(output), compare);
}

TEST(AppTests, DeleteCommand)
{
    std::vector<std::string> inputs = {
        "DELETE 1 103",
        "DELETE 2 105 106"};

    std::vector<std::string> output;
    runClient(inputs, output);

    std::string compare = "204 No Content\n204 No Content\n";
    EXPECT_EQ(VectorToString(output), compare);
}

TEST(AppTests, MultipleClients)
{
    std::vector<std::string> inputs_client1 = {
        "PATCH 1 103"};
    std::vector<std::string> inputs_client2 = {
        "PATCH 2 105"};
    std::vector<std::string> inputs_client3 = {
        "PATCH 2 106"};

    // Run the clients in parallel
    std::vector<std::string> output_client1;
    std::thread client1(runClient, inputs_client1, std::ref(output_client1));

    std::vector<std::string> output_client2;
    std::thread client2(runClient, inputs_client2, std::ref(output_client2));

    std::vector<std::string> output_client3;
    std::thread client3(runClient, inputs_client3, std::ref(output_client3));

    client1.join();
    client2.join();
    client3.join();

    std::string compare1 = "204 No Content\n";
    EXPECT_EQ(VectorToString(output_client1), compare1);
    EXPECT_EQ(VectorToString(output_client2), compare1);
    EXPECT_EQ(VectorToString(output_client3), compare1);

    std::vector<std::string> inputs = {
        "GET 1 104"};

    std::vector<std::string> output;
    runClient(inputs, output);

    // The data should be the same after we deleted and than readded
    std::string compare2 = "200 Ok\n\n105 106 107 110 112 113 108 109 111 114 \n";
    EXPECT_EQ(VectorToString(output), compare2);
}


int main(int argc, char **argv)
{
    ::testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS(); // Run the tests
}
