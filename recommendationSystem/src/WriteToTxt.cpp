#include "../header/WriteToTxt.h"


WriteToTxt::WriteToTxt(const std::string& filename) : filename_(filename) {}

void WriteToTxt::writeToMemory(std::string& userId,std::vector<std::string>& movieIds) {
    movieIds = this->distinct(movieIds);
    std::vector<std::string> lines;
    bool userIdFound = false;

    // Check if the file exists and open it for reading
    std::ifstream inFile(filename_);
    if (!inFile.is_open()) {
        // File does not exist, create a new one
        std::ofstream outFile(filename_);
        if (!outFile.is_open()) {
            throw std::ios_base::failure("Failed to create file: " + filename_);
        }
        outFile.close();
    } else {
        // Read the file content into memory
        std::string line;
        while (std::getline(inFile, line)) {
            std::istringstream iss(line);
            std::string firstWord;
            iss >> firstWord;

            if (firstWord == userId) {
                // userId found; append the vector of strings to this line
                std::ostringstream updatedLine;
                updatedLine << firstWord; // Ensure we only append once
                std::set<std::string> uniqueValues; // To avoid duplicates

                // Insert existing movies from the line
                std::string existingMovie;
                while (iss >> existingMovie) {
                    uniqueValues.insert(existingMovie);
                }

                // Insert new movies
                for (const auto& value : movieIds) {
                    uniqueValues.insert(value);
                }

                // Construct the updated line
                for (const auto& value : uniqueValues) {
                    updatedLine << " " << value;
                }

                lines.push_back(updatedLine.str());
                userIdFound = true;
            } else {
                // Keep the line as is
                lines.push_back(line);
            }
        }
        inFile.close();
    }

    // If the userId wasn't found, add a new line
    if (!userIdFound) {
        std::ostringstream newLine;
        newLine << userId;
        for (const auto& value : movieIds) {
            newLine << " " << value;
        }
        lines.push_back(newLine.str());
    }

    // Write the updated lines back to the file
    std::ofstream outFile(filename_, std::ios::trunc); // Truncate the file to overwrite
    if (!outFile.is_open()) {
        throw std::ios_base::failure("Failed to open file for writing: " + filename_);
    }

    for (size_t i = 0; i < lines.size(); ++i) {
        outFile << lines[i];
        if (i != lines.size() - 1) {
            outFile << "\n"; // Avoid adding an extra newline at the end
        }
    }

    outFile.close();
}
std::vector<std::string>WriteToTxt::distinct(std::vector<std::string>& input) {
    std::unordered_set<std::string> seen; // To track already seen strings
    std::vector<std::string> result;

    for (const auto& str : input) {
        // If the string is not already in the set, add it to the result
        if (seen.find(str) == seen.end()) {
            seen.insert(str);
            result.push_back(str);
        }
    }
    return result;
}