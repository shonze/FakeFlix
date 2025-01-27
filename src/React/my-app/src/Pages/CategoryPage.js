import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

import TopMenu from "../TopMenu/TopMenu";
import Movielst from "../Movieslst/Movieslst";

const chunkArray = (array, chunkSize) => {
    const chunks = [];
    for (let i = 0; i < array.length; i += chunkSize) {
        chunks.push(array.slice(i, i + chunkSize));
    }
    return chunks;
};

function CategoryPage() {
    const { id } = useParams();

    const [category, setCategory] = useState(null);
    const [categoryMoviesChunked, setCategoryMoviesChunked] = useState([]);
    const navigate = useNavigate();

    // Checks if the user is permited to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            const token = localStorage.getItem('jwtToken');

            const response = await fetch('http://localhost:8080/api/tokens/validate', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            });
            if (!response.ok) {
                 navigate('/404');
            }
        };

        checkValidation();
    }, []);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:3002/api/categories/${id}`, {
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
                setCategory(data);

                if (category != null) {
                    setCategoryMoviesChunked(chunkArray(category.movies, 4));
                }
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchCategories();
    }, []);

    if (!category) {
        return <h2>Category not found</h2>;
    }

    if (category.movies.length === 0) {
        return <h2>No movies!</h2>;
    }

    return (
        <div className={`bg-${localStorage.getItem("theme")} min-vh-100`}>
            <TopMenu />
            <div>
                {categoryMoviesChunked.map((movieIds) => (
                    <div key={movieIds[0]} className="col-md-3 mb-4">
                        <Movielst Movieslst={movieIds} />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CategoryPage