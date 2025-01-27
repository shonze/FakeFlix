const User = require('../modules/user');
const moongoose = require('mongoose');
require('custom-env').env(process.env.NODE_ENV, './config');
const jwt = require("jsonwebtoken");
/**
 * Creates a User.
 */
const createUser = async (username, password,email,fullName, photoName,photoUrl, birthdate) => {
    const existingUserByUsername = await User.findOne({ username: username });
        
    if (existingUserByUsername) {
        return([404,'Username already in use']);
    }

    const existingUserByEmail = await User.findOne({ email: email });
        
    if (existingUserByEmail) {
        return([404,'email already in use']);
    }

    const user = new User({ username: username, password: password,email:email,fullName:fullName});
    if (photoName) user.photoName = photoName;
    if (photoUrl) user.photoUrl = photoUrl;
    if (birthdate) user.birthdate = birthdate;
    return await user.save();
};

/**
 * Retrieves a user by its ID.
 */
const getUserById = async (id) => {
    if (!moongoose.Types.ObjectId.isValid(id)) return null;
    return await User.findById(id);
};

/**
 * Retrieves all users.
 */
const getUsers = async () => { return await User.find({}); };

/**
 * Checks if the user exists by its username and password.
 */
const getUserId = async (username, password) => {
    const user = await User.findOne({ username: username, password: password });
    if (!user) return null;
    return user.id;
};

const validateAndGetUser = async (req) => {
    if (!req.headers.authorization) {
        throw new Error('User not logged in');
    }

    const token = req.headers.authorization.split(" ")[1];
    let data;

    try {
        data = jwt.verify(token, process.env.JWT_SECRET);
        console.log('The logged in user is: ' + data.username);
    } catch (err) {
        throw new Error('Invalid Token');
    }

    const existingUserByUsername = await User.findOne({ username: data.username });
    if (!existingUserByUsername) {
        throw new Error('User not found');
    }

    return existingUserByUsername;
};

module.exports = { createUser, getUserById, getUserId, getUsers };
