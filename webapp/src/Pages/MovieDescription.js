import React from "react";
import { useState, useEffect } from "react";
import Movielst from "../Movieslst/Movieslst";

function HomeDescriptionPage({ movie }) {
    const [recommendedMovies, setRecommendedMovies] = useState([]);
    var categories = []

    useEffect(() => {
        const fetchRecommandation = async () => {
            try {
                const response = await fetch(`http://localhost:3002/api/movies/${movie._id}/recommend/`, {
                    method: 'GET'
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
                }

                const data = await response.json();
                setRecommendedMovies(data);

                categories = []

                const FindCategories = async () => {
                    for (categoryId in movie.categories) {
                        const response = await fetch(`http://localhost:3002/api/categories/${categoryId}`, {
                            method: 'GET'
                        });

                        if (!response.ok) {
                            throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
                        }

                        const data = await response.json();
                    }
                }
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchRecommandation();
    });

    return (
        <div className="bg-dark text-light min-vh-100 p-5">
            <img
                src={movie.thumbnail}
                className="card-img-top"
            />
            <div className="card">
                <div className="card-body bg-dark text-light">
                    <h3 className="card-title">{movie.title}</h3>
                    <p className="card-text">{movie.description}</p>
                    <div className="row">
                        <div className="col-6">
                            <p>Runtime: {movie.length}</p>
                            <p>Genres: {movie.categories.join(' * ')}</p>
                        </div>
                    </div>
                </div>
            </div>
            <h4 className="mt-4">Recommended Movies:</h4>
            <Movielst Movieslst={recommendedMovies} />
        </div>
    );
};

export default HomeDescriptionPage