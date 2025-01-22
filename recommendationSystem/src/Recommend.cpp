#include "../header/Recommend.h"

Recommend::Recommend(ILoadable* bloader)
    : loader(bloader) {}


Recommend::~Recommend() {}


// Return the recommendation string.
std::string Recommend::printRecommendation(std::vector<std::string> recommended){
    std::string recommendation;
    for(int i = 0;i < recommended.size();i++){
        recommendation.append(recommended.at(i));
        recommendation.append(" ");
    }
    recommendation.append("\n");
    return recommendation;
}

// Set the movie IDs
void Recommend::setMovieId(const std::string& movieId) {
    this->movieId.assign(movieId); 
}

// Set the user ID
void Recommend::setUserId(const std::string& userId) {
    this->userId.assign(userId);
}

std::string Recommend::execute(const std::string& userId,const std::vector<std::string>& movieIds){
    try{
        // Check for the validatin of the input
        // If not valid. We need to return the correct code
        if(movieIds.size() != 1){
            return codeMap[400];
        }
        if(!Utilities::isValid(userId) || !Utilities::isValid(movieIds.at(0))){
            return codeMap[400];
        }

        // Set user and movie id
        setMovieId(movieIds.at(0));
        setUserId(userId);

        // Load the data file 
        std::map<std::string, std::vector<std::string>> map = loader->load();
        if(map.empty()){
            return codeMap[404];
        }
        
        // Construct an algorithm class
        RecommendAlgorithm algorithm(this->userId,this->movieId,map);

        // Get recommended movies
        std::vector<std::string> recommended;

        int validity_algorithm = algorithm.Algorithm(recommended);
        if(validity_algorithm == -1){
            // An error occured, so no need to print
            return "";
        } else if(validity_algorithm == 0){
            // This means the user was not found
            return codeMap[404];
        }

        // Get the return value of the command
        // The command is valid, so we print the correlated code
        if(recommended.size() <= 0) {
            return codeMap[404];
        }

        std::string output = codeMap[200];
        output.append(printRecommendation(recommended));

        return output;
    } catch( ... ){
        // If an error accures, not need to print
        return codeMap[400];
    }
}