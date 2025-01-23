import React, { useState } from 'react';
import './Login.css';
import Field from './Field';
import { useNavigate } from "react-router-dom"; // Import useNavigate for navigation

const LoginScreen = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });
    const storedEmail = localStorage.getItem("email");
    if (storedEmail) {
        formData.email = storedEmail;
    }

    const navigate = useNavigate(); // Hook for programmatic navigation
    const handleRegister = () => {
        navigate("../register"); // Navigate to the login page
      };
    
    const handleHome = () => {
        navigate("../home"); // Navigate to the login page
      };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    const handleFileChange = (e) => {
        setFormData((prevData) => ({
            ...prevData,
            profilePicture: e.target.files[0],
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        // const { password } = formData; ???

        const data = {
            username: formData.username,
            password: formData.password
        };

        try {
            const response = await fetch('http://localhost:3000/api/tokens', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const result = await response.json();
                if (result.token) {
                    localStorage.setItem('jwtToken', result.token);
                    handleHome();
                } else {
                    alert('Login failed: ' + (result.message || 'Invalid input'));
                }
            } else {
                alert('Login failed: ' + (response.message || 'Invalid input'));
            }
        } catch (error) {
            alert('An error occurred: ' + error.message);
        }
    };

    return (
        <div className="login-container">
            <div className="form-container">
                <h1 className="app-title">Fakeflix</h1>
                <form onSubmit={handleSubmit}>
                    <div className="form-row">
                        <Field
                            label="Username"
                            name="username"
                            value={formData.username}
                            onChange={handleInputChange}
                            type="text"
                            required
                        />
                    </div>
                    <div className="form-row">
                        <Field  
                            label="Password"
                            name="password"
                            value={formData.password}
                            onChange={handleInputChange}
                            type="password"
                            required
                        />
                    </div>

                    <button type="submit" className="login-btn">
                        Login
                    </button>

                    <div className="login-text">
                        <p>
                            New to Fakeflix?{' '}
                            <a
                            href="/register"
                            className="register-link"
                            onClick={(e) => {
                                e.preventDefault(); // Prevent default navigation
                                handleRegister(); // Call the function
                            }}
                            >
                            Register here
                            </a>
                        </p>
                    </div>


                </form>
            </div>
        </div>
    );
};

export default LoginScreen;
