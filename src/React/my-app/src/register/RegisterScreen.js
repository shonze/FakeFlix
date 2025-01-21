import React, { useState } from 'react';
import './register.css';

const RegisterScreen = () => {
    const [formData, setFormData] = useState({
        fullName: '',
        username: '',
        email: '',
        birthdate: '',
        password: '',
        confirmPassword: '',
        profilePicture: null,
    });

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

        const { password, confirmPassword, birthdate } = formData;

        // Validate passwords
        if (password.length < 8) {
            alert('Passwords should be at least 8 characters long.');
            return;
        }
        if (password !== confirmPassword) {
            alert('Passwords do not match. Please try again.');
            return;
        }

        // Validate birthdate
        if (
            Date.parse(birthdate) > Date.now() ||
            Date.parse(birthdate) < Date.parse('1900-01-01')
        ) {
            alert('Birthdate is not valid.');
            return;
        }

        // Create form data for file upload
        const data = new FormData();
        Object.keys(formData).forEach((key) => {
            if (formData[key] && key !== 'confirmPassword') {
                data.append(key, formData[key]);
            }
        });

        try {
            const response = await fetch('http://localhost:3000/api/users', {
                method: 'POST',
                body: data,
            });

            if (response.ok) {
                const result = await response.json();

                if (result.token) {
                    localStorage.setItem('jwtToken', result.token);
                    alert('Registration successful!');
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
        <div className="container vh-100 d-flex justify-content-center align-items-center">
            <div className="bg-dark p-4 rounded shadow-lg w-50 bg-opacity-75">
                <h1 className="text-danger fw-bold mb-4 text-center">Fakeflix</h1>
                <form onSubmit={handleSubmit} className="register-form">
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label htmlFor="fullName" className="form-label text-light">
                                Full Name
                            </label>
                            <input
                                type="text"
                                id="fullName"
                                name="fullName"
                                className="form-control"
                                placeholder="Enter your full name"
                                value={formData.fullName}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label htmlFor="username" className="form-label text-light">
                                Username
                            </label>
                            <input
                                type="text"
                                id="username"
                                name="username"
                                className="form-control"
                                placeholder="Enter your username"
                                value={formData.username}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label htmlFor="email" className="form-label text-light">
                                Email Address
                            </label>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                className="form-control"
                                placeholder="Enter your email"
                                value={formData.email}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label htmlFor="birthdate" className="form-label text-light">
                                Birthdate (Optional)
                            </label>
                            <input
                                type="date"
                                id="birthdate"
                                name="birthdate"
                                className="form-control"
                                value={formData.birthdate}
                                onChange={handleInputChange}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-md-6 mb-3">
                            <label htmlFor="password" className="form-label text-light">
                                Password
                            </label>
                            <input
                                type="password"
                                id="password"
                                name="password"
                                className="form-control"
                                placeholder="**********"
                                value={formData.password}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div className="col-md-6 mb-3">
                            <label htmlFor="confirmPassword" className="form-label text-light">
                                Confirm Password
                            </label>
                            <input
                                type="password"
                                id="confirmPassword"
                                name="confirmPassword"
                                className="form-control"
                                placeholder="**********"
                                value={formData.confirmPassword}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="profilePicture" className="form-label text-light">
                            Profile Picture (Optional)
                        </label>
                        <input
                            type="file"
                            id="profilePicture"
                            name="profilePicture"
                            className="form-control"
                            accept="image/*"
                            onChange={handleFileChange}
                        />
                    </div>
                    <button type="submit" className="btn btn-danger w-100">
                        Register
                    </button>
                </form>
                <div className="text-center mt-4">
                    <p className="text-light">
                        Already have an account?{' '}
                        <a href="/login" className="text-danger text-decoration-none">
                            Sign in here
                        </a>
                    </p>
                </div>
            </div>
        </div>
    );
};

export default RegisterScreen;
