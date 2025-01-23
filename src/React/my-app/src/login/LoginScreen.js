import React, { useState } from "react";
import "./LoginScreen.css";
import Field from './Field';
import { useNavigate } from "react-router-dom"; // Import useNavigate for navigation

const LoginPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({ ...prevData, [name]: value }));
    };

    const navigate = useNavigate(); // Hook for programmatic navigation
    const handleRegister = () => {
      navigate("../register"); // Navigate to the register page
    };
    const handleFileChange = (e) => {
        setFormData((prevData) => ({
            ...prevData,
            profilePicture: e.target.files[0],
        }));
    };

    const handleSubmit = async (e) => {
        // e.preventDefault();
        // const { password, confirmPassword, birthdate } = formData;

        // if (password.length < 8) {
        //     alert('Passwords should be at least 8 characters long.');
        //     return;
        // }
        // if (password !== confirmPassword) {
        //     alert('Passwords do not match. Please try again.');
        //     return;
        // }

        // if (birthdate && (
        //     Date.parse(birthdate) > Date.now() ||
        //     Date.parse(birthdate) < Date.parse('1900-01-01')
        // )) {
        //     alert('Birthdate is not valid.');
        //     return;
        // }

        // const data = new FormData();
        // Object.keys(formData).forEach((key) => {
        //     if (formData[key] && key !== 'confirmPassword') {
        //         data.append(key, formData[key]);
        //     }
        // });

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
                  window.location.href = '/homepage';
              } else {
                  alert('Registration failed: ' + (result.message || 'Invalid input'));
              }
          } else {
              alert('Error: Unable to connect to the server.');
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
                              className="login-link"
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


export default LoginPage;
