import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { Dropdown } from "bootstrap";
import './CategorySearch.css';

const CategorySearch = () => {
    const [categories, setCategories] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCategories = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:8080/api/categories`, {
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

                setCategories(data);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        };

        fetchCategories();
    }, []);

    return (
        <li className="nav-item dropdown">
            <a className={`nav-link dropdown-toggle text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"}`} href="" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                Watch By Categories
            </a>
            <ul className={`dropdown-menu dropdown-menu-${localStorage.getItem("theme")}`} aria-labelledby="navbarDarkDropdownMenuLink">
                {categories.map((category) => (
                    <li key={category._id}>
                        <a
                            className="dropdown-item"
                            href="#"
                            onClick={(e) => {
                                e.preventDefault(); // Prevent the default link behavior
                                e.stopPropagation(); // Stop the dropdown from closing
                                navigate(`/category/${category._id}`);
                                // Reopen the dropdown menu
                                const dropdown = Dropdown.getOrCreateInstance(document.getElementById('navbarDarkDropdownMenuLink'));
                                dropdown.show();
                            }}
                        >
                            {category.name}
                        </a>
                    </li>
                ))}
            </ul>
        </li>
    );
};

export default CategorySearch;