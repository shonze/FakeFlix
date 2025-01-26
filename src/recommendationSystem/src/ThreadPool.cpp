#include "../header/ThreadPool.h"



ThreadPool::ThreadPool(int server_fd, std::map<std::string, ICommand*> commands, std::map<std::string, std::string> rwMap, size_t numThreads):
    server_fd(server_fd), commands(commands), rwMap(rwMap), stop(false) {
    
    for (size_t i = 0; i < numThreads; ++i) {
        workers.emplace_back([this]() { workerFunction(); });
    }
}


ThreadPool::~ThreadPool() {
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        stop = true;
    }

    condition.notify_all();
    for (std::thread &worker : workers) {
        if (worker.joinable()) {
            worker.join();
        }
    }
}

void ThreadPool::workerFunction() {

    while (true) {
        int client_fd;
        {
            std::unique_lock<std::mutex> lock(queueMutex);
            condition.wait(lock, [this]() { return stop || !tasks.empty(); });

            if (stop && tasks.empty())
                return;

            client_fd = tasks.front();
            tasks.pop(); // Mutex is unlocked here
            // std::unique_lock<std::mutex> lock(queueMutex);

        }

        // Process the client outside the critical section
        handleClient(client_fd);

        // Close the client socket after processing
        close(client_fd);
    }
}


void ThreadPool::handleClient(int client_fd) {
    try {
        char buffer[BUFFER_SIZE] = {0}; // Buffer for incoming messages
        while (true) {
            // Read data from the client
            int bytes_read = recv(client_fd, buffer, sizeof(buffer), 0);
            if (bytes_read <= 0) {
                close(client_fd);  // Close the client socket if disconnected
                break;
            }

            // Parse the received input
            std::string input(buffer, bytes_read);
            std::string command, userId;
            std::vector<std::string> movies;
            std::istringstream stream(input);

            // Check for invalid whitespace
            if (std::any_of(input.begin(), input.end(), [](unsigned char c) {
                    return std::isspace(c) && c != ' ';
                })) {
                std::string response = codeMap[400];
                {
                    send(client_fd, response.c_str(), response.size(), 0);
                }
                continue;
            }

            // Extract the command and arguments
            if (!(stream >> command)) {
                command = "";
                std::string response = codeMap[400];
                {
                    send(client_fd, response.c_str(), response.size(), 0);
                }
                continue;
            }
            if (!(stream >> userId)) userId = "";
            std::string movie;
            while (stream >> movie) {
                movies.push_back(movie);
            }

            // Prepare response buffer
            std::string response;

            if (rwMap[command] == "reader") {
                // Reader commands
                std::shared_lock<std::shared_mutex> lock(rw_mutex);
                if (commands.find(command) != commands.end()) {
                    response = commands[command]->execute(userId, movies);
                } else {
                    response = codeMap[400];
                }
            } else {
                // Writer commands
                std::unique_lock<std::shared_mutex> lock(rw_mutex);
                if (commands.find(command) != commands.end()) {
                    response = commands[command]->execute(userId, movies);
                } else {
                    response = codeMap[400];
                }
            }
            // Send the execution result to the client within the I/O critical section
            
            send(client_fd, response.c_str(), response.size(), 0);
        }
    } catch (const std::exception& e) {
        std::string error_message = codeMap[400];
        send(client_fd, error_message.c_str(), error_message.size(), 0);
    }
}

void ThreadPool::addTask(int client_fd) {
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        tasks.push(client_fd);
    }
    condition.notify_one();
}