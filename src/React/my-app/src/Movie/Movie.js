import { useState, useEffect } from 'react';
import './Movie.css';
import MovieDescription from '../Pages/MovieDescription';

function Movie({ id }) {
    const [movie, setMovie] = useState({});
    const [isHovered, setIsHovered] = useState(false); // State to track hover

    useEffect(() => {
        const fetchMovie = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:8080/api/movies/${id}`, {
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
            className="thumbnail"
            style={{ cursor: "pointer" }}
            onMouseEnter={() => setIsHovered(true)} // Set hover state to true
            onMouseLeave={() => setIsHovered(false)} // Set hover state to false
        >
            <div className="cube">
                <div className="cube-face front">
                    <img src={movie.thumbnail} alt="photo" />
                </div>
                {isHovered && ( // Conditionally render the MovieDescription on hover
                    <div className="cube-face back">
                        <MovieDescription movie={movie} />
                    </div>
                )}
            </div>
        </div>
    );
}

export default Movie;
