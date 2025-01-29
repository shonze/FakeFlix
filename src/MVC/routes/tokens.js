const express = require('express');
var router = express.Router();
const TokensController = require('../controllers/tokens');

/**
 * Route to create a new User.
 */
router.route('/')
    .post(TokensController.authenticateUser);
router.route('/validate')
    .post(TokensController.validateUser);
router.route('/user')
    .get(TokensController.getUserPic);
    
module.exports = router;