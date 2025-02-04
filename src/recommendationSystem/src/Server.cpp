#include "../header/Server.h"

Server::Server(int port) {
    const int server_port=port;
    

    // Create socket
    this->server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (this->server_fd < 0) {
        exit(EXIT_FAILURE);
    }

    // Bind socket to the specified port
    struct sockaddr_in sin;
    memset(&sin,0, sizeof(sin));
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = INADDR_ANY;
    sin.sin_port = htons(server_port);

    if (bind(this->server_fd, (struct sockaddr*) &sin, sizeof(sin)) < 0) {
        perror("Bind failed");
        exit(EXIT_FAILURE);
    }

    // Start listening for client connections
    if (listen(this->server_fd, MAX_CLIENTS) < 0) {
        exit(EXIT_FAILURE);
    }

    std::string fileName= "../data/data.txt";
    // Initializing the commands

    MemoryWriteable* writer = new WriteToTxt(fileName);
    ILoadable* loader = new Loader(fileName);

    ICommand* idelete = new Delete(writer,loader,fileName);
    commands["DELETE"] = idelete;

    ICommand* get= new Recommend(loader);
    commands["GET"]= get;

    ICommand* patch = new Patch(writer,loader);
    commands["PATCH"] = patch;

    ICommand* post = new Post(writer,loader);
    commands["POST"] = post;

    ICommand* help= new Help();
    commands["help"]= help;


    rwMap["DELETE"] = "writer";
    rwMap["PATCH"] = "writer";
    rwMap["POST"] = "writer";
    rwMap["help"]= "reader";
    rwMap["GET"]= "reader";


    threadPool = new ThreadPool(server_fd,commands,rwMap,MAX_CLIENTS);
}

/**
 * Server destructor.
 * Closes the server socket to release resources.
 */
Server::~Server() {
    close(this->server_fd);
}

/**
 * Run the server to accept incoming connections and handle them in separate threads.
 */
void Server::run() {
    while (true) {
        struct sockaddr_in client_sin;
        socklen_t client_len = sizeof(client_sin);

        // Accept an incoming client connection
        int client_fd = accept(this->server_fd, (struct sockaddr*)&client_sin, &client_len);
        if (client_fd < 0) {
            continue;
        }

        // Create a new thread to handle the client
        // std::thread(&Server::handleClient, this, client_fd).detach();
        threadPool->addTask(client_fd);
    }
}

/**
 * Main function to start the server.
 * The server listens on port 3000 and processes client commands.
 */
int main(int argc, char* argv[]) {
    int port = std::stoi(argv[1]);  // Server port
    Server* server= new Server(port);  // Create the server
    server->run();  // Start the server
    return 0;
}