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
                    headers:{
                        'Authorization': 'Bearer ' + token ,
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
            className="w-full h-full"
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