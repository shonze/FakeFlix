const UserService = require('../services/user');
const jwt = require('jsonwebtoken');
require('custom-env').env(process.env.NODE_ENV, './config');
/**
 * Handles user authentication by checking if the username and password are valid.
 */
const authenticateUser = async (req, res) => {
    try {
        const username= req.body.username;
        const password= req.body.password;

        if (!username || !password) {
            return res.status(400).json({ errors: ['Username and password are required'] });
        }

        const userId = await UserService.getUserId(username, password);
        if (!userId) {
            return res.status(404).json({ errors: ['User not found or invalid credentials'] });
        }

        const user = await UserService.getUserById(userId);
        console.log(user);
        const isAdmin = user.isAdmin;
        console.log(isAdmin);

        const data = { username: username, isAdmin: isAdmin };
        const token = jwt.sign(data, process.env.JWT_SECRET)
        console.log(token);
        res.status(201).json({ token });

    } catch (error) {
        console.error('Error authenticating user:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

module.exports = { authenticateUser};
