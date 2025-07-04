const UserService = require('../services/user');
const User = require('../modules/user');
const jwt = require('jsonwebtoken');
require('custom-env').env(process.env.NODE_ENV, './config');
/**
 * Creates a new User.
 */
const createUser = async (req, res) => {
    try {
        console.log(req.body);
        if(!req.body.username) return res.status(400).json({ errors: ['Username is required'] });
        if(!req.body.password) return res.status(400).json({ errors: ['Password is required'] });
        if(!req.body.email) return res.status(400).json({ errors: ['Email is required'] });
        if(!req.body.fullName) return res.status(400).json({ errors: ['Full name is required'] });

        if (req.body.password.length < 8) {
            return res.status(400).json({ errors: ['Password must be at least 8 characters long'] });
        }

        if (Date.parse(req.body.birthdate) > Date.now() || Date.parse(req.body.birthdate) < Date.parse('1900-01-01')) {
            return res.status(400).json({ errors: ['Birthdate not valid'] });
        }

        const newUser = await UserService.createUser(
            req.body.username,
            req.body.password,
            req.body.email,
            req.body.fullName,
            req.body.photoName,
            req.body.photoUrl,
            req.body.birthdate
        );
        if (newUser[0] == 404) {
            return res.status(404).json({ errors: newUser[1] });
        }
        
        const data = { username: req.body.username, isAdmin: false };
        const token = jwt.sign(data, process.env.JWT_SECRET)
        return res.status(201).json({ token });

    } catch (error) {
        console.error('Error creating user:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

const checkUser = async (req, res) => {
    console.log("BALLS BBABABABABBABABA")
    console.log(req.body);
    console.log("USERNAME")
    console.log(req.body.username);
    try {

        if(!req.body.username) return res.status(400).json({ errors: ['Username is required'] });
        if(!req.body.password) return res.status(400).json({ errors: ['Password is required'] });
        if(!req.body.email) return res.status(400).json({ errors: ['Email is required'] });
        if(!req.body.fullName) return res.status(400).json({ errors: ['Full name is required'] });

        if (req.body.password.length < 8) {
            return res.status(400).json({ errors: ['Password must be at least 8 characters long'] });
        }

        if (Date.parse(req.body.birthdate) > Date.now() || Date.parse(req.body.birthdate) < Date.parse('1900-01-01')) {
            return res.status(400).json({ errors: ['Birthdate not valid'] });
        }
        console.log("PASEED")
        const existingUserByUsername = await User.findOne({ username: req.body.username });
            
        if (existingUserByUsername) {
            return res.status(400).json({ errors: ['Username already in use'] })
        }

        const existingUserByEmail = await User.findOne({ email: req.body.email });
            
        if (existingUserByEmail) {
            return res.status(400).json({ errors: ['email already in use'] })
        }
        console.log("PASEED check")
        return res.status(201).json({});

    } catch (error) {
        console.error('Error creating user:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

/**
 * Retrieves all users.
 */
const getUsers = async (req, res) => {
    try {
        const users = await UserService.getUsers();
        res.json(users);
    } catch (error) {
        console.error('Error retrieving users:', error);
        res.status(404).json({ errors: ['Internal server error'] });
    }
};

/**
 * Retrieves a single User by ID.
 */
const getUser = async (req, res) => {
    try {
        const user = await UserService.getUserById(req.params.id);
        if (!user) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        res.json(user);
    } catch (error) {
        console.error('Error retrieving user:', error);
        res.status(404).json({ errors: ['User not found'] });
    }
};

/**
 * Checks if a user exists by ID.
 */
const userExists = async (req, res) => {
    try {
        const id = await UserService.getUserId(req.params.username, req.params.password);
        if (!id) {
            return res.status(404).json({ errors: ['User not found'] });
        }
        res.json({ id });
    } catch (error) {
        console.error('Error checking user existence:', error);
        res.status(404).json({ errors: ['User not found'] });
    }
};

module.exports = { createUser, getUsers, getUser, userExists,checkUser };
