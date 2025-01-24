import React, { useState, useEffect } from 'react';
import './Register.css';
import Field from './Field';
import { useNavigate } from "react-router-dom";

const RegisterScreen = () => {
    const navigate = useNavigate(); // Hook for programmatic navigation

    const [formData, setFormData] = useState({
        fullName: '',
        username: '',
        email: '',
        birthdate: '',
        password: '',
        confirmPassword: '',
        profilePicture: null,
    });

    // Load stored email on component mount
    useEffect(() => {
        const storedEmail = localStorage.getItem("email");
        if (storedEmail) {
            setFormData((prevData) => ({
                ...prevData,
                email: storedEmail,
            }));
        }
    }, []);

    const handleUpload = async () => {
        const data2 = {
            profilePicture: formData.profilePicture
        };
        if (formData.profilePicture) {
            console.log('File to upload:', formData.profilePicture);
            const response2 = await fetch('http://localhost:3000/api/video', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data2),
            });
            const result2 = await response2.json();
            if (response2.ok) {
                alert('Profile picture uploaded successfully');
            } else {
                alert('Profile picture upload failed: ' + result2.message);
            }
        } else {
          alert('Please select a file first.');
        }
      };

    // const handleUpload = async () => {
    //     if (formData.profilePicture) {
    //         const formDataToSend = new FormData();
    //         formDataToSend.append("profilePicture", formData.profilePicture);
    
    //         try {
    //             const response2 = await fetch('http://localhost:3000/api/video', {
    //                 method: 'POST',
    //                 body: formDataToSend, // Use FormData as the body
    //             });
    //             const result2 = await response2.json();
    //             if (response2.ok) {
    //                 alert('Profile picture uploaded successfully');
    //             } else {
    //                 alert('Profile picture upload failed: ' + result2.message);
    //             }
    //         } catch (error) {
    //             console.error('Error uploading profile picture:', error);
    //             alert('An error occurred while uploading the profile picture.');
    //         }
    //     } else {
    //         alert('Please select a file first.');
    //     }
    // };

    const handleSignIn = () => {
        navigate("../login");
    };

    const handleHome = () => {
        navigate("../home");
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
        const { password, confirmPassword, birthdate, profilePicture } = formData;

        if (password.length < 8) {
            alert('Passwords should be at least 8 characters long.');
            return;
        }
        if (password !== confirmPassword) {
            alert('Passwords do not match. Please try again.');
            return;
        }

        if (birthdate && (
            Date.parse(birthdate) > Date.now() ||
            Date.parse(birthdate) < Date.parse('1900-01-01')
        )) {
            alert('Birthdate is not valid.');
            return;
        }

        const data = {
            fullName: formData.fullName,
            username: formData.username,
            email: formData.email,
            birthdate: formData.birthdate,
            password: formData.password,
            profilePicture: formData.profilePicture
        };

        try {
            const response = await fetch('http://localhost:3000/api/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });
            const result = await response.json();
            if (response.ok) {
                handleUpload();
                if (result.token) {
                    localStorage.setItem('jwtToken', result.token);
                    handleHome();
                } else {
                    alert('Registration failed: ' + (result.errors || 'Invalid input'));
                }
            } else {
                alert(result.errors);
            }
        } catch (error) {
            alert('An error occurred: ' + error.message);
        }
    };

    return (
        <div className="register-container">
            <div className="form-container">
                <h1 className="app-title">Fakeflix</h1>
                <form onSubmit={handleSubmit}>
                    <div className="form-row">
                        <Field  
                            label="Full Name"
                            name="fullName"
                            value={formData.fullName}
                            onChange={handleInputChange}
                            type="text"
                            required
                        />
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
                            label="Email Address"
                            name="email"
                            value={formData.email}
                            onChange={handleInputChange}
                            type="email"
                            required
                        />
                        <Field
                            label="Birthdate (Optional)"
                            name="birthdate"
                            value={formData.birthdate}
                            onChange={handleInputChange}
                            type="date"
                            not-required
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
                        <Field
                            label="Confirm Password"
                            name="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleInputChange}
                            type="password"
                            required
                        />
                    </div>

                    <div className="form-row">
                        <div className="form-column">
                            <label className="form-label">Profile Picture (Optional)</label>
                            <input
                                type="file"
                                name="profilePicture"
                                className="custom-input"
                                onChange={handleFileChange}
                                accept="image/*"
                            />
                        </div>
                    </div>

                    <button type="submit" className="register-btn">
                        Register
                    </button>

                    <div className="login-text">
                        <p>
                            Already have an account?{' '}
                            <a
                            href="/login"
                            className="login-link"
                            onClick={(e) => {
                                e.preventDefault(); // Prevent default navigation
                                handleSignIn(); // Call the function
                            }}
                            >
                            Sign in here
                            </a>
                        </p>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RegisterScreen;
