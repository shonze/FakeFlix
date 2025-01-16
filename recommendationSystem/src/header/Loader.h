#ifndef LOADER_H
#define LOADER_H

#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <unordered_set>
#include <sstream>


#include "ILoadable.h"

class Loader : public ILoadable {
    private:
        std::string loadFromFile;
    public:
        Loader(std::string fileName);
        std::map<std::string, std::vector<std::string>> load();
};

#endif