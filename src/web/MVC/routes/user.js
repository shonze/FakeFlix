const express = require('express');
var router = express.Router();
const UserController = require('../controllers/user');

/**
 * Route to create a new User.
 */
router.route('/')
    .post(UserController.createUser);

router.route('/check')
    .post(UserController.checkUser);

/**
 * Route to get a User by ID.
 */
router.route('/:id')
    .get(UserController.getUser)

module.exports = router;
