#ifndef HELP_H
#define HELP_H

#include "ICommand.h"
#include <map>
#include <string>
#include <vector>
#include <iostream>
#include "Utilities.h"

class Help : public ICommand {
private:
public:
    // Constructor
    Help();

    // Destructor
    virtual ~Help();

    // Override execute method from ICommand
    std::string execute(const std::string& userId,const std::vector<std::string>& movieIds) override;

};

#endif // HELP_H