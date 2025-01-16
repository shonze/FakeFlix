#ifndef MEMORYWRITEABLE_H
#define MEMORYWRITEABLE_H

#include <string>
#include <vector>

// Interface: writeable
class MemoryWriteable {
public:
    // Pure virtual function to enforce implementation in derived classes
    virtual void writeToMemory(std::string& userId,std::vector<std::string>& movieIds) = 0;

    // Virtual destructor for proper cleanup in derived classes
    virtual ~MemoryWriteable() = default;
    
};

#endif // MEMORYWRITEABLE_H
