const UserService = require('../services/user');
const jwt = require('jsonwebtoken');
require('custom-env').env(process.env.NODE_ENV, './config');
/**
 * Handles user authentication by checking if the username and password are valid.
 */
const authenticateUser = async (req, res) => {
    try {
        const username = req.body.username;
        const password = req.body.password;

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

const validateUser = async (req, res) => {
    console.log("entered")
    try {
        let existingUserByUsername;
        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        console.log("requireadmin:")
        console.log(req.headers.requireadmin)
        console.log(req.headers)
        if ((req.headers.requireadmin == 'true') || (req.headers.requireadmin === 'true') || (req.headers.requireadmin == true) || (req.headers.requireadmin === 'true')) {
            if (!existingUserByUsername.isAdmin) {
                return res.status(403).json({ errors: ['Not an admin!'] });
            }
        }

        return res.status(200).json({ isAdmin: existingUserByUsername.isAdmin });
         // In case the Category ID is not valid
    } catch (error) {
        return res.status(500).json({ errors: ['Error occured'] });
    }
};

const getUserPic = async (req, res) => {
    try {
        let existingUserByUsername;
        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await UserService.validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        console.log("in tokens controller: " + existingUserByUsername.photoUrl);
        return res.status(200).json({ userPicture: existingUserByUsername.photoUrl ,userFullName: existingUserByUsername.fullName });
    } catch (error) {
        return res.status(500).json({ errors: ['Error occured'] });
    }
};
module.exports = { authenticateUser, validateUser, getUserPic };