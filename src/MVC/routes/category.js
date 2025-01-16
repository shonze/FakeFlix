const express = require('express');
var router = express.Router();
const CategoryController = require('../controllers/category');

/**
 * Route to get all categories or create a new category.
 */
router.route('/')
    .get(CategoryController.getCategories)
    .post(CategoryController.createCategory);

/**
 * Route to get, update, or delete a category by ID.
 */
router.route('/:id')
    .get(CategoryController.getCategory)
    .patch(CategoryController.updateCategory)
    .delete(CategoryController.deleteCategory);

module.exports = router;
