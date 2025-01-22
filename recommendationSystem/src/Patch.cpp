#include "../header/Patch.h"

Patch::Patch(MemoryWriteable* writer, ILoadable* loader)
    : writer_(writer),loader_(loader) {}


// Destructor implementation
Patch::~Patch() {}

// Override execute method
std::string Patch::execute(const std::string& userId,const std::vector<std::string>& movieIds) {
    if (!(Utilities::isValid(userId))) {
        return codeMap[400];
    }

    // Check if movieIds vector is empty
    if(movieIds.empty()){
        return codeMap[400];
    }

    for (const std::string& s : movieIds) {
        if (!(Utilities::isValid(s))) {
            return codeMap[400];
        }
    }

    std::map<std::string, std::vector<std::string>> myMap = loader_->load();
    if (myMap.find(userId) != myMap.end()) { // only differnce from Post in the '=' insted of '!'
        std::string copyuserId = userId;  // Create a copy of the key
        std::vector<std::string> copymovieIds = movieIds;  // Create a copy of the value
        writer_->writeToMemory(copyuserId , copymovieIds);
        return codeMap[204]; // user exists so legal 
    } else {
        return codeMap[404]; // logicaly illegal command
    }
}

// Set the writer object
void Patch::setWriter(MemoryWriteable* writer) {
    this->writer_ = writer;
}

// Set the loader object
void Patch::setLoader(ILoadable* loader) {
    this->loader_ = loader;
}
