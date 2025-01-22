#include "../header/Utilities.h"

bool Utilities::isValid(const std::string& str) {

    if (str.empty()) return false; // Empty string is not an integer

        
    for (size_t i = 0; i < str.length(); ++i) {
        if (!isxdigit(str[i])) return false; // Check if character is not a valid hex digit

        if (std::isupper(static_cast<unsigned char>(str[i]))) return false;
    }
    
    return true; // All characters are valid hex digits

}

bool Utilities::isInputValid(const std::string& userId,const std::vector<std::string>& movieIds) {

    if (!(Utilities::isValid(userId))) {
        return false;
    }

    // Check if movieIds vector is empty
    if(movieIds.empty()){
        return false;
    }

    for (const std::string& s : movieIds) {
        if (!(Utilities::isValid(s))) {
            return false;
        }
    }
    return true;

}

// Helper function to write temporary files for testing
void Utilities::writeTestFile(const std::string& filename, const std::string& content) {
    std::ofstream file(filename);
    if (!file.is_open()) {
        throw std::runtime_error("Could not create test file: " + filename);
    }
    file << content;
    file.close();
}

// Helper function to read the content of the file for verification
std::string Utilities::readFileContents(const std::string& filename) {
    std::ifstream file(filename);
    std::ostringstream content;

    if (file) {
        content << file.rdbuf();  // Read the entire file into the content string
    } else {
        throw std::runtime_error("Could not open file");
    }

    return content.str();
}

std::map<int,std::string> codeMap = {
    {200, "200 Ok\n\n"},
    {201, "201 Created\n"},
    {204, "204 No Content\n"},
    {400, "400 Bad Request\n"},
    {404, "404 Not Found\n"}
};
