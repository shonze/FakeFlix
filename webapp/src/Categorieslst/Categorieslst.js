import { useState, useEffect } from 'react';
import CategoryMovieslst from '../CategoryMovieslst/CategoryMovieslst'

const Categorieslst = (userId) => {
  const [categories, setCategories] = useState([]);

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
        setCategories(data);
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, [userId]);

  return (
    <div class="container my-5">
      {Object.keys(categories).length > 0 ? (
        <div class="container">
          {Object.entries(categories).map(([category, movies]) => {
            return movies.length > 0 ? (
              <CategoryMovieslst CategoryName={category} Movieslst={movies} />
            ) : null;
          })}
        </div>
      ) : (
        <p>No Movies Exists Yet, Come Back Later!!!</p>
      )}
    </div>
  );
};

export default Categorieslst;