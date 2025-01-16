#include "RecommendAlgorithm.h"

class RecommendAlgorithmTestable : public RecommendAlgorithm {
public:
    RecommendAlgorithmTestable(const std::string& userId, 
                               const std::string& movieId, 
                               std::map<std::string, std::vector<std::string>> movieMap)
        : RecommendAlgorithm(userId, movieId, movieMap) {}

    // Expose protected methods for testing
    using RecommendAlgorithm::setWeights;
    using RecommendAlgorithm::setGrades;
    using RecommendAlgorithm::setMoviesWatched;
    using RecommendAlgorithm::getBest;
    using RecommendAlgorithm::moviesGrade;    
    using RecommendAlgorithm::weights; 
    using RecommendAlgorithm::moviesWatched;     
};