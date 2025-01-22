import React from 'react';
import './TopMenu.css';
import CategorySearch from '../CategorySearch/CategorySearch'

const TopMenu = () => {
    const homePage = `http://${window.location.hostname}:${window.location.port}`

    return (
        <nav className=".bg-transparent navbar navbar-expand-lg navbar-dark sticky-top">
            <a className="navbar-brand" href={homePage}>MovieTime</a>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav ml-auto">
                    <li className="nav-item">
                        <a className="nav-link" href={homePage}>Home</a>
                    </li>
                    <CategorySearch />
                    <li className="nav-item">
                        <a className="nav-link" href={homePage}>Admin</a>
                    </li>
                </ul>
            </div>
            <div className="bg-dark d-flex justify-content-center align-items-center">
                <div className="position-relative">
                    <input type="text"
                        className="form-control rounded-pill search-bar pe-5"
                        placeholder="Search..."
                        aria-label="Search" />
                    <button className="btn position-absolute top-50 end-0 translate-middle-y" type="button">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-search" viewBox="0 0 16 16">
                            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
                        </svg>
                    </button>
                </div>
            </div>
        </nav >
    );
};

export default TopMenu;