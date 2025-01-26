import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import StartingPage from './StartingPage';
import RegisterPage from '../register/RegisterScreen';
import LoginPage from '../login/LoginScreen';

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