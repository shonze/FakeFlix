const path = require("path");

const uploadFile = (req, res) => {
    try {
        if (!req.file && !req.files) {
            return res.status(400).json({ message: "No file uploaded" });
        }

        const filesInfo = req.files
            ? req.files.map(file => ({
                filename: file.filename,
                path: file.path,
            }))
            : { filename: req.file.filename, path: req.file.path };

        res.status(200).json({
            message: "Files uploaded successfully",
            files: filesInfo,
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Error uploading file", error });
    }
};

module.exports = { uploadFile };
