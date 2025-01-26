#include "../header/Help.h"

// Constructor implementation
Help::Help(){}


// Destructor implementation
Help::~Help() {
    // Any cleanup code can go here if needed
}

// Override execute method from ICommand
std::string Help::execute(const std::string& userId,const std::vector<std::string>& movieIds) {
    if (!userId.empty() || !movieIds.empty()) {
        return codeMap[400];
    }


    std::string result;
    std::vector<std::string> strings;
    std::string xdelete = "DELETE, arguments: [userid] [movieid1] [movieid2] ...\n";
    strings.push_back(xdelete);
    std::string get = "GET, arguments: [userid] [movieid]\n";
    strings.push_back(get);
    std::string patch = "PATCH, arguments: [userid] [movieid1] [movieid2] ...\n";
    strings.push_back(patch);
    std::string post = "POST, arguments: [userid] [movieid1] [movieid2] ...\n";
    strings.push_back(post);
    std::string help = "help\n";
    strings.push_back(help);
    for (const auto& str : strings) {
        result += str;
    }
    return result;
}
