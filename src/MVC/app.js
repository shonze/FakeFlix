const express = require("express");
const bodyParser = require('body-parser');
const cors = require('cors');
const mongoose = require('mongoose');

const movies = require('./routes/movies');
const category = require('./routes/category');
const user = require('./routes/user');
const tokens = require('./routes/tokens');
const video = require('./routes/video');

require('custom-env').env(process.env.NODE_ENV, './config');

mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true
}).then(() => {
    console.log("Connected to MongoDB");
}).catch(err => {
    console.error("MongoDB connection error:", err);
});

const app = express();
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.use("/api/movies", movies);
app.use('/api/categories', category);
app.use('/api/users', user);
app.use('/api/tokens', tokens);

app.use('/api/video', video);

const PORT = process.env.PORT;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});