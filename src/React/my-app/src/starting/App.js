import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import StartingPage from './StartingPage';
import RegisterPage from '../register/Register';
import LoginPage from '../register/Register';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<StartingPage />} />
                <Route path="/register" element={<RegisterPage />} /> 
                <Route path="/login" element={<LoginPage />} /> 
            </Routes>
        </Router>
    );
};

export default App;