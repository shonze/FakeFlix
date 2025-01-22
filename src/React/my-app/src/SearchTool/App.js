import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import SearchTool from './SearchTool';
import WatchMovie from './WatchMovie';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<SearchTool />} />
                <Route path="/watch-movie" element={<WatchMovie />} /> 
            </Routes>
        </Router>
    );
};

export default App;
