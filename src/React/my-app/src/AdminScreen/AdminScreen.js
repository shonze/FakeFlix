import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminScreen.css';

const AdminScreen = () => {
  const [categories, setCategories] = useState([]);
  const [newMovie, setNewMovie] = useState({
      title: '',
      categories: [],
      description: '',
      length: '',
      thumbnail: '',
      video: '',
  });
  const [newCategory, setNewCategory] = useState({
      name: '',
      promoted: false,
      description: '',
  });
  const [editingCategory, setEditingCategory] = useState(null);
  const [searchId, setSearchId] = useState('');
  const [foundMovie, setFoundMovie] = useState(null);

  const [successMessage, setSuccessMessage] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const [selectedCategories, setSelectedCategories] = useState([]); // Selected categories

  const navigate = useNavigate();


  const handleBackClick = () => {
    navigate('/home'); // Replace '/home' with the route for your HomePage
  };
  const clearMessages = () => {
      setSuccessMessage('');
      setErrorMessage('');
  };

  const fetchCategories = async () => {
      try {
          const response = await fetch('http://localhost:8080/api/categories');
          if (!response.ok) throw new Error('Network response was not ok');
          const data = await response.json();
          setCategories(data);
      } catch (error) {
          console.error('Error fetching categories:', error);
      }
  };

  useEffect(() => {
      fetchCategories();
  }, []);

  const handleAddMovie = async (e) => {
    e.preventDefault();
    clearMessages();
    try {
        const movieToAdd = {
            ...newMovie,
            categories: selectedCategories, // Add this line to set categories
        };
        const response = await fetch('http://localhost:8080/api/movies', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(movieToAdd),
        });
        if (!response.ok) throw new Error('Error adding movie');
        setSuccessMessage('Movie added successfully!');
        setNewMovie({
            title: '',
            categories: [],
            description: '',
            length: '',
            thumbnail: '',
            video: '',
        });
        setSelectedCategories([]); // Reset selected categories
    } catch (error) {
        setErrorMessage('Failed to add movie.');
        console.error(error);
    }
  };

  const handleCategoryChange = (category) => {
    setSelectedCategories(prevCategories => {
      // If the category is already selected, remove it
      if (prevCategories.includes(category)) {
        return prevCategories.filter(cat => cat !== category);
      } 
      // Otherwise, add the category
      return [...prevCategories, category];
    });
  };

  const handleSearchMovie = async (e) => {
    e.preventDefault();
    clearMessages();
    try {
      const response = await fetch(`http://localhost:8080/api/movies/${searchId}`);
      if (!response.ok) throw new Error('Movie not found');
      const data = await response.json();
      // Ensure categories is an array, defaulting to empty if null
      setFoundMovie({
        ...data,
      });
    } catch (error) {
      setErrorMessage('Movie not found.');
      setFoundMovie(null);
    }
  };

  const handleDeleteMovie = async (id) => {
      clearMessages();
      try {
          const response = await fetch(`http://localhost:8080/api/movies/${id}`, {
              method: 'DELETE',
          });
          if (!response.ok) throw new Error('Error deleting movie');
          setSuccessMessage('Movie deleted successfully!');
          setFoundMovie(null);
      } catch (error) {
          setErrorMessage('Failed to delete movie.');
          console.error(error);
      }
  };

  const handleUpdateMovie = async (e) => {
      e.preventDefault();
      clearMessages();
      try {
          const response = await fetch(`http://localhost:8080/api/movies/${foundMovie._id}`, {
              method: 'PUT',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(foundMovie),
          });
          if (!response.ok) throw new Error('Error updating movie');
          setSuccessMessage('Movie updated successfully!');
          setFoundMovie(null);
      } catch (error) {
          setErrorMessage('Failed to update movie.');
          console.error(error);
      }
  };

  const handleAddCategory = async (e) => {
      e.preventDefault();
      clearMessages();
      try {
          const response = await fetch('http://localhost:8080/api/categories', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(newCategory),
          });
          if (!response.ok) throw new Error('Error adding category');
          setSuccessMessage('Category added successfully!');
          setNewCategory({ name: '', promoted: false, description: '' });
          fetchCategories();
      } catch (error) {
          setErrorMessage('Failed to add category.');
          console.error(error);
      }
  };

  const handleDeleteCategory = async (id) => {
      clearMessages();
      try {
          const response = await fetch(`http://localhost:8080/api/categories/${id}`, {
              method: 'DELETE',
          });
          if (!response.ok) throw new Error('Error deleting category');
          setSuccessMessage('Category deleted successfully!');
          fetchCategories();
      } catch (error) {
          setErrorMessage('Failed to delete category.');
          console.error(error);
      }
  };

  const handleUpdateCategory = async (e) => {
      e.preventDefault();
      clearMessages();
      try {
          const response = await fetch(`http://localhost:8080/api/categories/${editingCategory._id}`, {
              method: 'PATCH',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(editingCategory),
          });
          if (!response.ok) throw new Error('Error updating category');
          setSuccessMessage('Category updated successfully!');
          setEditingCategory(null);
          setNewCategory({ name: '', promoted: false, description: '' });
          fetchCategories();
      } catch (error) {
          setErrorMessage('Failed to update category.');
          console.error(error);
      }
  };

    return (
        <div className="container">
          <button className="back-button" onClick={handleBackClick}>
            &#8592; Back
          </button>
          <h1>Admin Screen</h1>

          {/* Add Category Form */}
          <form onSubmit={handleAddCategory} className="add-category">
            <h2>Add Category</h2>
            <input
              type="text"
              placeholder="Category Name"
              value={newCategory.name}
              onChange={(e) => setNewCategory({ ...newCategory, name: e.target.value })}
              required
            />
            <input
              type="text"
              placeholder="Description"
              value={newCategory.description}
              onChange={(e) => setNewCategory({ ...newCategory, description: e.target.value })}
            />
            <label>
              Promoted:
              <input
                type="checkbox"
                checked={newCategory.promoted}
                onChange={(e) => setNewCategory({ ...newCategory, promoted: e.target.checked })}
              />
            </label>
            <button type="submit">Add Category</button>
          </form>

          {/* Separate Editing Form (when a category is being edited) */}
          {editingCategory && (
            <form onSubmit={handleUpdateCategory} className="edit-category">
              <h2>Edit Category</h2>
              <input
                type="text"
                placeholder="Category Name"
                value={editingCategory.name}
                onChange={(e) => setEditingCategory({ ...editingCategory, name: e.target.value })}
                required
              />
              <input
                type="text"
                placeholder="Description"
                value={editingCategory.description}
                onChange={(e) => setEditingCategory({ ...editingCategory, description: e.target.value })}
              />
              <label>
                Promoted:
                <input
                  type="checkbox"
                  checked={editingCategory.promoted}
                  onChange={(e) => setEditingCategory({ ...editingCategory, promoted: e.target.checked })}
                />
              </label>
              <button type="submit">Update Category</button>
              <button type="button" onClick={() => setEditingCategory(null)}>Cancel</button>
            </form>
            )}
            {/* Success Message */}
            {successMessage && <div className="success-message">{successMessage}</div>}

            {/* Category List */}
            <h2>Categories</h2>
            <ul>
                {categories.map((category) => (
                    <li key={category._id}>
                        {category.name} <h6></h6> 
                        <button onClick={() => {
                            setEditingCategory(category);
                        }}>Edit</button> <p>  </p>
                        <button className="delete-button" onClick={() => handleDeleteCategory(category._id)}>Delete</button>
                        <h6></h6> 
                    </li>
                ))}
            </ul>

            {/* Add Movie Form */}
            <form onSubmit={handleAddMovie} className="add-movie">
                <h2>Add Movie</h2>
                <input
                    type="text"
                    placeholder="Title"
                    value={newMovie.title}
                    onChange={(e) => setNewMovie({ ...newMovie, title: e.target.value })}
                    required
                />
                <h3>Select Categories:</h3>
                <div className="category-checkbox-list">
                  {categories.map((category) => (
                    <label key={category._id} className="checkbox-item">
                      <input
                        type="checkbox"
                        value={category.name}
                        checked={selectedCategories.includes(category.name)}
                        onChange={() => handleCategoryChange(category.name)}
                      />
                      {category.name}
                    </label>
                  ))}
                </div>
                <input
                    type="text"
                    placeholder="Description"
                    value={newMovie.description}
                    onChange={(e) => setNewMovie({ ...newMovie, description: e.target.value })}
                    required
                />
                <input
                    type="number"
                    placeholder="Length (in minutes)"
                    value={newMovie.length}
                    onChange={(e) => setNewMovie({ ...newMovie, length: e.target.value })}
                    required
                />
                <input
                    type="text"
                    placeholder="Thumbnail URL"
                    value={newMovie.thumbnail}
                    onChange={(e) => setNewMovie({ ...newMovie, thumbnail: e.target.value })}
                />
                <input
                    type="text"
                    placeholder="Video URL"
                    value={newMovie.video}
                    onChange={(e) => setNewMovie({ ...newMovie, video: e.target.value })}
                    required
                />
                <button type="submit">Add Movie</button>
            </form>
            {/* Search Movie by ID */}
            <form onSubmit={handleSearchMovie} className="search-movie">
                <h2>Search Movie by ID</h2>
                <input
                    type="text"
                    placeholder="Enter Movie ID"
                    value={searchId}
                    onChange={(e) => setSearchId(e.target.value)}
                    required
                />
                <button type="submit">Search</button>
              </form>

            {/* Display Found Movie */}
            {foundMovie && (
              <div className="found-movie">
                <h2>Found Movie</h2>
                <form onSubmit={handleUpdateMovie} className="update-movie">
                  <h2>Update Movie</h2>
                  <input
                    type="text"
                    placeholder="Movie ID"
                    value={foundMovie._id}
                    readOnly
                  />
                  <input
                    type="text"
                    placeholder="Title"
                    value={foundMovie.title}
                    onChange={(e) => setFoundMovie({ ...foundMovie, title: e.target.value })}
                    required
                  />
                  
                  {/* Category Checkbox Section */}
                  <h3>Select Categories:</h3>
                  <div className="category-checkbox-list">
                    {categories && categories.length > 0 && categories.map((category) => (
                      <label key={category._id} className="checkbox-item">
                        <input
                          type="checkbox"
                          value={category.name}
                          checked={foundMovie.categories && foundMovie.categories.includes(category._id)}
                          onChange={() => {
                            const currentCategories = foundMovie.categories;
                            const updatedCategories = currentCategories.includes(category._id)
                              ? currentCategories.filter(cat => cat !== category._id)
                              : [...currentCategories, category._id];
                            setFoundMovie({ ...foundMovie, categories: updatedCategories });
                          }}
                        />
                        {category.name}
                      </label>
                    ))}
                  </div>

                  <input
                      type="text"
                      placeholder="Description"
                      value={foundMovie.description}
                      onChange={(e) => setFoundMovie({ ...foundMovie, description: e.target.value })}
                      required
                  />
                  <input
                      type="number"
                      placeholder="Length (in minutes)"
                      value={foundMovie.length}
                      onChange={(e) => setFoundMovie({ ...foundMovie, length: e.target.value })}
                      required
                  />
                  <input
                      type="text"
                      placeholder="Thumbnail URL"
                      value={foundMovie.thumbnail}
                      onChange={(e) => setFoundMovie({ ...foundMovie, thumbnail: e.target.value })}
                  />
                  <input
                      type="text"
                      placeholder="Video URL"
                      value={foundMovie.video}
                      onChange={(e) => setFoundMovie({ ...foundMovie, video: e.target.value })}
                      required
                  />
                  <button type="submit">Update Movie</button>
              </form>
            </div>
          )}
        </div>
    );
};

export default AdminScreen;
