import React from 'react';
import { useState } from 'react'

const Theme = () => {
    const [theme, setTheme] = useState("light");

    const handleClick = () => {
        if(theme === "light"){
            setTheme("dark");
        } else {
            setTheme("light");
        }
        localStorage.setItem('theme', theme);
        window.dispatchEvent(new Event("storage"));
    }   

    return (
        <button id={theme} onClick={handleClick} className={`btn btn-${localStorage.getItem('theme')} bg-transparent border-0`}>{theme}</button>
    );
}

export default Theme;