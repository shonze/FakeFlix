const express = require('express');
const videoController = require('../controllers/video');
const videoService = require('../services/video');

const router = express.Router();

// Route for uploading videos
router.post('/', videoService.upload.single('video'), videoController.uploadVideo);

router.route('/:id')
    .get(videoController.getVideoById)

module.exports = router;