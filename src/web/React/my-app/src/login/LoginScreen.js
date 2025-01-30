import React, { useState, useEffect,useCallback  } from 'react';
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
    
    // const handleHome = () => {
    //     navigate("../home");
    //   };
    // // Load stored email on component mount
    // useEffect(() => {
    //     const storedjwt = localStorage.getItem("jwtToken");
    //     const rememberMe = localStorage.getItem("rememberMe");
    //     if (storedjwt && rememberMe === 'true') {
    //         handleHome();
    //     }
    // }, []);


  const handleHome = useCallback(() => {
    navigate("../home");
  }, [navigate]); // Dependency: navigate is stable from React Router

  useEffect(() => {
    const storedjwt = localStorage.getItem("jwtToken");
    const rememberMe = localStorage.getItem("rememberMe");
    if (storedjwt && rememberMe === "true") {
      handleHome();
    }
  }, [handleHome]); // Add handleHome as a dependency

    const handleInputChange = (e) => {
      const { name, type, checked, value } = e.target;
      setFormData((prevData) => ({
          ...prevData,
          [name]: type === 'checkbox' ? checked : value
      }));
  };
  

    // const handleFileChange = (e) => {
    //     setFormData((prevData) => ({
    //         ...prevData,
    //         profilePicture: e.target.files[0],
    //     }));
    // };
    
    function showToast(message, type) {
      // Create a container for the toast messages if it doesn't exist
      let toastContainer = document.querySelector('.toast-container');
      if (!toastContainer) {
        toastContainer = document.createElement('div');
        toastContainer.className = 'toast-container';
        document.body.appendChild(toastContainer);
      }
  
      // Create the toast element
      const toast = document.createElement('div');
      toast.className = type === 'success' ? 'success-toast' : 'error-toast';
      toast.textContent = message;
  
      // Add the toast to the container
      toastContainer.appendChild(toast);
  
      // Remove the toast after 4 seconds
      setTimeout(() => {
        toast.remove();
      }, 4000);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        // const { password } = formData; ???

        const data = {
            username: formData.username,
            password: formData.password
        };

        try {
            const response = await fetch(`http://localhost:${process.env.REACT_APP_PORT}/api/tokens`, {
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
                    showToast('Login failed: ' + (result.errors || 'Invalid input'), 'error');
                }
            } else {
                showToast(result.errors, 'error');
            }
        } catch (error) {
            showToast('An error occurred: ' + error.message, 'error');
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
                                class="custom-checkbox"
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
