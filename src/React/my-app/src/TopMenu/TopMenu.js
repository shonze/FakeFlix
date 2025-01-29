import React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import './TopMenu.css';
import CategorySearch from '../CategorySearch/CategorySearch'
import Theme from '../Theme/Theme';

const TopMenu = ({ admin ,userPic, userName}) => {
    console.log("balls")
    console.log(userName)
    const [isTop, setIsTop] = useState(true);
    const [isAdmin, setIsAdmin] = useState(false);
    const [userPicture, setUserPicture] = useState('');
    const [userFullName, setUserFullName] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const handleScroll = () => {
            const isTop =
                (Math.round(window.scrollY)) ===
                0;

            setIsTop(isTop);
        };

        // Add scroll event listener
        window.addEventListener('scroll', handleScroll);

        // Cleanup function to remove event listener
        return () => {
            window.removeEventListener('scroll', handleScroll);
        };
    }, []); // Empty dependency array means this runs once on mount

    useEffect(() => {
        setIsAdmin(admin);
    }, [admin]);
    useEffect(() => {
        setUserPicture(userPic);
    }, [userPic]);
    useEffect(() => {
        setUserFullName(userName);
    }, [userName]);
    
    const handleLogOut = () => {
        // Remove jwtToken and rememberMe from local storage
        localStorage.removeItem("jwtToken");
        localStorage.removeItem("rememberMe");

        // Navigate to the parent route
        navigate("..");
    };

    const homePage = `http://${window.location.hostname}:${window.location.port}/home`

    console.log("name:")
    console.log(userFullName)

    return (
        <div className="sticky-top">
            <nav
                className={`navbar navbar-expand-lg`}
                style={{
                    transition: 'background-color 0.6s ease-in-out',
                    backgroundColor: isTop
                        ? 'transparent'
                        : (localStorage.getItem("theme") === 'dark' ? '#000' : '#fff')
                }}
            >
                <a className="navbar-brand" href={homePage}>Fakeflix</a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav ml-auto">
                        <CategorySearch />
                        {isAdmin ? (
                            <li className="nav-item">
                                <button
                                    className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"}  bg-transparent border-0`}
                                    onClick={() => { navigate('../admin') }}
                                >Admin</button>
                            </li>
                        ) : null}
                        <li className="nav-item">
                            <button
                                className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"} bg-transparent border-0`}
                                onClick={() => { handleLogOut() }}
                            >LogOut</button>
                        </li>
                        <Theme />
                    </ul>
                </div>

                <div className="d-flex align-items-center">
                    <div className="navbar-text welcome-text">
                        Welcome back: {userFullName}
                    </div>
                    {userPicture && (
                        <div className="image-preview-topMenu">
                            <img src={userPicture} alt="Profile Preview" className="preview-image-TopMenu" />
                        </div>
                    )}
                </div>


                <div className="d-flex justify-content-center align-items-center bg-white rounded shadow">
                    <div className="position-relative">
                        <button
                            className={`btn position-absolute top-50 end-0 translate-middle-y bg-${localStorage.getItem("theme")} border rounded-circle shadow`}
                            type="button"
                            onClick={() => navigate(`/search`)}
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                width="16"
                                height="16"
                                fill={localStorage.getItem("theme") === "dark" ? "white" : "black"}
                                className="bi bi-search"
                                viewBox="0 0 16 16"
                            >
                                <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
                            </svg>
                        </button>
                    </div>
                </div>
            </nav >
        </div>
    );
};

export default TopMenu;