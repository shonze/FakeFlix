import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './AdminScreen.css';

const AdminScreen = () => {
  const [newMovie, setNewMovie] = useState({
    thumbnail: null,
    video: null,
  });

  const [previewThumbnail, setPreviewThumbnail] = useState(null); // For thumbnail preview
  const [previewVideo, setPreviewVideo] = useState(null); // For video preview
  const fileInputThumbnailRef = useRef(null);
  const fileInputVideoRef = useRef(null);

  const navigate = useNavigate();

  const handleBackClick = () => {
    navigate('/home');
  };

  const handleFileChange = (e, type) => {
    const file = e.target.files[0];
    if (file) {
      if (type === 'thumbnail') {
        const validImageTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        if (!validImageTypes.includes(file.type)) {
          showToast('Please upload a valid image file (JPEG, PNG, GIF, WEBP).', 'error');
          handleRemoveFile('thumbnail');
          return;
        }
        setNewMovie((prevData) => ({ ...prevData, thumbnail: file }));
        setPreviewThumbnail(URL.createObjectURL(file));
      } else if (type === 'video') {
        const validVideoTypes = ['video/mp4', 'video/webm', 'video/ogg'];
        if (!validVideoTypes.includes(file.type)) {
          showToast('Please upload a valid video file (MP4, WEBM, OGG).', 'error');
          handleRemoveFile('video');
          return;
        }
        setNewMovie((prevData) => ({ ...prevData, video: file }));
        setPreviewVideo(URL.createObjectURL(file));
      }
    }
  };

  const handleRemoveFile = (type) => {
    if (type === 'thumbnail') {
      setNewMovie((prevData) => ({ ...prevData, thumbnail: null }));
      setPreviewThumbnail(null);
      if (fileInputThumbnailRef.current) fileInputThumbnailRef.current.value = '';
    } else if (type === 'video') {
      setNewMovie((prevData) => ({ ...prevData, video: null }));
      setPreviewVideo(null);
      if (fileInputVideoRef.current) fileInputVideoRef.current.value = '';
    }
  };

  const handleAddMovie = async (e) => {
    e.preventDefault();
    try {
      const formData = new FormData();
      // if (newMovie.thumbnail) formData.append('thumbnail', newMovie.thumbnail);
      // if (newMovie.video) formData.append('video', newMovie.video);
      formData.append('files', newMovie.video);

      const token = localStorage.getItem('jwtToken');
      const response = await fetch('http://localhost:8080/api/file', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) throw new Error('Error adding movie');
      showToast('Movie added successfully!', 'success');

      // Reset form
      setNewMovie({
        thumbnail: null,
        video: null,
      });
      setPreviewVideo(null);
      handleRemoveFile('video')
    } catch (error) {
      showToast('Error: Unable to add the movie.', 'error');
      console.error(error);
    }

  try {
      const formData = new FormData();
      // if (newMovie.thumbnail) formData.append('thumbnail', newMovie.thumbnail);
      // if (newMovie.video) formData.append('video', newMovie.video);
      formData.append('files', newMovie.thumbnail);

      const token = localStorage.getItem('jwtToken');
      const response = await fetch('http://localhost:8080/api/file', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) throw new Error('Error adding movie');
      showToast('Movie added successfully!', 'success');

      // Reset form
      setNewMovie({
        thumbnail: null,
        video: null,
      });
      setPreviewThumbnail(null);
      handleRemoveFile('thumbnail')

    } catch (error) {
      showToast('Error: Unable to add the movie.', 'error');
      console.error(error);
    }
  };

  const showToast = (message, type) => {
    let toastContainer = document.querySelector('.toast-container');
    if (!toastContainer) {
      toastContainer = document.createElement('div');
      toastContainer.className = 'toast-container';
      document.body.appendChild(toastContainer);
    }
    const toast = document.createElement('div');
    toast.className = type === 'success' ? 'success-toast' : 'error-toast';
    toast.textContent = message;
    toastContainer.appendChild(toast);
    setTimeout(() => toast.remove(), 4000);
  };

  return (
    <div className="container">
      <button className="back-button" onClick={handleBackClick}>
        &#8592; Back
      </button>
      <h1>Admin Screen</h1>
      <form onSubmit={handleAddMovie} className="add-movie">
        <h2>Add Movie</h2>

        <label className="form-label">Thumbnail</label>
        <input
          type="file"
          className="custom-input"
          onChange={(e) => handleFileChange(e, 'thumbnail')}
          accept="image/*"
          ref={fileInputThumbnailRef}
        />
        {previewThumbnail && (
          <div className="image-preview">
            <img src={previewThumbnail} alt="Thumbnail Preview" />
            <button
              type="button"
              className="remove-photo-btn"
              onClick={() => handleRemoveFile('thumbnail')}
            >
              ✖
            </button>
          </div>
        )}

        <label className="form-label">Video</label>
        <input
          type="file"
          className="custom-input"
          onChange={(e) => handleFileChange(e, 'video')}
          accept="video/*"
          ref={fileInputVideoRef}
        />
        {previewVideo && (
          <div className="video-preview">
            <video src={previewVideo} controls width="300" />
            <button
              type="button"
              className="remove-photo-btn"
              onClick={() => handleRemoveFile('video')}
            >
              ✖
            </button>
          </div>
        )}

        <button type="submit">Add Movie</button>
      </form>
    </div>
  );
};

export default AdminScreen;
