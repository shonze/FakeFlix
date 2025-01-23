const CategoryService = require('../services/category');
const MovieService = require("../services/movies");
require('custom-env').env(process.env.NODE_ENV, './config');
const User = require('../modules/user');

const validateAndGetUser = async (req) => {
    if (!req.headers.Authorization) {
        throw new Error('User not logged in');
    }

    const token = req.headers.Authorization.split(" ")[1];
    let data;

    try {
        data = jwt.verify(token, process.env.JWT_SECRET);
        console.log('The logged in user is: ' + data.username);
    } catch (err) {
        throw new Error('Invalid Token');
    }

    const existingUserByUsername = await User.findOne({ username: data.username });
    if (!existingUserByUsername) {
        throw new Error('User not found');
    }

    return existingUserByUsername;
};
/**
 * Creates a new category.
 */
const createCategory = async (req, res) => {
    try {
        
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        if (existingUserByUsername.isAdmin == false) { 
            return res.status(403).json({ errors: 'User is not an admin' });
        }
        if(!req.body.name) return res.status(400).json({ errors: ['Name is required'] });
        if(req.body.promoted == undefined) return res.status(400).json({ errors: ['Promoted is required'] });
        const newCategory = await CategoryService.createCategory(
            req.body.name,
            req.body.promoted,
            req.body.description,
            req.body.createdAt,
            req.body.movies
        );
        return res.status(201).json();
    } catch (error) {
        console.error('Error creating category:', error);
        res.status(500).json({ errors: ['Internal server error'] });
    }
};

/**
 * Retrieves all categories.
 */
const getCategories = async (req, res) => {
    try {
        const categories = await CategoryService.getCategories();
        res.json(categories);
    } catch (error) {
        console.error('Error retrieving categories:', error);
        return res.status(404).json({ errors: ['Categories not found'] });
    }
};

/**
 * Retrieves a single category by ID.
 */
const getCategory = async (req, res) => {
    try {
        const category = await CategoryService.getCategoryById(req.params.id);
        if (!category) {
            return res.status(404).json({ errors: ['Category not found'] });
        }

        return res.json(category);
    // In case the Category ID is not valid
    } catch (error) {
        console.error('Error retrieving category:', error);
        return res.status(404).json({ errors: ['Category not found'] });
    }
};

/**
 * Deletes a category by ID.
 */
const deleteCategory = async (req, res) => {
    try {
        
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        if (existingUserByUsername.isAdmin == false) { 
            return res.status(403).json({ errors: 'User is not an admin' });
        }

        const category = await CategoryService.deleteCategory(req.params.id);
        if (!category) {
            return res.status(404).json({ errors: ['Category not found'] });
        }

        return res.status(204).json();
    // In case the Category ID is not valid
    } catch (error) {
        console.error('Error deleting category:', error);
        return res.status(404).json({ errors: ['Category not found'] });
    }
};

/**
 * Updates a category by ID.
 */
const updateCategory = async (req, res) => {
    try {
        
        let existingUserByUsername;

        // Use the new helper function to validate the token and retrieve the user
        try {
            existingUserByUsername = await validateAndGetUser(req);
        } catch (error) {
            return res.status(403).json({ errors: error.message });
        }
        if (existingUserByUsername.isAdmin == false) { 
            return res.status(403).json({ errors: 'User is not an admin' });
        }
        
        // If all the update details are missing then its a bad request
        if(!(req.body.name ||
            req.body.promoted ||
            req.body.description ||
            req.body.createdAt ||
            req.body.movies))
            return res.status(400).json({ errors: ['Update details required'] });

        const category = await CategoryService.updateCategory(req.params.id,
             req.body.name,
             req.body.promoted,
             req.body.description,
             req.body.createdAt,
             req.body.movies);
        if (!category) {
            return res.status(404).json({ errors: ['Category not found'] });
        }

        return res.status(204).json();
    // In case the Category ID is not valid
    } catch (error) {
        console.error('Error updating category:', error);
        return res.status(404).json({ errors: ['Category not found'] });
    }
};


module.exports = { createCategory, getCategories, getCategory, updateCategory, deleteCategory };
