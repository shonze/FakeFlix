// Get references to the input field and buttons
const emailInput = document.getElementById("emailInput");
const getStartedButton = document.getElementById("getStartedBtn");
const loginButton = document.getElementById("signInBtn");
const registerButton = document.getElementById("registerBtn");

// Add click event listener to the "Sign In" button
getStartedButton.addEventListener("click", () => {
  // Save the entered email to localStorage
  const email = emailInput.value;
  if (email) {
    localStorage.setItem("email", email);
  }

  // Redirect to the login page
  window.location.href = "/register.html";
});

loginButton.addEventListener("click", () => {
    window.location.href = "/login.html";
});

registerButton.addEventListener("click", () => {
    window.location.href = "/register.html";
});