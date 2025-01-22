import { useState, useEffect } from 'react';

function Movie(id) {
    const [movie, setMovie] = useState([])

    useEffect(() => {
        const fetchMovie = async () => {
            try {
                console.log(id.id)
                const response = await fetch(`http://localhost:3002/api/movies/${id.id}`, {
                    method: 'GET'
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
                }

                const data = await response.json();

                console.log(data)

                setMovie(data);
            } catch (error) {
                console.error("Error fetching movie:", error);
            }
        };
        fetchMovie();
    }, [id]);

    return (
        <div className="card">
            <img src={movie.thumbnail} className="card-img-top" alt="Movie Poster" />
            <div className="card-body">
                <h5 className="card-title">{movie.title}</h5>
                <p className="card-text">{movie.description}</p>
                <a href={`/movies/${id}`} className="btn btn-primary">Play</a>
            </div>
        </div>
    );
};

export default Movie;