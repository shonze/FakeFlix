#ifndef PATCHH
#define PATCH_H

#include "WriteToTxt.h"
#include "ICommand.h"
#include "MemoryWriteable.h"
#include "Utilities.h"
#include "Loader.h"

#include <string>
#include <vector>
#include <iostream>
#include <algorithm>
#include <limits>

class Patch : public ICommand {
private:
    MemoryWriteable* writer_;          // Reference to a MemoryWriteable object
    ILoadable* loader_;          // Reference to a MemoryWriteable object

public:
    // Constructor
    Patch(MemoryWriteable* writer, ILoadable* loader);

    // Destructor
    virtual ~Patch();

    // Override execute method from ICommand
    std::string execute(const std::string& userId,const std::vector<std::string>& movieIds) override;

    void setWriter(MemoryWriteable* writer);
    void setLoader(ILoadable* loader);

};

#endif // PATCH_H