import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";

function Movie({ id }) {
    const [movie, setMovie] = useState([]);
    const navigate = useNavigate();
    
    useEffect(() => {
        const fetchMovie = async () => {
            try {
                const response = await fetch(`http://localhost:3002/api/movies/${id}`, {
                    method: 'GET'
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
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
            className="card position-relative overflow-hidden"
            onClick={() => navigate(`/movie/${movie._id}`)}
            style={{ cursor: "pointer" }}
        >
            <img
                src={movie.thumbnail}
                className="card-img-top"
                alt="Movie Poster"
            />
            <div className="card-body position-absolute bottom-0 start-0 w-100 bg-dark bg-opacity-75 text-white p-2 d-none">
                <h5 className="card-title">{movie.title}</h5>
                <p className="card-text">{movie.description}</p>
            </div>
        </div>
    );
};

export default Movie;