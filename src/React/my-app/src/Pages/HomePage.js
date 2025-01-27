import Categorieslst from '../Categorieslst/Categorieslst';
import TopMenu from '../TopMenu/TopMenu';
import '../App.css';

import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';

function HomePage() {
    const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
    const [isAdmin,setIsAdmin] = useState(false);
    const navigate = useNavigate();

    // Checks if the user is permited to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            const token = localStorage.getItem('jwtToken');

            const response = await fetch('http://localhost:8080/api/tokens/validate', {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                    'requiredAdmin': false
                }
            });
            if (!response.ok) {
                 navigate('/404');
            }
        };

        checkValidation();

        const checkIfAdmin = async () => {
            const token = localStorage.getItem('jwtToken');

            const response = await fetch('http://localhost:8080/api/tokens/validate', {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json',
                    'requiredAdmin': true
                }
            });
            if (!response.ok) {
                setIsAdmin(true);
            }
        };

        checkIfAdmin();
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
        <div className={`bg-${theme} min-vh-100`} >
            <TopMenu admin={isAdmin}/> 
            <Categorieslst />
        </div>
    );
}

export default HomePage;