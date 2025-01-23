import React from "react";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import TopMenu from "../TopMenu/TopMenu";
import Movie from "../Movie/Movie";
import Movielst from "../Movieslst/Movieslst";

function HomeDescriptionPage() {
    const { id } = useParams();
    const [recommendedMovies, setRecommendedMovies] = useState([]);

    useEffect(() => {
        const fetchRecommandation = async () => {
            try {
                const response = await fetch(`http://localhost:3002/api/movies/${id}/recommend/`, {
                    method: 'GET'
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
                }

                const data = await response.json();
                setRecommendedMovies(data);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchRecommandation();
    });

    return (
        <div className="bg-dark min-vh-100">
            <TopMenu />
            <div>
                <Movie id={id} className="w-100 h-100" />
                <div key={recommendedMovies[0]} className="col-md-3 mb-4">
                    <Movielst Movieslst={recommendedMovies} />
                </div>
            </div>
        </div>
    );
};

export default HomeDescriptionPage