import { useState, useEffect } from 'react';
import './Movie.css';
import MovieDescription from '../Pages/MovieDescription';

function Movie({ id }) {
    const [movie, setMovie] = useState({});
    const [isHovered, setIsHovered] = useState(false);

    useEffect(() => {
        const fetchMovie = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/movies/${id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();
                setMovie(data);
            } catch (error) {
                console.error("Error fetching movie:", error);
            }
        };
        fetchMovie();
    }, [id]);

    return (
        <div
            className={`movie-container ${isHovered ? 'hovered' : ''}`}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <img className="thumbnail" src={movie.thumbnail} alt={movie.title} />
            {isHovered && (
                <div className="hover-overlay">
                    <MovieDescription movie={movie} />
                </div>
            )}
        </div>
    );
}

export default Movie;
