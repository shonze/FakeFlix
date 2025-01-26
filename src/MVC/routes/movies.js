const express = require("express");
const router = express.Router();
const MoviesController = require("../controllers/movies");

router.route('/')
    .get(MoviesController.getMovies)
    .post(MoviesController.createMovie);

router.route('/:id')
    .get(MoviesController.getMovie)
    .put(MoviesController.updateMovie)
    .delete(MoviesController.deleteMovie);

router.route('/search/:query/')
    .get(MoviesController.searchMovies);

router.route('/:id/recommend/')
    .get(MoviesController.getRecoomendations)
    .post(MoviesController.updateWatched);

module.exports = router;