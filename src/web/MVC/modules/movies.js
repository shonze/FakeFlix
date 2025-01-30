const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const MovieSchema = new Schema({
    title: {
        type: String,
        required: true
    },
    categories: {
        type: Array, 
        required: true,
        items: {
          type: String, 
        }
    },
    description: {
        type: String,
        required: true
    },
    length: {
        type: String,
        required: true
    },
    thumbnail: {
        type: String,
        required: true
    },
    thumbnailName: {
        type: String,
        required: true
    },
    video: {
        type: String,
        required: true
    },
    videoName: {
        type: String,
        required: true
    }
});

module.exports = mongoose.model('Movie', MovieSchema);