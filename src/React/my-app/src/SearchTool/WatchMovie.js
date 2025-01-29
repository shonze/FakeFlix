import React, { useEffect, useState } from 'react';
import { useLocation , useNavigate} from 'react-router-dom';
import './WatchMovie.css';
import PleaseConnect from '../Pages/PleaseConnect';



const WatchMovie = () => {

    const location = useLocation();
    const selectedMovie = location.state?.movie;
    const [isPlaying, setIsPlaying] = useState(false);
    const [categories, setCategories] = useState([]); // State to hold category names
    const navigate = useNavigate();
    const [isLogged, setIsLogged] = useState(null);

    const handleBackClick = () => {
        navigate('/home'); 
    };

    // Checks if the user is permitted to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch('http://localhost:8080/api/tokens/validate', {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json',
                        'Requireadmin': false,
                    }
                });
                if (!response.ok) {
                    setIsLogged(false)
                    return;
                }
                // const isAdmin = await response.json();
                // setIsAdmin(isAdmin.isAdmin);
                setIsLogged(true)
                
            } catch (error) {
                setIsLogged(false)
            };
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

    if (isLogged === null) {
        // Render nothing or a loading spinner while the validation is in progress
        return <div>Loading...</div>;
    }
    return (
        isLogged ? (

            <div className="watchMovie">
                <button className="watchMovie-back-button" onClick={handleBackClick}>
                    &#8592; Back
                </button>
            <div className="watchMovie-movie-container">
                <img
                    src={selectedMovie.thumbnail}
                    alt={`${selectedMovie.title} Background`}
                    className="watchMovie-movie-background"
                />
                <div className="watchMovie-overlay">
                    <div className="watchMovie-movie-info">
                        <h1 className="watchMovie-movie-title">{selectedMovie.title}</h1>
                        <p className="watchMovie-movie-description">
                            {selectedMovie.description}
                            <p>    </p>
                            {selectedMovie.length} minutes
                        </p>
                        <button className="watchMovie-play-button" onClick={handlePlayClick}>Play</button>
                        <div className="watchMovie-category-container">
                            {categories.map((category, index) => (
                                <span key={index} className="watchMovie-category-badge">{category}</span>
                            ))}
                        </div>
                        {isPlaying && (
                            <div className="watchMovie-video-container">
                                <video key={selectedMovie._id} controls className="watchMovie-video-player">
                                    <source src={selectedMovie.video} type="video/mp4" />
                                    Your browser does not support the video tag.
                                </video>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
        ) : (
            <PleaseConnect />
        )
    );
};

export default WatchMovie;
