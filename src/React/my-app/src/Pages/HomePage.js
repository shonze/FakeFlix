import Categorieslst from '../Categorieslst/Categorieslst';
import TopMenu from '../TopMenu/TopMenu';
import './HomePage.css';
import PleaseConnect from './PleaseConnect';

import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';

function HomePage() {
    const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
    const [isAdmin, setIsAdmin] = useState(false);
    const [isLogged, setIsLogged] = useState(null);
    const navigate = useNavigate();

    // Checks if the user is permited to enter the screen
    useEffect(() => {
        const checkValidation = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch('http://localhost:8080/api/tokens/validate', {
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
        const handleStorageChange = (event) => {
            setTheme(localStorage.getItem("theme"));
        };

        // Listen for changes to localStorage (triggered by other windows/tabs)
        window.addEventListener("storage", handleStorageChange);
    }, []);

    if (isLogged === null) {
        // Render nothing or a loading spinner while the validation is in progress
        return <div>Loading...</div>;
    }

    return (
        isLogged ? (
            <div className={`home-bg-${theme} min-vh-100`}>
                <TopMenu admin={isAdmin} />
                <Categorieslst />
            </div>
        ) : (
            <PleaseConnect />
        )
    );
    
}

export default HomePage;