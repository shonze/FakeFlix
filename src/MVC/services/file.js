const fs = require("fs");
const path = require("path");

// Delete a file from the server
const deleteFileService = (filePath) => {
    return new Promise((resolve, reject) => {
        // const filePath = path.join(__dirname, "../public/uploads", fileName);
        fs.unlink(filePath, (err) => {
            if (err) {
                reject(new Error(`Failed to delete file: ${err.message}`));
            } else {
                resolve(`File ${filePath} deleted successfully`);
            }
        });
    });
};

module.exports = { deleteFileService };
