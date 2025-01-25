import React, { useState, useEffect , useRef} from 'react';
import './Register.css';
import Field from './Field';
import { useNavigate } from "react-router-dom";

const RegisterScreen = () => {
    const navigate = useNavigate(); // Hook for programmatic navigation

    const fileInputRef = useRef(null);

    const [formData, setFormData] = useState({
        fullName: '',
        username: '',
        email: '',
        birthdate: '',
        password: '',
        confirmPassword: '',
        files: null,
    });

    const [preview, setPreview] = useState(null); // State for the image preview

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

    const handlePosting = async () => {
        let path=null;
        let url=null;

        const data = {
            fullName: formData.fullName,
            username: formData.username,
            email: formData.email,
            birthdate: formData.birthdate,
            password: formData.password,
            photoPath: path,
            photoUrl: url
        };

        try {
            const response = await fetch('http://localhost:3000/api/users', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data),
            });
            const result = await response.json();
            if (response.ok) {

                if (result.token) {
                    localStorage.setItem('jwtToken', result.token);

                    if (formData.files) {
                        const formDataToSend = new FormData();
                        formDataToSend.append("files", formData.files);
                        try {
                            const response2 = await fetch('http://localhost:3000/api/upload', {
                                method: 'POST',
                                body: formDataToSend, // Use FormData as the body
                            });
                            const result2 = await response2.json();
                            if (response2.ok) {
                                url = result2.files[0].url;
                                path = result2.files[0].path;
                                showToast('Profile picture uploaded successfully', 'success');
                            } else {
                                showToast('Profile picture upload failed: ' + result2.message, 'error');
                            }
                        } catch (error) {
                            console.error('Error uploading profile picture:', error);
                            showToast('An error occurred while uploading the profile picture.', 'error');
                        }
                    }
                    showToast('Account created successfully', 'success');
                    handleHome();
                } else {
                    showToast('Registration failed: ' + (result.errors || 'Invalid input'), 'error');
                }
            } else {
                showToast(result.errors, 'error');
            }
        } catch (error) {
            showToast('An error occurred: ' + error.message, 'error');
        }
    };

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
        const file = e.target.files[0];
        if (file) {
            const validImageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
            if (!validImageTypes.includes(file.type)) {
                showToast('Please upload a valid image file (JPEG, PNG, GIF, WEBP).', 'error');
                handleRemovePhoto();
                return;
            }
            setFormData((prevData) => ({
                ...prevData,
                files: file,
            }));
            setPreview(URL.createObjectURL(file)); // Generate the preview URL
        } else {
            setPreview(null); // Clear preview if no file is selected
        }
    };

    const handleRemovePhoto = () => {
        setFormData((prevData) => ({
            ...prevData,
            files: null, // Clear the selected file
        }));
        setPreview(null); // Clear the image preview
    
        if (fileInputRef.current) {
            fileInputRef.current.value = ''; // Reset the file input value
        }
    };
    

    const handleSubmit = async (e) => {
        e.preventDefault();
        const { password, confirmPassword, birthdate } = formData;

        if (password.length < 8) {
            showToast('Passwords should be at least 8 characters long.', 'error');
            return;
        }
        if (password !== confirmPassword) {
            showToast('Passwords do not match. Please try again.', 'error');
            return;
        }

        if (birthdate && (
            Date.parse(birthdate) > Date.now() ||
            Date.parse(birthdate) < Date.parse('1900-01-01')
        )) {
            showToast('Birthdate is not valid.', 'error');
            return;
        }
        
        handlePosting();
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
                                name="files"
                                className="custom-input"
                                onChange={handleFileChange}
                                accept="image/*"
                                ref={fileInputRef}
                            />
                            {preview && (
                                <div className="image-preview">
                                    <img src={preview} alt="Profile Preview" className="preview-image" />
                                    <button className="remove-photo-btn" onClick={handleRemovePhoto}>
                                        ✖
                                    </button>
                                </div>
                            )}

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
