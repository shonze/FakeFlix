import { useState, useEffect } from 'react';
import Movieslst from '../Movieslst/Movieslst'
import TopMovie from '../TopMovie/TopMovie';
import './Categorieslst.css';

const Categorieslst = () => {
  const [categoriesMovies, setCategoriesMovies] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem('jwtToken');
        console.log(token)
        const response = await fetch(`http://localhost:8080/api/movies`, {
          method: 'GET',
          headers: {
            'Authorization': 'Bearer ' + token,
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
  }, []);

  const AllReturnedMovies = Object.values(categoriesMovies).flat();

  const RandomMovieId = AllReturnedMovies[~~(Math.random() * AllReturnedMovies.length)];

  return (
    <div className="container-fluid custom-container-fluid">
      <TopMovie id={RandomMovieId} />
      <div className="custom-container">
        {Object.keys(categoriesMovies).length > 0 ? (
          <div className="custom-container">
            {Object.entries(categoriesMovies).map(([category, movies]) => {
              return movies.length > 0 ? (
                <div key={category}>
                  <h6
                    className={`text-center ${localStorage.getItem("theme") === "dark" ? "text-light" : "text-dark"
                      }`}
                  >
                    {category}
                  </h6>
                  <Movieslst key={category} Movieslst={movies} />
                </div>
              ) : null;
            })}
          </div>
        ) : (
          <p className="no-movies">No Movies Exist Yet, Come Back Later!!!</p>
        )}
      </div>
    </div>
  );
};

export default Categorieslst;