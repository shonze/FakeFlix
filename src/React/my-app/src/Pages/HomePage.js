import Categorieslst from '../Categorieslst/Categorieslst';
import TopMenu from '../TopMenu/TopMenu';
import '../App.css';

import { useState, useEffect } from 'react';

function HomePage() {
    const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");

    useEffect(() => {
        const handleStorageChange = (event) => {
            const button = event.explicitOriginalTarget
            if (button.id === "dark" || button.id === "light") {
                setTheme(button.id);  // Update theme from localStorage change
            }
        };

        // Listen for changes to localStorage (triggered by other windows/tabs)
        window.addEventListener("storage", handleStorageChange);
    }, []);

    return (
        <div className={`bg-${theme} min-vh-100`}>
            <TopMenu />
            <Categorieslst userId="6790b3ec594b4ec368666d12" />
        </div>
    );
}

export default HomePage;