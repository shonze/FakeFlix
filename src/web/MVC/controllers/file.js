const path = require("path");

const uploadFile = (req, res) => {
    try {
        if (!req.file && !req.files) {
            return res.status(400).json({ message: "No file uploaded" });
        }

        // const baseUrl = `${req.protocol}://${req.get("host")}`;
        // const filesInfo = req.files
        //     ? req.files.map(file => ({
        //         filename: file.filename,
        //         url: `${baseUrl}/uploads/${file.filename}`,
        //         name: file.filename,
        //     }))
        //     : { filename: req.file.filename, url: `${baseUrl}/uploads/${req.file.filename}`,name: file.filename, };

        // res.status(200).json({
        //     message: "Files uploaded successfully",
        //     files: filesInfo,
        // });

        const baseUrl = `${req.protocol}://${req.get("host")}`;
        const filesInfo = req.files
            ? req.files.map(file => ({
                url: `${baseUrl}/uploads/${file.filename}`,
                name: file.filename,
            }))
            : [{
                url: `${baseUrl}/uploads/${req.file.filename}`,
                name: req.file.filename,
            }];

        res.status(200).json({
            message: "Files uploaded successfully",
            ...filesInfo[0], // For single file, directly spread the first object
        });

    } catch (error) {
        console.error(error);
        res.status(500).json({ message: "Error uploading file", error });
    }
};

// const uploadFile = (req, res) => {
//     try {
//         if (!req.file && !req.files) {
//             return res.status(400).json({ message: "No file uploaded" });
//         }

//         const baseUrl = `${req.protocol}://${req.get("host")}`;

//         const sanitizeFilename = (filename) => {
//             return filename.replace(/\s+/g, "_"); // Replace spaces with underscores
//         };

//         const filesInfo = req.files
//             ? req.files.map((file) => {
//                 const sanitizedFilename = sanitizeFilename(file.filename);
//                 return {
//                     filename: sanitizedFilename,
//                     url: `${baseUrl}/uploads/${sanitizedFilename}`,
//                     name: sanitizedFilename,
//                 };
//             })
//             : (() => {
//                 const sanitizedFilename = sanitizeFilename(req.file.filename);
//                 return {
//                     filename: sanitizedFilename,
//                     url: `${baseUrl}/uploads/${sanitizedFilename}`,
//                     name: sanitizedFilename,
//                 };
//             })();

//         res.status(200).json({
//             message: "Files uploaded successfully",
//             files: filesInfo,
//         });
//     } catch (error) {
//         console.error(error);
//         res.status(500).json({ message: "Error uploading file", error });
//     }
// };


const { deleteFileService } = require("../services/file");

// Controller to delete a file
const deleteFile = async (req, res) => {
    const filename = req.params.fileName
    
    console.log (filename)
    console.log("params:")
    console.log(req.params)
    try {
        const result = await deleteFileService(filename);
        res.status(200).json({ message: result });
    } catch (error) {
        res.status(500).json({ error: "Failed to delete the file", details: error.message });
    }
};


module.exports = { uploadFile,deleteFile };