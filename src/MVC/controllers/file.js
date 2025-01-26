const path = require("path");

const uploadFile = (req, res) => {
    try {
        if (!req.file && !req.files) {
            return res.status(400).json({ message: "No file uploaded" });
        }

        const baseUrl = `${req.protocol}://${req.get("host")}`;
        const filesInfo = req.files
            ? req.files.map(file => ({
                filename: file.filename,
                url: `${baseUrl}/uploads/${file.filename}`,
                path: file.path,
            }))
            : { filename: req.file.filename, url: `${baseUrl}/uploads/${req.file.filename}`,path: file.path, };

        res.status(200).json({
            message: "Files uploaded successfully",
            files: filesInfo,
        });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Error uploading file", error });
    }
};

const { deleteFileService } = require("../services/file");

// Controller to delete a file
const deleteFile = async (req, res) => {
    const filePath = "/" + req.params[0];

    console.log("filePath: ")
    console.log(req.params.filePath)
    console.log("params: ")
    console.log(req.params[0])
    try {
        const result = await deleteFileService(filePath);
        res.status(200).json({ message: result });
    } catch (error) {
        res.status(500).json({ error: "Failed to delete the file", details: error.message });
    }
};


module.exports = { uploadFile,deleteFile };