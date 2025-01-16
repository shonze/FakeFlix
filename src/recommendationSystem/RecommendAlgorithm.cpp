#include "./header/RecommendAlgorithm.h"

RecommendAlgorithm::RecommendAlgorithm(const std::string& userId,const std::string& movieId,
                                       std::map<std::string, std::vector<std::string>> movieMap)
   : recommendUserId(userId), recommendMovieId(movieId), movieMap(movieMap) {
   }

RecommendAlgorithm::~RecommendAlgorithm() {}

bool RecommendAlgorithm::setMoviesWatched(){
    auto it = movieMap.find(recommendUserId); // Find the user in the map
    if (it != movieMap.end()) {
        this->moviesWatched = it->second; // Assign the value (vector of movies) to moviesWatched
    } else {
        return false;
    }
    return true;
}

void RecommendAlgorithm::setWeights(){
    // Iterate over the UserIds
    for(auto userInfo : movieMap){
        if(userInfo.first == this->recommendUserId){
            continue;
        }
        int weight = 0;
        const std::string& user = userInfo.first; // User Id
        const std::vector<std::string>& movies = userInfo.second; // Movies watched by the user

        // Search for movies that are common with the user we should recommend to
        for(auto movieId : moviesWatched){
            auto it = std::find(movies.begin(), movies.end(), movieId);
            if (it != movies.end()) {
                // MovieId was found, so increase the weights according to the algorithm
                weight++;
            }
        }
        this->weights.insert(std::pair<std::string,int>(user,weight));
    }
}

void RecommendAlgorithm::setGrades(){
    // For more efficent search in moviesWatched
    std::unordered_set<std::string> watchedSet(moviesWatched.begin(), moviesWatched.end());

    for(auto userInfo : movieMap){
        const std::string& userId = userInfo.first; // User Id
        const std::vector<std::string>& movies = userInfo.second; // Movies watched by the user

        if(userId == this->recommendUserId || weights.at(userId) == 0){
            continue;
        }
        // Search if the recommended movie was watched by the user
        if(std::find(movies.begin(), movies.end(), this->recommendMovieId) == movies.end()){
            // The recommended movie was not found, So we dont take the user into account.
            continue;
        }
        // Go through all the moveis to add grades for them
        for(auto movieId : movies){
            // We dont recommend the recommended movie
            // If the user we recommend to already watched the movie, we don't give it a grade
            if (watchedSet.find(movieId) != watchedSet.end() || movieId == this->recommendMovieId) {
                continue; // Movie already watched, or is the recommended movie
            }

            // Add the weight of the user to the grade of the movie
            this->moviesGrade[movieId] += this->weights[userId];
        }
    }
}

std::vector<std::string> RecommendAlgorithm::getBest(){
    // Transform the map into a vector
    std::vector<std::pair<std::string, int>> sortedVector(moviesGrade.begin(), moviesGrade.end());

    // Defining the range as whole vector
    auto first = sortedVector.begin();
    auto last = sortedVector.end();
  
    // Defining an comper function with tie breaker.
    auto comp = [] (std::pair<std::string, int> a, std::pair<std::string, int> b) {
        if(a.second == b.second){
            // Sort by key, here its from small to big
            // We convert them to char*,using c_str, and than to int using atoi
            return atoi(a.first.c_str()) < atoi(b.first.c_str());
        } else {
            // Sort from big to small
            return a.second > b.second;
        }
    };
      
    // Sorting the vector from big to small with tie breaker
    sort(first, last, comp);
    
    std::vector<std::string> recommended;
    // Take the 10 most recommended(with the highest grade) movies 
    for(int i = 0;i < sortedVector.size() && i < 10;i++){
        // Pushing the movieId 
        recommended.push_back(sortedVector.at(i).first);
    }
    return recommended;
}

int RecommendAlgorithm::Algorithm(std::vector<std::string>& recommended){
    try{
    // Get the movies that userId watched
    if(!setMoviesWatched()){
        // Special case for this 
        return 0;
    }

    // Set the weights map
    setWeights();

    // Set the grades map
    setGrades();

    // Gets the 10 best movies orderd and return it.
    recommended = getBest();
    return 1;
    } catch( ... ){
        return -1;
    }
}