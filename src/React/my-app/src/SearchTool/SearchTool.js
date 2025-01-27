import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './SearchTool.css';

const SearchScreen = () => {
    const [movieQuery, setMovieQuery] = useState('');
    const [movies, setMovies] = useState([]);
    const navigate = useNavigate();

    // Check if the user is permitted to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            const token = localStorage.getItem('jwtToken');

            const response = await fetch('http://localhost:8080/api/tokens/validate', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                navigate('/404');
            }
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
        }, 300); // Debounce by 300ms

        return () => clearTimeout(delayDebounce); // Cleanup debounce timer
    }, [movieQuery]);

    return (
        <div className="search-container">
            <h1>FakeFlix Movies</h1>
            <div className="search-sections">
                <section className="movie-search">
                    <h2>Search Movies</h2>
                    <input
                        type="text"
                        placeholder="What would you like to see today?"
                        value={movieQuery}
                        onChange={(e) => setMovieQuery(e.target.value)}
                    />
                    {movies.length > 0 ? (
                        <div className="results">
                            <h3>Movies</h3>
                            <ul>
                                {movies.map((movie) => (
                                    <li key={movie._id} onClick={() => handleMovieClick(movie)}>
                                        <strong>{movie.title}</strong> <img src={movie.thumbnail} alt={movie.title} />
                                    </li>
                                ))}
                            </ul>
                        </div>
                    ) : (
                        <div className="no-results">No Results Found</div>
                    )}
                </section>
            </div>
        </div>
    );
};

export default SearchScreen;
