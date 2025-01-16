#ifndef RECOMMENDALGORITHM_H
#define RECOMMENDALGORITHM_H

#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <algorithm>
#include <unordered_set>

class RecommendAlgorithm {
private:
    // The user we should recommend to
    const std::string& recommendUserId; 

    // The movie we should recommend by
    const std::string& recommendMovieId; 

    // The database of When the key is a userId.
    std::map<std::string, std::vector<std::string>> movieMap; 
protected:
    // Puting the methods here so we can test them

    // Returns the 10 best movies orderd from best to worst.
    std::vector<std::string> getBest();

    // Get the movies watched by userId by itirating on the map
    // Bool return value for validity resones
    bool setMoviesWatched();

    // Set the grades that for each movie it sums the weights of the users that watched him.
    void setGrades(); 

    // Set the weights that each user by the number of common movies with userId.
    void setWeights();

    // movies watched by userId provided in the constructor 
    std::vector<std::string> moviesWatched; 

    // Holds the weight of recommendation for each userId
    std::map<std::string, int> weights; 

    // Holds the grade of recommendation for each movieId
    std::map<std::string, int> moviesGrade; 
public:
    // Constructor
    RecommendAlgorithm(const std::string& userId,const std::string& movieIds,std::map<std::string, std::vector<std::string>> movieMap);

    // Destructor
    virtual ~RecommendAlgorithm();

    // Recommendation Algorithm that will return true if function works and false if not. 
    // Should put in recommended the 10 movies with best grades.
    int Algorithm(std::vector<std::string>& recommended);
    // Returns int for validity check
};

#endif // RECOMMENDALGORITHM_H