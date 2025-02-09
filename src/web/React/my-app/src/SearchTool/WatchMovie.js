import React, { useEffect, useState } from 'react';
import { useLocation , useNavigate} from 'react-router-dom';
import './WatchMovie.css';
import Movieslst from '../Movieslst/Movieslst'
import PleaseConnect from '../Pages/PleaseConnect';



const WatchMovie = () => {
    const [recommendedMovies, setRecommendedMovies] = useState([]);
    const location = useLocation();
    const selectedMovie = location.state?.movie;
    const [isPlaying, setIsPlaying] = useState(false);
    const [categories, setCategories] = useState([]); // State to hold category names
    const [recommendation, setRecommendation] = useState([]);
    const navigate = useNavigate();
    const [isLogged, setIsLogged] = useState(null);
    const [movielst, setmovielst] = useState([]);

    const handleBackClick = () => {
        navigate('/home'); 
    };

        // Checks if the user is permitted to enter the screen
    const checkValidation = async () => {
        try {
            const token = localStorage.getItem('jwtToken');

            const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/tokens/validate`, {
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

    const getRecommendation = async () => {
        if (!selectedMovie || !selectedMovie._id) {
            console.error("No selected movie found for recommendations.");
            return;
        }
    
        try {
            const token = localStorage.getItem('jwtToken');
    
            console.log(`Fetching recommendations from: http://localhost:${process.env.REACT_APP_PORT}/api/movies/${selectedMovie._id}/recommend`);
    
            const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/movies/${selectedMovie._id}/recommend`, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                }
            });
    
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
    
            const data = await response.json();
            console.log("Recommendation data received:", data);
    
            // Extract only the movie IDs from the received movie objects
            const movieIds = data.map(movie => movie._id);
    
            setmovielst(movieIds); // Store only the IDs in movielst
    
        } catch (error) {
            console.error("Error fetching recommendations:", error);
        }
    };
    
    

    useEffect(() => {
        checkValidation();
    }, []);

    useEffect(() => {
        if (selectedMovie && !isPlaying) { // Only fetch recommendations when not playing
            getRecommendation();
        }
    }, [selectedMovie, isPlaying]);


        
    const notifyWatchMovie = async () => {
        try {
            const token = localStorage.getItem('jwtToken');
            const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/movies/${selectedMovie._id}/recommend`, {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                }
            });
            if (!response.ok) {
                setIsLogged(false);
                return;
            }
        }
        catch (error) {
            console.log(error);
        };
    };

    // Function to bring categories based on selected movie
    const bringCategories = async () => {
        if (!selectedMovie) return; // Early exit if no movie is selected
        try {
            // Assuming selectedMovie.categories is an array of category IDs
            const categoryPromises = selectedMovie.categories.map(async (categoryID) => {
                const token = localStorage.getItem('jwtToken');
                const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/categories/${categoryID}`, {
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
        setmovielst([]);
        notifyWatchMovie();
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
                    { movielst.length > 0 && (
                        <div>
                            <div className = 'recommended-title'> Recommended:</div>
                            <div className='watchMovie-recommendation'>
                                <Movieslst Movieslst={movielst} />
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </div>
        ) : (
            <PleaseConnect />
        )
    );
};

export default WatchMovie;
