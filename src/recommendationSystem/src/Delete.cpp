#include "../header/Delete.h"

Delete::Delete(MemoryWriteable* writer, ILoadable* loader,std::string filename)
    : writer_(writer),loader_(loader),filename_(filename) {}

// Destructor implementation
Delete::~Delete() {}

// Override execute method
std::string Delete::execute(const std::string& userId,const std::vector<std::string>& movieIds) {
    if (!(Utilities::isInputValid(userId,movieIds))) {
        return codeMap[400];
    }

    std::map<std::string, std::vector<std::string>> myMap = loader_->load();


    auto it = myMap.find(userId);

    // If the userId is found
    if (it != myMap.end()) { 
        std::vector<std::string> copymovieIds = movieIds;
        if (deletemovieIds(myMap[userId],copymovieIds)) { // ==1 = true
            return codeMap[404]; // logicaly illegal command no such movie
        }
        std::ofstream file(filename_, std::ofstream::trunc); // delete file content
        //write the data back to the file
        for (auto it = myMap.begin(); it != myMap.end(); ++it) {
            // Create non-const copies of the values
            std::string userId = it->first;  // Create a copy of the key
            std::vector<std::string> movieIds = it->second;  // Create a copy of the value
            
            // Pass the non-const copies to writeToMemory
            writer_->writeToMemory(userId, movieIds);
        }

        return codeMap[204]; // user exists so legal 
    } else {
        return codeMap[404]; // logicaly illegal command no such user
    }
}

// Set the writer object
void Delete::setWriter(MemoryWriteable* writer) {
    this->writer_ = writer;
}

// Set the loader object
void Delete::setLoader(ILoadable* loader) {
    this->loader_ = loader;
}

int Delete::deletemovieIds(std::vector<std::string>& originVector,std::vector<std::string>& removeVector) {

    // Check if all elements of removeVector are in originVector
    for (const auto& item : removeVector) {
        if (std::find(originVector.begin(), originVector.end(), item) == originVector.end()) {
            // If an element from removeVector is not in originVector, return 1
            return 1;
        }
    }

    // Remove all elements of removeVector from originVector
    for (const auto& item : removeVector) {
        originVector.erase(std::remove(originVector.begin(), originVector.end(), item), originVector.end());
    }

    // Return 0 after successful removal
    return 0;
}