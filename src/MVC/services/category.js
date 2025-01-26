const Category = require('../modules/category');
const MoviesModel = require('../modules/movies'); 
const moongoose = require('mongoose');

/**
 * Creates a new movie category.
 */
const createCategory = async (name, promoted, description, createdAt, movies) => {
    const category = new Category({ name: name, promoted: promoted });
    if (description) category.description = description;
    if (createdAt) category.createdAt = createdAt;
    if (movies) category.movies = movies;
    return await category.save();
};

/**
 * Retrieves a category by its ID.
 */
const getCategoryById = async (id) => {
    if (!moongoose.Types.ObjectId.isValid(id)) return null;
    return await Category.findById(id);
};

/**
 * Retrieves all categories.
 */
const getCategories = async () => { return await Category.find({}); };

/**
 * Updates the title of a category by its ID.
 */
const updateCategory = async (id, name, promoted, description, createdAt, movies) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    if (name) category.name = name
    if (!(promoted == undefined)) category.promoted = promoted;
    if (description) category.description = description;
    if (createdAt) category.createdAt = createdAt;
    if (movies) category.movies = movies;

    await category.save();
    return category;
};

/**
 * Deletes a category by its ID.
 */
const deleteCategory = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;

    for (const movieId of category.movies) {
        const movie = await MoviesModel.findById(movieId);
        if (movie) {
            movie.categories = movie.categories.filter(cat => cat != id);
            await movie.save(); 
        }
    }

    await Category.deleteOne(category);
    return category;
};

const isPromoted = async (id) => {
    const category = await getCategoryById(id);
    if (!category) return null;
    return category.promoted;
}
module.exports = { createCategory, getCategoryById, getCategories, updateCategory, deleteCategory, isPromoted };
