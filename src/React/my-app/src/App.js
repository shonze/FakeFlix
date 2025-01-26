import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SearchTool from './SearchTool/SearchTool';
import WatchMovie from './SearchTool/WatchMovie';
import AdminScreen from './AdminScreen/AdminScreen'; 

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<SearchTool />} />
                <Route path = "/admin" element = {<AdminScreen />} />
                <Route path="/watch-movie" element={<WatchMovie />} /> 
            </Routes>
        </Router>
    );
};

export default App;
