import { useState, useEffect } from 'react';
import CategoryMovieslst from '../CategoryMovieslst/CategoryMovieslst'
import TopMovie from '../TopMovie/TopMovie';

const Categorieslst = (userId) => {
  const [categoriesMovies, setCategoriesMovies] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch("http://localhost:3002/api/movies", {
          method: 'GET',
          headers: {
            'userId': userId.userId
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
    <>
      <TopMovie id={RandomMovieId} />
      <div className="container my-5">
        {Object.keys(categoriesMovies).length > 0 ? (
          <div className="container">
            {Object.entries(categoriesMovies).map(([category, movies]) => {
              return movies.length > 0 ? (
                <CategoryMovieslst key={category} CategoryName={category} Movieslst={movies} />
              ) : null;
            })}
          </div>
        ) : (
          <p>No Movies Exists Yet, Come Back Later!!!</p>
        )}
      </div>
    </>
  );
};

export default Categorieslst;