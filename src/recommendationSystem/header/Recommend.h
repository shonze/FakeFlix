#ifndef RECOMMEND_H
#define RECOMMEND_H

#include "ICommand.h"
#include "ILoadable.h"
#include "RecommendAlgorithm.h"
#include "Loader.h" 
#include "Utilities.h"

#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <limits>

class Recommend : public ICommand {
private:
    ILoadable* loader;          // Loader to load database

    std::string userId; // The user we should recommend to

    std::string movieId; // The movie we should recommend by 

    void setMovieId(const std::string& movieId); // Setter for movieId

    void setUserId(const std::string& userId); // Setter for userId

    std::string printRecommendation(std::vector<std::string> recommended); // Print the recommendation by order
public:
    // Constructor
    Recommend(ILoadable* bloader);

    // Destructor
    virtual ~Recommend();

    // Override execute method from ICommand
    std::string execute(const std::string& userId,const std::vector<std::string>& movieIds) override;
};

#endif // RECOMMEND_H