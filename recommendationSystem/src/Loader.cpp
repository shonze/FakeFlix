#include "../header/Loader.h"

// The constractor
Loader:: Loader(std::string fileName){
    this->loadFromFile= fileName;
}


std::map<std::string, std::vector<std::string>> Loader::load() {
    std::map<std::string, std::vector<std::string>> userMovies;
    std::ifstream file(this->loadFromFile);

    // Checking if the file was opened successfully
    if (!file.is_open()) {
        throw std::runtime_error("Failed to open file: " + this->loadFromFile);
    }

    std::string line;
    while (std::getline(file, line)) {
        // Parse the line
        std::istringstream lineStream(line);
        std::string userId;
        std::vector<std::string> movies;
        std::string token;

        // Extract the first token as the user ID
        if (lineStream >> userId) {
            // Extract the rest as movie titles
            while (lineStream >> token) {
                movies.push_back(token);
            }


            userMovies[userId] = movies;
        }
    }

    return userMovies;
}

