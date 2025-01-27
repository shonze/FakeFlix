import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import './SearchTool.css';

const SearchScreen = () => {
    const [movieQuery, setMovieQuery] = useState('');
    const [movies, setMovies] = useState([]);
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

    const handleMovieSearch = async (e) => {
        e.preventDefault();
        try {
            const token = localStorage.getItem('jwtToken');
            const response = await fetch(`http://localhost:8080/api/movies/search/${movieQuery}`, {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
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

    return (
        <div className="search-container">
            <h1>FakeFlix Movies</h1>
            <div className="search-sections">
                <section className="movie-search">
                    <h2>Search Movies</h2>
                    <form onSubmit={handleMovieSearch}>
                        <input
                            type="text"
                            placeholder="What would you like to see today?"
                            value={movieQuery}
                            onChange={(e) => setMovieQuery(e.target.value)}
                            required
                        />
                        <button type="submit">Search</button>
                    </form>
                    {movies.length > 0 ? (
                        <div className="results">
                            <h3>Movies</h3>
                            <ul>
                                {movies.map((movie) => (
                                    <li key={movie._id} onClick={() => handleMovieClick(movie)}>
                                        <strong>{movie.title}</strong> <img src={movie.thumbnail} className='' />
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
