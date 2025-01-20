const User = require('../modules/user');
const moongoose = require('mongoose');

/**
 * Creates a User.
 */
const createUser = async (username, password,gmail,fullName, photo, birthdate) => {
    const existingUserByUsername = await User.findOne({ username: username });
        
    if (existingUserByUsername) {
        return([404,'Username already in use']);
    }

    const existingUserByGmail = await User.findOne({ gmail: gmail });
        
    if (existingUserByGmail) {
        return([404,'Gmail already in use']);
    }

    const user = new User({ username: username, password: password,gmail:gmail,fullName:fullName});
    if (photo) user.photo = photo;
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

module.exports = { createUser, getUserById, getUserId, getUsers };
