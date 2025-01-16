const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const Category = new Schema({
    name: {
        type: String, // Name of the category
        required: true,
    },
    promoted:{
        type: Boolean, // Defines however is the category promoted
        required: true,
    },
    description: {
        type: String, // Brief description of the category
        required: false,
        default: "No description available.", // Default value: 'No description available.'
    },
    createdAt: {
        type: Date, // Timestamp for when the category was created
        required: false,
        default: Date.now, // Default value: current date and time
    },
    movies: {
        type: Array, // List of movies belonging to this category
        required: false,
        default: [], // Default value: empty array
        items: {
          type: String, // Movie name
        }
    }
});

module.exports = mongoose.model('Category', Category);