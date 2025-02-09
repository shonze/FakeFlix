import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Movielst from "../Movieslst/Movieslst.js";
import "./MovieDescription.css";

function HomeDescriptionPage({ movie }) {
  const [recommendedMovies, setRecommendedMovies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
  const navigate = useNavigate();

  return (
    <div className={`movieDescription-body ${theme === "dark" ? "bg-dark text-light" : "bg-light text-dark"}`}>
      <div className="movieDescription-container">
        <img
          src={movie.thumbnail}
          alt={`${movie.title} Background`}
          className="movieDescription-background"
        />
        <div className="movieDescription-overlay">
          <div className="movieDescription-info">
            <h1 className="movieDescription-title">{movie.title}</h1>
            <p className="movieDescription-description">
              {movie.description}
              <p></p>
              length: {movie.length}
            </p>
            <button className="movieDescription-play-button" onClick={() => navigate("/watch-movie", { state: { movie } })}>
              More Info
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default HomeDescriptionPage;
