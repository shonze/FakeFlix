import React from "react";
import { Link } from "react-router-dom"; // React Router for navigation
import './NotFoundPage.css'

const NotFoundPage = () => {
  return (
    <div className="not-found-page">
      <div className="not-found-container">
        <h1 className="not-found-title">404</h1>
        <p className="not-found-message">Oops! The page you're looking for doesn't exist.</p>
        <Link to="/" className="not-found-home-link">Go Back Home</Link>
      </div>
    </div>
  );
};

export default NotFoundPage;
