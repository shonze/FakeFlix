const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const User= new Schema({
    username: {
        type: String, // Username of the user
        required: true,
        unique: true,
    },
    password: {
        type: String, // Password for the user account
        required: true,
    },
    email: {
        type: String, // email for the user account
        required: true,
        unique: true,
    },
    fullName: {
        type: String, // Password for the user account
        required: true,
    },
    createdAt: {
        type: Date,
        default: Date.now
    },
    photoName: {
        type: String, // URL or path to the user's photo
        required: false,
        default: null, // Default value: null
    },
    photoUrl: {
        type: String, // URL or path to the user's photo
        required: false,
        default: null, // Default value: null
    },
    birthdate: {
        type: Date, // The user's date of birth
        required: false,
        default: null, // Default value: null
    },
    movies: {
        type: Array, // List of movies the user seen
        required: false,
        default: [], // Default value: enpty array
        items: {
          type: String, // movie name
        }
    },
    isAdmin:{
        type: Boolean, // Defines however is the category promoted
        default: false,
    }
});

module.exports = mongoose.model('User', User);