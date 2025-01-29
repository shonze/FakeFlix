import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './SearchTool.css';
import TopMenu from '../TopMenu/TopMenu';
import PleaseConnect from '../Pages/PleaseConnect';

const SearchScreen = () => {
    const [movieQuery, setMovieQuery] = useState('');
    const [movies, setMovies] = useState([]);
    const [isAdmin, setIsAdmin] = useState(false);
    const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
    const navigate = useNavigate();
    const [isLogged, setIsLogged] = useState(null);

    // Check if the user is permitted to enter the screen
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
                const isAdmin = await response.json();
                setIsAdmin(isAdmin.isAdmin);
                setIsLogged(true)
                
            } catch (error) {
                setIsLogged(false)
            };
        };

        checkValidation();
    }, [navigate]);

    const handleMovieSearch = async () => {
        if (!movieQuery.trim()) {
            setMovies([]);
            return;
        }

        try {
            const token = localStorage.getItem('jwtToken');
            const response = await fetch(`http://localhost:8080/api/movies/search/${movieQuery}`, {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) throw new Error('Failed to fetch movies');
            const data = await response.json();
            setMovies(data);
        } catch (error) {
            console.error('Error fetching movies:', error);
            setMovies([]);
        }
    };

    const handleMovieClick = (movie) => {
        navigate('/watch-movie', { state: { movie } });
    };

    // Trigger search on movieQuery changes
    useEffect(() => {
        const delayDebounce = setTimeout(() => {
            handleMovieSearch();
        }, 100); // Debounce by 100ms

        return () => clearTimeout(delayDebounce); // Cleanup debounce timer
    }, [movieQuery]);

    useEffect(() => {
        const handleStorageChange = (event) => {
            setTheme(localStorage.getItem("theme"));
        };

        // Listen for changes to localStorage (triggered by other windows/tabs)
        window.addEventListener("storage", handleStorageChange);
    }, []);

    if (isLogged === null) {
        // Render nothing or a loading spinner while the validation is in progress
        return <div>Loading...</div>;
    }
    return (
        isLogged ? (

            <div className={`search-container bg-${theme}`}>
            <TopMenu admin={isAdmin} />
            <div className="search-sections">
                <div className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"} bg-transparent border-0`}> 
                    <h2>Search Movies</h2>
                </div>
                <section className="search">
                    <input
                        type="text"
                        placeholder="What would you like to see today?"
                        value={movieQuery}
                        onChange={(e) => setMovieQuery(e.target.value)}
                    />
                    {movies.length > 0 ? (
                        <div className="results">
                            <div className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"} bg-transparent border-0`}>
                                <h4>Movies</h4>
                            </div>
                            <ul>
                                {movies.map((movie) => (
                                    <li key={movie._id} onClick={() => handleMovieClick(movie)}>
                                        <div className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"} bg-transparent border-0`}>
                                            <h6>{movie.title}</h6>  
                                        </div>
                                        <img src={movie.thumbnail} alt={movie.title} />
                                    </li>
                                ))}
                            </ul>
                        </div>
                    ) : movieQuery.length > 0 ? (
                        <div className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"} bg-transparent border-0`}>
                            <h2 className="no-results">No Results Found</h2>
                        </div>
                    ) : null}
                </section>
            </div>
        </div>
        ) : (
            <PleaseConnect />
        )
    );
};

export default SearchScreen;