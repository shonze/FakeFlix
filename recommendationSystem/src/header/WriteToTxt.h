#ifndef WRITETOTXT_H
#define WRITETOTXT_H

#include "MemoryWriteable.h" // Assuming this is where the `writeable` interface is defined
#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include <sstream>
#include <stdexcept>
#include <set>
#include <unordered_set>

// Class: WriteToTxt
class WriteToTxt : public MemoryWriteable {
public:
    // Constructor
    WriteToTxt(const std::string& filename);

    // Method to update movie IDs for a given person in memory and write back to the file
    void writeToMemory(std::string& userId,std::vector<std::string>& movieIds) override;
    std::vector<std::string> distinct(std::vector<std::string>& input);

private:
    std::string filename_; // File name for the text file
};

#endif // WRITETOTXT_H
