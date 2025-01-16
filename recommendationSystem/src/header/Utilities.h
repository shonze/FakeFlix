#ifndef UTILITIES_H
#define UTILITIES_H

#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <limits>
#include <sstream>
#include <iostream>
#include <fstream>
#include <string>
#include <algorithm>
#include <cctype>

// A class that contains static functions to use. 

class Utilities {
public:
    // Checks if the str is an positive int.
    static bool isValid(const std::string& str);
    static bool isInputValid(const std::string& userId,const std::vector<std::string>& movieIds);

    static void writeTestFile(const std::string& filename, const std::string& content);

    static std::string readFileContents(const std::string& filename);
};

extern std::map<int,std::string> codeMap;

#endif