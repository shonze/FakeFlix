import Categorieslst from '../Categorieslst/Categorieslst';
import TopMenu from '../TopMenu/TopMenu';
import './HomePage.css';

import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';

function HomePage() {
    const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
    const [isAdmin, setIsAdmin] = useState(false);
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
            const isAdmin = await response.json();
            console.log(isAdmin)
            setIsAdmin(isAdmin);
        };

        checkValidation();
    }, []);

    useEffect(() => {
        const handleStorageChange = (event) => {
            const button = event.explicitOriginalTarget
            if (button.id === "dark" || button.id === "light") {
                setTheme(button.id);  // Update theme from localStorage change
            }
        };

        // Listen for changes to localStorage (triggered by other windows/tabs)
        window.addEventListener("storage", handleStorageChange);
    }, []);

    return (
        <div className={`home-bg-${theme} min-vh-100`} >
            <TopMenu admin={isAdmin} />
            <Categorieslst />
        </div>
    );
}

export default HomePage;