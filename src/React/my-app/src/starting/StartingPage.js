import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // Import useNavigate for navigation
import "./StartingPage.css"; // Custom CSS file for styling

const FakeflixLandingPage = () => {
  const [email, setEmail] = useState("");
  const navigate = useNavigate(); // Hook for programmatic navigation

  const handleGetStarted = () => {
    localStorage.setItem("email", email);
    navigate("/register"); // Navigate to the register page
  };

  const handleSignIn = () => {
    navigate("/login"); // Navigate to the login page
  };

  const handleRegister = () => {
    navigate("/register"); // Navigate to the register page
  };

  return (
    <div className="landing-page">
      <nav className="navbar">
        <div className="navbar-brand">Fakeflix</div>
        <div className="nav-buttons">
          <button className="btn" onClick={handleRegister}>
            Register
          </button>
          <button className="btn" onClick={handleSignIn}>
            Sign In
          </button>
        </div>
      </nav>

      <div className="content">
        <h1>Unlimited movies, TV shows, and more.</h1>
        <p>Watch anywhere. Cancel anytime.</p>
        <p>Ready to watch? Enter your email to create your membership.</p>
        <div className="form-inline">
          <input
            type="email"
            placeholder="Email address"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="email-input"
          />
          <button className="btn get-started-btn" onClick={handleGetStarted}>
            Get Started &gt;
          </button>
        </div>
      </div>
    </div>
  );
};

export default FakeflixLandingPage;
