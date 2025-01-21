import React, { useEffect, useState } from 'react';
import './AdminScreen.css';

const AdminScreen = () => {
    const [movies, setMovies] = useState([]);
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

    // Fetch movies from the server
    const fetchMovies = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/movies');
            if (!response.ok) throw new Error('Network response was not ok');
            const data = await response.json();
            setMovies(data);
        } catch (error) {
            console.error('Error fetching movies:', error);
        }
    };

    // Fetch categories from the server
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
        fetchMovies();
        fetchCategories();
    }, []);

    // Handle adding a new movie
    const handleAddMovie = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/movies', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newMovie),
            });
            if (!response.ok) throw new Error('Error adding movie');
            await fetchMovies(); // Refresh movie list
            setNewMovie({
                title: '',
                categories: [],
                description: '',
                length: '',
                thumbnail: '',
                video: '',
            }); // Reset form
        } catch (error) {
            console.error('Error adding movie:', error);
        }
    };

    // Handle searching for a movie by ID
    const handleSearchMovie = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/movies/${searchId}`);
            if (!response.ok) throw new Error('Movie not found');
            const data = await response.json();
            setFoundMovie(data);
        } catch (error) {
            console.error('Error searching for movie:', error);
            setFoundMovie(null); // Clear found movie on error
        }
    };

    // Handle deleting a movie
    const handleDeleteMovie = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/movies/${id}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error('Error deleting movie');
            await fetchMovies(); // Refresh movie list
            setFoundMovie(null); // Clear found movie
        } catch (error) {
            console.error('Error deleting movie:', error);
        }
    };

    // Handle updating a movie
    const handleUpdateMovie = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/movies/${foundMovie._id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(foundMovie),
            });
            if (!response.ok) throw new Error('Error updating movie');
            await fetchMovies(); // Refresh movie list
            setFoundMovie(null); // Clear found movie
        } catch (error) {
            console.error('Error updating movie:', error);
        }
    };

    // Handle adding a new category
    const handleAddCategory = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/categories', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newCategory),
            });
            if (!response.ok) throw new Error('Error adding category');
            await fetchCategories(); // Refresh category list
            setNewCategory({ name: '', promoted: false, description: '' }); // Reset form
        } catch (error) {
            console.error('Error adding category:', error);
        }
    };

    // Handle deleting a category
    const handleDeleteCategory = async (id) => {
        try {
            const response = await fetch(`http://localhost:8080/api/categories/${id}`, {
                method: 'DELETE',
            });
            if (!response.ok) throw new Error('Error deleting category');
            await fetchCategories(); // Refresh category list
        } catch (error) {
            console.error('Error deleting category:', error);
        }
    };

    // Handle updating a category
    const handleUpdateCategory = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/categories/${editingCategory._id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(editingCategory),
            });
            if (!response.ok) throw new Error('Error updating category');
            await fetchCategories(); // Refresh category list
            setEditingCategory(null); // Reset editing
            setNewCategory({ name: '', promoted: false, description: '' }); // Reset form
        } catch (error) {
            console.error('Error updating category:', error);
        }
    };

    return (
        <div className="container">
            <h1>Admin Screen</h1>


            {/* Add Category Form */}
            <form onSubmit={editingCategory ? handleUpdateCategory : handleAddCategory} className="add-category">
                <h2>{editingCategory ? 'Update Category' : 'Add Category'}</h2>
                <input
                    type="text"
                    placeholder="Category Name"
                    value={editingCategory ? editingCategory.name : newCategory.name}
                    onChange={(e) => {
                        if (editingCategory) {
                            setEditingCategory({ ...editingCategory, name: e.target.value });
                        } else {
                            setNewCategory({ ...newCategory, name: e.target.value });
                        }
                    }}
                    required
                />
                <input
                    type="text"
                    placeholder="Description"
                    value={editingCategory ? editingCategory.description : newCategory.description}
                    onChange={(e) => {
                        if (editingCategory) {
                            setEditingCategory({ ...editingCategory, description: e.target.value });
                        } else {
                            setNewCategory({ ...newCategory, description: e.target.value });
                        }
                    }}
                />
                <label>
                    Promoted:
                    <input
                        type="checkbox"
                        checked={editingCategory ? editingCategory.promoted : newCategory.promoted}
                        onChange={(e) => {
                            if (editingCategory) {
                                setEditingCategory({ ...editingCategory, promoted: e.target.checked });
                            } else {
                                setNewCategory({ ...newCategory, promoted: e.target.checked });
                            }
                        }}
                    />
                    <h4></h4>
                </label>
                <button type="submit">{editingCategory ? 'Update Category' : 'Add Category'}</button>
            </form>

            {/* Category List */}
            <h2>Categories</h2>
            <ul>
                {categories.map((category) => (
                    <li key={category._id}>
                        {category.name} <h20></h20> 
                        <button onClick={() => {
                            setEditingCategory(category);
                            setNewCategory({ name: category.name, promoted: category.promoted, description: category.description });
                        }}>Edit</button> <h20></h20> 
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
                <select
                    multiple
                    value={newMovie.categories}
                    onChange={(e) => setNewMovie({ ...newMovie, categories: [...e.target.selectedOptions].map(option => option.value) })}
                >
                    {categories.map((category) => (
                        <option key={category._id} value={category.name}>{category.name}</option>
                    ))}
                </select>
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
                    <h3>{foundMovie.title}</h3>
                    <p>Description: {foundMovie.description}</p>
                    <p>Length: {foundMovie.length} minutes</p>
                    <p>Categories: {foundMovie.categories.join(', ')}</p>
                    <button className="delete-button" onClick={() => handleDeleteMovie(foundMovie._id)}>Delete Movie</button>
                    <form onSubmit={handleUpdateMovie} className="update-movie">
                        <h2>Update Movie</h2>
                        <input
                            type="text"
                            placeholder="Title"
                            value={foundMovie.title}
                            onChange={(e) => setFoundMovie({ ...foundMovie, title: e.target.value })}
                            required
                        />
                        <select
                            multiple
                            value={foundMovie.categories}
                            onChange={(e) => setFoundMovie({ ...foundMovie, categories: [...e.target.selectedOptions].map(option => option.value) })}
                        >
                            {categories.map((category) => (
                                <option key={category._id} value={category.name}>{category.name}</option>
                            ))}
                        </select>
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
