import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import StartingPage from './StartingPage';
import RegisterPage from '../register/RegisterScreen';
import LoginPage from '../login/LoginScreen';
import SearchScreen from '../SearchTool/SearchTool';
import WatchMovie from '../SearchTool/WatchMovie';
import AdminScreen from '../AdminScreen/AdminScreen'; 
import HomePage from '../Pages/HomePage'; 
import CategoryPage from '../Pages/CategoryPage';
import NotFoundPage from '../Pages/NotFoundPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/home" element={<HomePage />} />
                <Route path="/" element={<StartingPage />} />
                <Route path="/register" element={<RegisterPage />} /> 
                <Route path="/login" element={<LoginPage />} /> 
                <Route path="/search" element={<SearchScreen />} />
                <Route path = "/admin" element = {<AdminScreen />} />
                <Route path="/watch-movie" element={<WatchMovie />} /> 
                <Route path="/category/:id" element={<CategoryPage />} />
                <Route path="/*" element={<NotFoundPage />} />
            </Routes>
        </Router>
    );
};

export default App;