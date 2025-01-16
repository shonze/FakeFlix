#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>

#ifndef ILOADABLE_H
#define ILOADABLE_H


class ILoadable {

    public:
        virtual std::map<std::string, std::vector<std::string>> load() = 0;
};
#endif
