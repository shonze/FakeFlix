import React from "react";
import { Link } from "react-router-dom"; // React Router for navigation
import './PleaseConnect.css'

const PleaseConnect = () => {
  return (
    <div className="pleaseConnect-page">
      <div className="pleaseConnect-container">
        <h1 className="pleaseConnect-title">Oops! You need to connect to see this page</h1>
        <Link to="/" className="pleaseConnect-home-link">Go Back Home</Link>
      </div>
    </div>
  );
};

export default PleaseConnect;