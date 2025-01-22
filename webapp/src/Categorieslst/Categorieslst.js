import { useState, useEffect } from 'react';

const Categorieslst = () => {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await fetch("http://localhost:3000/api/movies", {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
          },
          body: {
            // Add any necessary request body here
          }
        });
        
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        setCategories(data); 
      } catch (error) {
        console.error("Error fetching categories:", error);
      }
    };

    fetchCategories();
  }, []);

  return (
    <div>
      {Object.keys(categories).length > 0 ? (
        <ul>
          {Object.entries(categories).map(([category, movies]) => (
            <li key={category}>
              <strong>{category}:</strong> {movies}
            </li>
          ))}
        </ul>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default Categorieslst;