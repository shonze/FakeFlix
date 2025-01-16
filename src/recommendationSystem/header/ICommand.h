#ifndef ICOMMAND_H
#define ICOMMAND_H

#include <string>
#include <vector>

// ICommand interface
class ICommand {
public:
    // Virtual destructor to ensure proper cleanup of derived classes
    virtual ~ICommand() {}

    // Pure virtual function to be implemented by derived classes
    virtual std::string execute(const std::string& userId,const std::vector<std::string>& movieIds) = 0;

};

#endif // ICOMMAND_H