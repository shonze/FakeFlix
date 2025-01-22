import React, { useState } from "react";
import "./StartingPage.css"; // Custom CSS file for styling

const FakeflixLandingPage = () => {
  const [email, setEmail] = useState("");

  const handleGetStarted = () => {
    if (email) {
      localStorage.setItem("email", email);
      window.location.href = "/register.html"; // Redirect to register page
    }
  };

  const handleSignIn = () => {
    window.location.href = "/login.html"; // Redirect to login page
  };

  const handleRegister = () => {
    window.location.href = "/register.html"; // Redirect to register page
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
