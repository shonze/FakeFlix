#ifndef DeleteH
#define Delete_H

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
#include <functional>

class Delete : public ICommand {
private:
    MemoryWriteable* writer_;          // Reference to a MemoryWriteable object
    ILoadable* loader_;          // Reference to a MemoryWriteable object
    std::string filename_;

public:
    // Constructor
    Delete(MemoryWriteable* writer, ILoadable* loader,std::string filename);

    // Destructor
    virtual ~Delete();

    // Override execute method from ICommand
    std::string execute(const std::string& userId,const std::vector<std::string>& movieIds)override;

    void setWriter(MemoryWriteable* writer);
    void setLoader(ILoadable* loader);
    int deletemovieIds(std::vector<std::string>& originVector,std::vector<std::string>& removeVector);

};

#endif // Delete_H