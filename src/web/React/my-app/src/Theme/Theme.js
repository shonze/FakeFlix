import React from 'react';

const Theme = () => {
    const handleClick = () => {
        if(localStorage.getItem(("theme")) === "light"){
            localStorage.setItem("theme","dark");
        } else {
            localStorage.setItem("theme","light");
        }
        window.dispatchEvent(new Event("storage"));
    }   

    return (
        <button onClick={handleClick} className={`btn text-${localStorage.getItem("theme") === "dark" ? "light" : "dark"}  bg-transparent border-0`}>{localStorage.getItem("theme")}</button>
    );
}

export default Theme;