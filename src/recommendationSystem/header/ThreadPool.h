#ifndef THREADPOOL_H
#define THREADPOOL_H

#include <iostream>
#include <thread>
#include <mutex>
#include <atomic>
#include <vector>
#include <queue>
#include <condition_variable>
#include <string>
#include <cstring>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <iostream>
#include <thread>
#include <vector>
#include <string>
#include <map>
#include <sstream>
#include <cstring>
#include <mutex>
#include <memory>
#include <netinet/in.h>
#include <unistd.h>
#include <shared_mutex>
#include <iostream>
#include <thread>
#include <vector>
#include <string>
#include <map>
#include <sstream>
#include <cstring>
#include <mutex>
#include <memory>
#include <netinet/in.h>
#include <unistd.h>
#include <shared_mutex>
#include <algorithm>

#include "Utilities.h"

#include "ICommand.h"

#define BUFFER_SIZE 1024

class ThreadPool {
private:
    int server_fd;  // File descriptor for the server socket.
    std::map<std::string, ICommand*> commands;  // Commands.

    std::map<std::string, std::string> rwMap;  // Commands.

    std::shared_mutex rw_mutex; // Mutex for thread-safecommands execution.

    size_t numThreads;

    //original code
    std::vector<std::thread> workers;
    std::queue<int> tasks; // Queue of client sockets
    std::mutex queueMutex;
    std::condition_variable condition;
    std::atomic<bool> stop;

public:
    ThreadPool(int server_fd, std::map<std::string, ICommand*> commands, std::map<std::string, std::string> rwMap, size_t numThreads);

    ~ThreadPool();

    void handleClient(int client_fd);

    void workerFunction();

    void addTask(int client_fd);
};

#endif