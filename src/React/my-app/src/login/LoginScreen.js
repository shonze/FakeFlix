import React, { useState, useEffect } from 'react';
import './Login.css';
import Field from './Field';
import { useNavigate } from "react-router-dom";

const LoginScreen = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        rememberMe: false,
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
    // Load stored email on component mount
    useEffect(() => {
        const storedjwt = localStorage.getItem("jwtToken");
        const rememberMe = localStorage.getItem("rememberMe");
        if (storedjwt && rememberMe == 'true') {
            handleHome();
        }
    }, []);

    const handleInputChange = (e) => {
      const { name, type, checked, value } = e.target;
      setFormData((prevData) => ({
          ...prevData,
          [name]: type === 'checkbox' ? checked : value
      }));
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

            const result = await response.json();
            if (response.ok) {
                if (result.token) {
                    localStorage.setItem('jwtToken', result.token);
                    localStorage.setItem('rememberMe', formData.rememberMe);
                    handleHome();
                } else {
                    alert('Login failed: ' + (result.errors || 'Invalid input'));
                }
            } else {
                alert(result.errors);
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
                    <div className="form-row">
                        <label className="remember-me">
                            <input
                                type="checkbox"
                                name="rememberMe"
                                checked={formData.rememberMe}
                                onChange={handleInputChange}
                            />
                            Remember Me
                        </label>
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
