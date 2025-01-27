import { useState, useEffect } from 'react';
import './Movie.css';
import MovieDescription from '../Pages/MovieDescription'

function Movie({ id }) {
    const [movie, setMovie] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const toggleModal = () => setShowModal(!showModal);

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

    const theme = localStorage.getItem("theme");

    return (
        <div
            className="thumbnail"
            onClick={() => {
                // navigate(`/movie/${movie._id}`);
                toggleModal();
            }}
            style={{ cursor: "pointer" }}
        >
            <div className="cube">
                <div className="cube-face front">
                    <img src={movie.thumbnail} alt="photo" />
                </div>
            </div>

            {showModal && (
                <div className="modal fade show d-block center-container">
                    <MovieDescription movie={movie} />
                </div>
            )}
        </div>
    );
};

export default Movie;