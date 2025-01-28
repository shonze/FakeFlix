import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import TopMenu from "../TopMenu/TopMenu";
import Movielst from "../Movieslst/Movieslst";
import './categoryPage.css'; // Import the CSS file

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
    const [theme, setTheme] = useState(localStorage.getItem("theme"));
    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();

    // Checks if the user is permitted to enter the screen
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
            const isAdmin = await response.json();
            setIsAdmin(isAdmin);
        };

        checkValidation();
    }, [navigate]);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:8080/api/categories/${id}`, {
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

                if (data.movies) {
                    setCategoryMoviesChunked(chunkArray(data.movies, 4));
                }
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchCategories();
    }, [id]);

    useEffect(() => {
        const handleStorageChange = (event) => {
            setTheme(localStorage.getItem("theme"));
        };

        // Listen for changes to localStorage (triggered by other windows/tabs)
        window.addEventListener("storage", handleStorageChange);
    }, []);

    if (!category) {
        return <h2>Category not found</h2>;
    }

    return (
        <div className={`bg-${theme} `}>
            <TopMenu admin={isAdmin}/>
            {category.movies.length !== 0 ? (
                <div>
                    <h1
                        className={`${localStorage.getItem("theme") === "dark" ? "text-light" : "text-dark"} category-page-head`}
                    >
                        {category.name}
                    </h1>
                    <div className={`movies-row bg-${theme}`}>
                        {categoryMoviesChunked.map((movieIds, index) => (
                            <div key={index} className="movie-card">
                                <Movielst Movieslst={movieIds} />
                            </div>
                        ))}
                    </div>
                </div>) : (
                <div className="full-screen">
                    <h1
                        className={`${localStorage.getItem("theme") === "dark" ? "text-light" : "text-dark"} category-page-head`}
                    >
                        {category.name}
                    </h1>
                    <h2 className="no-movies-found">
                        No Movie Found!
                    </h2>
                    <h3 className="no-movies-found">
                        Come Back Later.
                    </h3>
                </div>
            )}
        </div>
    );
};

export default CategoryPage;
