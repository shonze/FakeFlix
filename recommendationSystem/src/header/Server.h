#ifndef SERVER_H
#define SERVER_H

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


#include "ICommand.h"
#include "Help.h"
#include "Recommend.h"
#include "Delete.h"
#include "Patch.h"
#include "Post.h"
#include "Utilities.h"

#include "ThreadPool.h"

#define MAX_CLIENTS 8

class Server {
private:
    int server_fd;  // File descriptor for the server socket.
    std::map<std::string, ICommand*> commands;  // Commands.

    std::map<std::string, std::string> rwMap;  // Commands.

    std::shared_mutex rw_mutex; // Mutex for thread-safecommands execution.

    ThreadPool* threadPool;

public:
    Server(int port);

    ~Server();

    void run();
};

#endif