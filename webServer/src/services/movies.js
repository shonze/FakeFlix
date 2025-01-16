const mongoose = require("mongoose");
const User = require('../modules/user');
require('custom-env').env(process.env.NODE_ENV, './config');
const MoviesModel = require("../modules/movies");
const CategoriesModel = require("../modules/category");
const userServices = require("./user");
const CategoriesService = require("./category");

const createMovie = async (title, categories, description, length, thumbnail) => {
    // Checks if one of the fields are missing
    if (!title || !categories || !description || !length || !video) {
        return [400, "One of the required fields are missing"];
    }

    // Creates a new Movie and saves it to the database
    if (!thumbnail) {
        thumbnail = "No Thumbnail";
    }

    const Movie = new MoviesModel({ title, categories, description, length, thumbnail , video });

    // Stores the list of categories to save them in the end
    // We dont want to save the movie if one of the categories is not valid
    const ListOfCategories = [];

    const CategoriesIds = [];

    // Checks if the categories are valid and add the movie to the categories list of movies
    for (const categoryName of categories) {
        const Category = await CategoriesModel.findOne({ name: categoryName });

        // Returns 404 if the category is not found
        if (!Category) return [404, "One of the categories was not found"];

        // Add the movie to the category list of movies
        Category.movies.push(Movie._id);

        // Trasnfer the categories field from names to Ids
        CategoriesIds.push(Category._id);

        ListOfCategories.push(Category);
    }

    // Transnfer the categories field from names to Ids
    Movie.categories = CategoriesIds;

    // Save the categories
    for (const category of ListOfCategories) {
        await category.save();
    }

    return await [201, Movie.save()];
};

const getMovieById = async (id) => {
    if (mongoose.Types.ObjectId.isValid(id) == false) return [400, "Movie not found"];

    Movie = await MoviesModel.findById(id);

    // Returns 404 if the Movie is not found
    if (!Movie) return [404, "Movie not found"];

    return [200, Movie];
};

// Gets userId becuase the function require the user to be logged in
const getMovies = async (userId) => {
    // Get all the categories that are promoted
    const promotedCategories = await CategoriesModel.find({ promoted: true });

    // Get the user
    const user = await userServices.getUserById(userId);

    // If the user is not found, return 404
    if (!user) return [404, "User not found"];

    const watchedMovies = user.movies;

    // Create an object to store the random movies by category
    const randomMoviesByCategory = {};

    // For each promoted category, find 20 random movies
    for (const category of promotedCategories) {
        // Randomly shuffle the movies in the category
        const shuffledMovies = category.movies.sort(() => Math.random() - 0.5);

        // Get rid of the movies that the user has already watched
        for (const movie of shuffledMovies) {
            if (watchedMovies.includes(movie)) {
                shuffledMovies.splice(shuffledMovies.indexOf(movie), 1);
            }
        }

        // Get the first 20 movies
        const randomMovies = shuffledMovies.slice(0, 20);

        // Correlate the random movies to the category
        randomMoviesByCategory[category.name] = randomMovies;
    }

    // Get the last 20 watched movies
    const lastWatchedMovies = watchedMovies.slice(-20);

    // Find 20 random movies that the user has watched
    const shuffledlastWatchedMovies = lastWatchedMovies.sort(() => Math.random() - 0.5);

    randomMoviesByCategory["lastWatched"] = shuffledlastWatchedMovies;

    return [200, randomMoviesByCategory];
};

const updateMovie = async (id, title, description, length, thumbnail, categories) => {
    let Movie = await getMovieById(id);

    // Returns 404 if the Movie is not found
    if (Movie[0] != 200) return Movie;

    // Get the movie object from the array
    Movie = Movie[1];

    // Checks if one of the fields are missing
    if (!title || !categories || !description || !length || !video) {
        return [400, "One of the required fields are missing"];
    }

    // Stores the list of categories to save them in the end
    // We dont want to save the movie if one of the categories is not valid
    const ListOfCategories = [];

    const CategoriesIds = [];

    // Remove the old movie from the categories list of movies
    for (const categoryId of Movie.categories) {
        const Category = await CategoriesModel.findOne({ _id: categoryId });

        // Returns 404 if the category is not found
        if (!Category) return [404, "One of the categories was not found"];

        // Remove the movie from the category list of movies
        Category.movies = Category.movies.filter(movie_id => {
            return movie_id.toString() !== Movie._id.toString();
        });

        ListOfCategories.push(Category);
    }

    if (!thumbnail) {
        thumbnail = "No Thumbnail";
    }
    // Update the movie fields
    Movie.title = title;
    Movie.description = description;
    Movie.length = length;
    Movie.thumbnail = thumbnail;
    Movie.categories = categories;
    Movie.video = video;

    // Checks if the categories are valid and add the movie to the categories list of movies
    for (const categoryName of categories) {
        const Category = await CategoriesModel.findOne({ name: categoryName });

        // Returns 404 if the category is not found
        if (!Category) return [404, "One of the categories was not found"];

        // Add the movie to the category list of movies
        Category.movies.push(Movie._id);

        CategoriesIds.push(Category._id);  

        ListOfCategories.push(Category);
    }

    // Transfer the categories field from names to Ids
    Movie.categories = CategoriesIds;

    await Movie.save();

    // Save the categories
    for (const category of ListOfCategories) {
        await category.save();
    }

    return [204, Movie];
};

const deleteMovie = async (id) => {
    let Movie = await getMovieById(id);

    // Returns 404 if the Movie is not found
    if (Movie[0] != 200) return Movie;

    Movie = Movie[1];

    // We dont want to save the movie if one of the categories is not valid
    const ListOfObjects = [];

    // Remove the old movie from the categories list of movies
    for (const categoryId of Movie.categories) {
        const Category = await CategoriesModel.findOne({ _id: categoryId });

        // Returns 404 if the category is not found
        if (!Category) return [404, "One of the categories was not found"];

        // Remove the movie to the category list of movies
        Category.movies = Category.movies.filter(movie_id => {
            return movie_id.toString() !== Movie._id.toString();
        });

        ListOfObjects.push(Category);
    }

    // Remove the movie from the user's watched movies in the cpp recommendation server
    const net = require('net');

    const ip = process.env.CPP_IP || 'host.docker.internal'; // Ensure IP is defined
    const port = process.env.CPP_PORT || 3001; // Ensure port is defined

    const client = new net.Socket();

    // Use a Promise to handle asynchronous connection and communication
    await new Promise((resolve, reject) => {
        client.connect(port, ip, async () => {
            try {
                const users = await userServices.getUsers();

                for (const user of users) {

                    // Remove the movie to the user list of movies
                    user.movies = user.movies.filter(movie_id => {
                        return movie_id.toString() !== Movie._id.toString();
                    });

                    // To save it later if no problems accure
                    ListOfObjects.push(user);

                    // Send a DELETE command to the server
                    await new Promise((resolve) => {
                        client.write(`DELETE ${user._id.toString()} ${id}`);

                        // Wait for response before sending next command
                        const handleData = (data) => {
                            console.log('Received response:', data.toString());
                            client.removeListener('data', handleData);
                            resolve();
                        };

                        client.on('data', handleData);
                    });
                }
                resolve();
            } catch (error) {
                client.destroy();
                reject(error);
            }
        });

        // Handle any connection errors
        client.on('error', (err) => {
            reject(err);
        });

        // Ensure the socket is closed properly
        client.on('close', () => {
            console.log("Connection closed");
        });
    });

    // After communication is done, destroy the client socket
    client.destroy();


    await Movie.deleteOne();

    // Save the categories
    for (const category of ListOfObjects) {
        await category.save();
    }

    return [204, Movie];
};

const searchMovies = async (query) => {
    const Movies = await MoviesModel.find({
        $or: [
            // Checks if any of the fields contain the query
            { title: { $regex: query } },
            { description: { $regex: query } },
            { categories: { $elemMatch: { $regex: query } } },
            { length: { $regex: query } }
        ]
    });

    // Returns 404 if no Movies are found
    if (Movies.length == 0) return [404, "No Movies found"];

    return [200, Movies];
};

const getRecoomendations = async (userId, movieId) => {
    const net = require('net');

    const ip = 'host.docker.internal'; // need to change
    const port = process.env.CPP_PORT;// was 3001 before change

    const user = await userServices.getUserById(userId);

    if (!user) {
        return([404, "User not found"]);
    }
    const movie = await MoviesModel.findById(movieId);

    if (!movie) {
        return([404, "Movie not found"]);
    }
    
    return new Promise((resolve, reject) => {
        const client = new net.Socket();

        client.connect(port, ip, () => {
            client.write(`GET ${userId} ${movieId}`);
        });

        client.on('data', (data) => {
            try {
                // Convert the data to a string
                const dataString = data.toString();

                // Split the string by newline to separate status and movies
                const parts = dataString.split('\n').map(part => part.trim());

                // Extract the status code from the first part (e.g., "200 Ok")
                const statusLine = parts[0];
                const statusMatch = statusLine.match(/^(\d{3})/); // Match the status code (e.g., "200")
                const status = statusMatch ? parseInt(statusMatch[1], 10) : null; // Extract and parse the status code

                // Extract the movies (remaining lines after an empty line)
                const movies = parts.slice(1).filter(movie => movie);

                // Combine the status and movies into a single array
                const result = status !== null ? [status, ...movies] : movies;

                // Resolve the promise with the result
                resolve(result);
            } catch (error) {
                reject(error); // Handle parsing errors
            } finally {
                client.destroy(); // Always destroy the client
            }
        });

        client.on('error', (err) => {
            reject(err); // Reject the promise on connection errors
            client.destroy();
        });
    });
};


const updateWatched = async (userId, movieId) => {
    const net = require('net');

    const ip = 'host.docker.internal'; // need to change
    const port = process.env.CPP_PORT;// was 3001 before change

    const user = await userServices.getUserById(userId);

    if (!user) {
        return([404, "User not found"]);
    }
    const movie = await MoviesModel.findById(movieId);

    if (!movie) {
        return([404, "Movie not found"]);
    }


    return new Promise((resolve, reject) => {
        const client = new net.Socket();

        client.connect(port, ip, () => {
            client.write(`POST ${userId} ${movieId}`);
            client.write(`PATCH ${userId} ${movieId}`);
        });

        client.on('data', async(data) => {
            try {
                // Convert the data to a string
                const dataString = data.toString();
                console.log(`recived : ${dataString}`);
                
                if (!user.movies.includes(movieId)) { 
                    user.movies.push(movieId); // Add the movie ID to the movies list
                    await user.save(); // Save the updated user document
                }

                // Resolve the promise with the result
                resolve([201, ""]);
            } catch (error) {
                reject(error); // Handle parsing errors
            } finally {
                client.destroy(); // Always destroy the client
            }
        });

        client.on('error', (err) => {
            reject(err); // Reject the promise on connection errors
            client.destroy();
        });
    });
};


module.exports = { createMovie, getMovieById, getMovies, updateMovie, deleteMovie, searchMovies, getRecoomendations, updateWatched };