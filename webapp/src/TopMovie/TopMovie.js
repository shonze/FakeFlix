import './TopMovie.css';
import { useState, useEffect } from 'react';

const TopMovie = ({ id }) => {
    const [movie, setMovie] = useState([])

    useEffect(() => {
        const fetchMovie = async () => {
            try {
                if(!id){
                    return;
                }

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
        <div className="container-fluid p-0 position-relative">
            <video className="full-movie w-100" autoPlay muted loop>
                <source src="http://localhost:3000/short.mp4" type="video/mp4" />
                Your browser does not support the video tag.
            </video>

            <div className="text-overlay position-absolute top-50 start-0 translate-middle-y text-white">
                <h1 className="display-3">{movie.title}</h1>
                <p className="lead">{movie.description}</p>
                <button>Play</button>
            </div>
        </div>
    );
};

export default TopMovie;