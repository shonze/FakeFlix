const express = require("express");
const multer = require("multer");
const path = require("path");
const { uploadFile } = require("../controllers/file");
const FileController = require('../controllers/file');

const router = express.Router();

// Storage configuration
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, path.join(__dirname, "../public/uploads")); // Upload folder
    },
    filename: (req, file, cb) => {
        const uniqueSuffix = Date.now() + "-" + Math.round(Math.random() * 1e9);
        cb(null, `${uniqueSuffix}-${file.originalname}`);
    },
});

// File filter to allow only images and videos
const fileFilter = (req, file, cb) => {
    const allowedTypes = ["image/jpeg","image/jpg", "image/png", "image/gif", "video/mp4", "video/mkv"];
    console.log(file);
    if (allowedTypes.includes(file.mimetype)) {
        cb(null, true);
    } else {
        cb(new Error("Invalid file type"), false);
    }
};

// Multer middleware
const upload = multer({
    storage,
    fileFilter,
    limits: { fileSize: 50 * 1024 * 1024 }, // 50MB file size limit
});

// Single or multiple file uploads
router.post("/", upload.array("files", 10), uploadFile); // Up to 10 files

router.route('/:fileName')
    .delete(FileController.deleteFile);

module.exports = router;
