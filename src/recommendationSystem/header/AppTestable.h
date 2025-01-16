#ifndef APPTESTABLE_H
#define APPTESTABLE_H
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <sstream>
#include <cctype> 
#include <algorithm>
#include "ICommand.h"

class AppTestable {
    private:
        std::map<std::string, ICommand*> commands;
    public:
        // We want to make the app run one command at a time.
        // Instead of while(true)

        AppTestable(std::map<std::string, ICommand*> commands);
        void run(std::vector<std::string> inputs);
};

#endif