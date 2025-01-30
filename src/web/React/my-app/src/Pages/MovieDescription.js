import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Movielst from "../Movieslst/Movieslst.js";
import "./MovieDescription.css";

function HomeDescriptionPage({ movie }) {
  const [recommendedMovies, setRecommendedMovies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRecommandation = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/movies/${movie._id}/recommend/`, {
          method: "GET",
          headers: {
            Authorization: "Bearer " + token,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const recommendedMovies = await response.json();
        setRecommendedMovies(recommendedMovies);
      } catch (error) {
        console.error("Error fetching recommended movies:", error);
      }
    };

    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem("jwtToken");
        const categories = await Promise.all(
          movie.categories.map(async (categoryId) => {
            const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/categories/${categoryId}`, {
              method: "GET",
              headers: {
                Authorization: "Bearer " + token,
                "Content-Type": "application/json",
              },
            });

            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
            }

            const category = await response.json();
            return category.name;
          })
        );

        setCategories(categories);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchRecommandation();
    fetchCategories();
  }, [movie._id, movie.categories]);

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

      {recommendedMovies.length > 0 && (
        <div className="recommended">
          <h3 className="recommended-title">Recommended Movies</h3>
          <Movielst Movieslst={recommendedMovies} />
        </div>
      )}
    </div>
  );
}

export default HomeDescriptionPage;
