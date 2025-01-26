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

    const theme = localStorage.getItem("theme");

    return (
        <div
            className="card position-relative overflow-hidden"
            onClick={() => {
                // navigate(`/movie/${movie._id}`);
                toggleModal();
            }}
            style={{ cursor: "pointer" }}
        >
            <img
                src={movie.thumbnail}
                className="w-full h-full object-cover"
                alt=""
            />
            <div className={`card-body position-absolute bottom-0 start-0 w-100 bg-${theme} bg-opacity-75 text-${theme === "dark" ? "white" : "black"} p-2 d-none`}>
                <h5 className="card-title">{movie.title}</h5>
                <p className="card-text">{movie.description}</p>
            </div>
            {showModal && (
                <div className="modal fade show d-block" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <MovieDescription movie={movie} />
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Movie;