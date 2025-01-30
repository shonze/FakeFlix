import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import TopMenu from "../TopMenu/TopMenu";
import Movielst from "../Movieslst/Movieslst";
import './CategoryPage.css'; // Import the CSS file
import PleaseConnect from './PleaseConnect';

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
    const [isLogged, setIsLogged] = useState(null);
    const navigate = useNavigate();

    // Checks if the user is permitted to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/tokens/validate`, {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json',
                        'Requireadmin': false,
                    }
                });
                if (!response.ok) {
                    setIsLogged(false)
                    return;
                }
                const isAdmin = await response.json();
                setIsAdmin(isAdmin.isAdmin);
                setIsLogged(true)
                
            } catch (error) {
                setIsLogged(false)
            };
        };

        checkValidation();
    }, []);

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/categories/${id}`, {
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
    if (isLogged === null) {
        // Render nothing or a loading spinner while the validation is in progress
        return <div>Loading...</div>;
    }
    return (
        isLogged ? (
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
        ) : (
            <PleaseConnect />
        )
    );
};

export default CategoryPage;
