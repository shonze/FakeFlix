const MovieService = require("../services/movies");
const UserService = require('../services/user');

// creates a Movie when POST reqeust is sent to /api/movies

const createMovie = async (req, res) => {
    try {
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        // try {
        //     existingUserByUsername = await UserService.validateAndGetUser(req);
        // } catch (error) {
        //     return res.status(403).json({ errors: error.message });
        // }
        // if (existingUserByUsername.isAdmin == false) { 
        //     return res.status(403).json({ errors: 'User is not an admin' });
        // }
        console.log(req.body);
        const Movie_and_Status = await MovieService.createMovie(req.body.title, req.body.categories,
            req.body.description, req.body.length, req.body.thumbnail, req.body.thumbnailName, req.body.video,req.body.videoName);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status[1];

        // If the status code is not 201, return the errors
        if (status != 201) {
            return res.status(status).json({ errors: Movie_or_Error });
        }

        res.status(status).json(Movie_or_Error);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getMovies = async (req, res) => {
    try {
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        

        const Movie_and_Status = await MovieService.getMovies(existingUserByUsername._id);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status[1];

        // If the status code is not 200, return the errors
        if (status != 200) {
            return res.status(status).json({ errors: Movie_or_Error });
        }

        res.status(status).json(Movie_or_Error);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getMovie = async (req, res) => {
    try {
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }

        const Movie_and_Status = await MovieService.getMovieById(req.params.id);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status[1];

        // If the status code is not 200, return the errors
        if (status != 200) {
            return res.status(status).json({ errors: Movie_or_Error });
        }

        res.status(status).json(Movie_or_Error);
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    };
}

const updateMovie = async (req, res) => {
    try {
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        if (existingUserByUsername.isAdmin == false) { 
            return res.status(403).json({ errors: 'User is not an admin' });
        }

        const Movie_and_Status = await MovieService.updateMovie(req.params.id,req.body.title, req.body.categories,
            req.body.description, req.body.length, req.body.thumbnail, req.body.thumbnailName, req.body.video ,req.body.videoName);


        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status[1];

        // If the status code is not 204, return the errors
        if (status != 204) {
            return res.status(status).json({ errors: Movie_or_Error });
        }

        res.status(status).end();
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const deleteMovie = async (req, res) => {
    try {
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        if (existingUserByUsername.isAdmin == false) { 
            return res.status(403).json({ errors: 'User is not an admin' });
        }

        const Movie_and_Status = await MovieService.deleteMovie(req.params.id);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status[1];

        // If the status code is not 204, return the errors
        if (status != 204) {
            return res.status(status).json({ errors: Movie_or_Error });
        }

        res.status(status).end();
    } catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const searchMovies = async (req, res) => {
    try {

        const Movie_and_Status = await MovieService.searchMovies(req.params.query);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status[1];

        // If the status code is not 200, return the errors
        if (status != 200) {
            return res.status(status).json({ errors: Movie_or_Error });
        }

        res.status(status).json(Movie_or_Error);
    }
    catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const getRecoomendations = async (req, res) => {

    try {
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }

        const Movie_and_Status = await MovieService.getRecoomendations(existingUserByUsername._id, req.params.id);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status.slice(1);

        // If the status code is not 200, return the errors
        if (status != 201 && status != 204 && status != 200 && status != '201' && status != '204' && status != '200') {
            return res.status(status).json({ errors: Movie_or_Error });
        }
        res.status(status).end();
    }
    catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

const updateWatched = async (req, res) => {

    try {        
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }

        const Movie_and_Status = await MovieService.updateWatched(existingUserByUsername._id, req.params.id);

        // Get the status code from the Movie_and_Status array
        const status = Movie_and_Status[0];
        const Movie_or_Error = Movie_and_Status.slice(1);

        // If the status code is not 201, return the errors
        if (status != 201 && status != 204 && status != 200 && status != '201' && status != '204' && status != '200') {
            return res.status(status).json({ errors: Movie_or_Error });
        }
        
        res.status(status).end();
    }
    catch (error) {
        res.status(500).json({ errors: [error.message] });
    }
};

module.exports = { createMovie, getMovies, getMovie, updateMovie, deleteMovie, searchMovies, getRecoomendations, updateWatched };