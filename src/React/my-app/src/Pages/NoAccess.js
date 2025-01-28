import React from "react";
import { Link } from "react-router-dom"; // React Router for navigation
import './NoAccess.css'

const NotFoundPage = () => {
  return (
    <div className="noAccess-page">
      <div className="noAccess-container">
        <h1 className="noAccess-title">Oops! You don't have access to this page</h1>
        <Link to="/" className="noAccess-home-link">Go Back Home</Link>
      </div>
    </div>
  );
};

export default NotFoundPage;
