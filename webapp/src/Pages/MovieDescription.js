import React from "react";
import { useState, useEffect } from "react";

import Movielst from "../Movieslst/Movieslst";

function HomeDescriptionPage({ movie }) {
    const [recommendedMovies, setRecommendedMovies] = useState([]);
    const [categories, setCategories] = useState([]);
    const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");

    useEffect(() => {
        const fetchRecommandation = async () => {
            // Fetch recommended movies
            try {
                const response = await fetch(`http://localhost:3002/api/movies/${movie._id}/recommend/`, {
                    method: 'GET',
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
                const categories = await Promise.all(
                    movie.categories.map(async (categoryId) => {
                        const response = await fetch(`http://localhost:${process.env.PORT}/api/categories/${categoryId}`, {
                            method: 'GET'
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
    },[movie._id,movie.categories]);

    return (
        <div className={`container mx-auto bg-${theme} text-${theme === "dark" ? "light" : "dark"} p-6`}>
 <div className="grid md:grid-cols-2 gap-6">
   <img
     src={movie.thumbnail}
     className="w-full h-96 object-cover rounded-lg"
     alt={movie.title}
   />
   <button className="absolute right-6 top-6 bg-red-600 text-white px-8 py-3 text-lg rounded-full hover:bg-red-700 transition-colors duration-300 flex items-center justify-center gap-3 shadow-lg">
 <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" viewBox="0 0 24 24" fill="currentColor">
   <path fillRule="evenodd" d="M4.5 5.653c0-1.426 1.529-2.33 2.779-1.643l11.54 6.348c1.295.712 1.295 2.573 0 3.285L7.28 19.991c-1.25.687-2.779-.217-2.779-1.643V5.653z" clipRule="evenodd" />
 </svg>
 Play
</button>
   <div>
     <h2 className="text-3xl font-bold mb-4">{movie.title}</h2>
     <p className="text-gray-800 mb-4">{movie.description}</p>
     
     <div className="bg-gray-100 p-4 rounded-lg">
       <div className="mb-2">
         <strong>Runtime:</strong> {movie.length}
       </div>
       <div>
         <strong>Genres:</strong>
         <div className="flex flex-wrap gap-2 mt-2">
           {categories.map((category) => (
             <span key={category._id} className={`bg-blue-600 px-2 py-1 rounded-full text-sm`}>
               {category.name}
             </span>
           ))}
         </div>
       </div>
     </div>
   </div>
 </div>

 <div className="mt-8">
   <h3 className="text-2xl font-bold mb-4">Recommended Movies</h3>
   <Movielst Movieslst={recommendedMovies} />
 </div>
</div>
    );
};

export default HomeDescriptionPage