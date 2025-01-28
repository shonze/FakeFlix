import React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Movielst from "../Movieslst/Movieslst.js";
import './MovieDescription.css';

function HomeDescriptionPage({ movie }) {
  const [recommendedMovies, setRecommendedMovies] = useState([]);
  const [categories, setCategories] = useState([]);
  const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRecommandation = async () => {
      // Fetch recommended movies
      try {
        const token = localStorage.getItem('jwtToken');

        const response = await fetch(`http://localhost:8080/api/movies/${movie._id}/recommend/`, {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'application/json'
          }
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
      // Fetch categories
      try {
        const token = localStorage.getItem('jwtToken');

        const categories = await Promise.all(
          movie.categories.map(async (categoryId) => {
            const response = await fetch(`http://localhost:8080/api/categories/${categoryId}`, {
              method: 'GET',
              headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
              }
            });

            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
            }

            return await response.json(); // Return the JSON response
          })
        );

        setCategories(categories);
        console.log(categories);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    const handleStorageChange = (event) => {
      const button = event.explicitOriginalTarget
      if (button.id === "dark" || button.id === "light") {
        setTheme(button.id);  // Update theme from localStorage change
      }
    };

    // Listen for changes to localStorage (triggered by other windows/tabs)
    window.addEventListener("storage", handleStorageChange);

    fetchRecommandation();
    fetchCategories();
  }, [movie._id, movie.categories]);

  return (
    <div className={`parent-container ${theme === "dark" ? "bg-dark text-light" : "bg-light text-dark"}`}>
      <div className="relative grid md-grid-cols-2 gap-6">
        <div className="half-top-container">
          <img src={movie.thumbnail} className="full-thumbnail" alt={movie.title} />
        </div>
        <button
          className="play-button"
          onClick={() => { navigate('/watch-movie', { state: { movie } }) }}
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" viewBox="0 0 24 24" fill="currentColor">
            <path fillRule="evenodd" d="M4.5 5.653c0-1.426 1.529-2.33 2.779-1.643l11.54 6.348c1.295.712 1.295 2.573 0 3.285L7.28 19.991c-1.25.687-2.779-.217-2.779-1.643V5.653z" clipRule="evenodd" />
          </svg>
          Play
        </button>
        <div className="details">
          <div className="details-left">
            <h2 className="title">{movie.title}</h2>
            <p className="description">{movie.description}</p>
          </div>
          <div className="details-right">
            <div className="runtime">
              <strong>Runtime:</strong> {movie.length}
            </div>
            <div className="genres">
              <strong>Genres:</strong>
              <div className="genres-list">
                {categories.map((category) => (
                  <span key={category._id} className="genre">{category.name}</span>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      {recommendedMovies.length > 0 ? (
        <div className="recommended">
          <h3 className="recommended-title">Recommended Movies</h3>
          <Movielst Movieslst={recommendedMovies} />
        </div>
      ) : null}
    </div>
  );
};

export default HomeDescriptionPage