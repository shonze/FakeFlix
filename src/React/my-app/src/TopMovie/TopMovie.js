import './TopMovie.css';
import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
const TopMovie = ({ id }) => {
    const [movie, setMovie] = useState([])
    const videoRef = useRef(null);
    const [isVisible, setIsVisible] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const handleScroll = () => {
            // Get video element's position
            const videoElement = videoRef.current;
            if (!videoElement) return;

            const rect = videoElement.getBoundingClientRect();

            const visibleHeight = Math.min(
                rect.bottom,
                window.innerHeight
            ) - Math.max(rect.top, 0);

            // Check if less than half of the video is visible
            const isHalfVisible = visibleHeight >= (rect.height * 3.5 / 6);

            if (!isHalfVisible) {
                videoElement.pause();
                setIsVisible(false);
            } else {
                videoElement.play();
                setIsVisible(true);
            }
        };

        // Add scroll event listener
        window.addEventListener('scroll', handleScroll);
        window.addEventListener('resize', handleScroll);

        // Initial check
        handleScroll();

        const fetchMovie = async () => {
            try {
                if (!id) {
                    return;
                }
                const token = localStorage.getItem('jwtToken');

                const response = await fetch(`http://localhost:8080/api/movies/${id}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status} ${response.error}`);
                }

                const data = await response.json();
                setMovie(data);
            } catch (error) {
                console.error("Error fetching movie:", error);
            }
        };
        fetchMovie();
    }, [id]);

    return (
        <div className="video-container">
            <video ref={videoRef} className="full-movie" autoPlay={isVisible} muted loop>
                <source src="http://localhost:3000/short.mp4" type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <div className="text-overlay position-absolute top-50 start-0 translate-middle-y text-white">
                <h1 className="display-3">{movie.title}</h1>
                <p className="lead">{movie.description}</p>
                <button
                    onClick={() => { navigate('/watch-movie', { state: { movie } }) }}
                >Play</button>
            </div>
        </div>
    );
};

export default TopMovie;