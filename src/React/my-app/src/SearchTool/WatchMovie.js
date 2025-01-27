import React, { useEffect, useState } from 'react';
import { useLocation,useNavigate } from 'react-router-dom';
import './WatchMovie.css';

const WatchMovie = () => {
    const location = useLocation();
    const selectedMovie = location.state?.movie;
    const [isPlaying, setIsPlaying] = useState(false);
    const [categories, setCategories] = useState([]); // State to hold category names
    const navigate = useNavigate();

    // Checks if the user is permited to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            const token = localStorage.getItem('jwtToken');

            const response = await fetch('http://localhost:8080/api/tokens/validate', {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                    'requiredAdmin': false
                }
            });
            if (!response.ok) {
                navigate('/404');
            }
        };

        checkValidation();
    }, []);

    // Function to bring categories based on selected movie
    const bringCategories = async () => {
        if (!selectedMovie) return; // Early exit if no movie is selected
        try {
            // Assuming selectedMovie.categories is an array of category IDs
            const categoryPromises = selectedMovie.categories.map(async (categoryID) => {
                const token = localStorage.getItem('jwtToken');
                const response = await fetch(`http://localhost:8080/api/categories/${categoryID}`, {
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });
                if (!response.ok) throw new Error('Failed to bring categories');
                const data = await response.json();
                return data.name; // Assuming the category object has a 'name' field
            });
            const categoryNames = await Promise.all(categoryPromises);
            setCategories(categoryNames);
        } catch (error) {
            console.error('Error fetching categories:', error);
            setCategories([]);
        }
    };

    // Use useEffect to fetch categories when the component mounts or when selectedMovie changes
    useEffect(() => {
        bringCategories(); // Always call this function, it handles the condition
    }, [selectedMovie]);

    const handlePlayClick = () => {
        setIsPlaying(true); // Show the video player
    };

    if (!selectedMovie) {
        return <p>No movie selected.</p>; // Handle the case where no movie is selected
    }

    return (
        <div className="movie-container">
            <img
                src={selectedMovie.thumbnail}
                alt={`${selectedMovie.title} Background`}
                className="movie-background"
            />
            <div className="overlay">
                <div className="movie-info">
                    <h1 className="movie-title">{selectedMovie.title}</h1>
                    <p className="movie-description">
                        {selectedMovie.description}
                        <p>    </p>
                        {selectedMovie.length} minutes
                    </p>
                    <button className="play-button" onClick={handlePlayClick}>Play</button>
                    <div className="category-container">
                        {categories.map((category, index) => (
                            <span key={index} className="category-badge">{category}</span>
                        ))}
                    </div>
                    {isPlaying && (
                        <div className="video-container">
                            <video key={selectedMovie._id} controls className="video-player">
                                <source src={selectedMovie.video} type="video/mp4" />
                                Your browser does not support the video tag.
                            </video>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default WatchMovie;
