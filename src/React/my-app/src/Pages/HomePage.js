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
    const [userPic, setUserPic] = useState('');
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
        const getUserPicture = async () => {
            try {
                const token = localStorage.getItem('jwtToken');

                const response = await fetch('http://localhost:8080/api/tokens/user', {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json',
                    }
                });
                if (!response.ok) {
                    setIsLogged(false)
                    return;
                }
                const pic = await response.json();
                console.log(pic);
                setUserPic(pic.userPicture)
                setIsLogged(true)
                
            } catch (error) {
                console.log(error);
            };
        };

        checkValidation();
        getUserPicture();
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

    console.log("USER PIC"+ userPic);
    return (
        isLogged ? (
            <div className={`home-bg-${theme} min-vh-100`}>
                <TopMenu admin={isAdmin}
                        userPic={userPic} />
                <Categorieslst />
            </div>
        ) : (
            <PleaseConnect />
        )
    );
    
}

export default HomePage;