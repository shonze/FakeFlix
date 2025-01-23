import React from "react";
import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Movie from "../Movie/Movie";
import TopMenu from "../TopMenu/TopMenu";

function CategoryPage() {
    const { id } = useParams();

    const [category, setCategory] = useState(null);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const response = await fetch(`http://localhost:3002/api/categories/${id}`, {
                    method: 'GET'
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
                }

                const data = await response.json();
                setCategory(data);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchCategories();
    }, [id]);

    if (!category) {
        return <h2>Category not found</h2>;
    }

    return (
        <div className="bg-dark">
            <TopMenu />
            <div>
                {category.movies.map((movieId) => (
                    <div key={movieId} className="col-md-3 mb-4">
                        <Movie id={movieId} />
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CategoryPage