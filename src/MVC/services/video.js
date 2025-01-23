const multer = require('multer');
const path = require('path');
const Video = require('../models/video');
const fs = require("fs").promises;

// Define the absolute path for storing videos
const videoDir = path.resolve(__dirname, '../../data/video');

const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, videoDir); // Save files in the "video" folder
    },
    filename: (req, file, cb) => {
        // Generate a unique filename to avoid collisions
        const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1e9);
        cb(null, `${file.fieldname}-${uniqueSuffix}${path.extname(file.originalname)}`);
    },
});

const upload = multer({ storage });

const createVideo = async (filePath) => {
    try {
        // Create a new video entry in the database
        const video = new Video({ filePath });
        await video.save();

        // Generate the new path with the video ID as the filename
        const newPath = path.join(videoDir, `${video.id}${path.extname(filePath)}`);

        // Rename the file to use the video ID
        await fs.rename(filePath, newPath);

        // Update the filePath in the database and save the video again
        video.filePath = newPath;
        return await video.save();
    } catch (err) {
        console.error("Error in createVideo:", err);
        throw new Error("Failed to create video.");
    }
};

const getVideoById = async (id) => {
    try {
        // Find the video with the given ID
        const video = await Video.findById(id);
        return video;
    } catch (err) {
        console.error("Error in getVideoById:", err);
        throw new Error("Video not found.");
    }
};

module.exports = {
    upload,
    createVideo,
    getVideoById,
};
