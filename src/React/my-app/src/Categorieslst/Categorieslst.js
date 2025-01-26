import { useState, useEffect } from 'react';
import Movieslst from '../Movieslst/Movieslst'
import TopMovie from '../TopMovie/TopMovie';
import './Categorieslst.css';

const Categorieslst = ({ userId }) => {
  const [categoriesMovies, setCategoriesMovies] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem('jwtToken');

        const response = await fetch(`http://localhost:3002/api/movies`, {
          method: 'GET',
          headers: {
            'userId': userId,
            'Authorization': 'Bearer' + token,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
        }

        const data = await response.json();
        setCategoriesMovies(data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, [userId]);

  const AllReturnedMovies = Object.values(categoriesMovies).flat();

  const RandomMovieId = AllReturnedMovies[~~(Math.random() * AllReturnedMovies.length)];

  return (
    <div className="container-fluid">
      <TopMovie id={RandomMovieId} />
      <div className="container">
        {Object.keys(categoriesMovies).length > 0 ? (
          <div className="container">
            {Object.entries(categoriesMovies).map(([category, movies]) => {
              return movies.length > 0 ? (
                <div key={category} className="sticky-left">
                  <h6 className={`text-start text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"}`}>{category}</h6>
                  <Movieslst key={category} Movieslst={movies} />
                </div>
              ) : null;
            })}
          </div>
        ) : (
          <p>No Movies Exists Yet, Come Back Later!!!</p>
        )}
      </div>
    </div>
  );
};

export default Categorieslst;