import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SearchTool from './SearchTool/SearchTool';
import WatchMovie from './SearchTool/WatchMovie';
import AdminScreen from './AdminScreen/AdminScreen'; 
import HomePage from "./Pages/HomePage";
import CategoryPage from "./Pages/CategoryPage";

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/home" element={<HomePage />} />
                <Route path="/category/:id" element={<CategoryPage />} />
                <Route path="/" element={<SearchTool />} />
                <Route path = "/admin" element = {<AdminScreen />} />
                <Route path="/watch-movie" element={<WatchMovie />} /> 
            </Routes>
        </Router>
    );
};

export default App;
