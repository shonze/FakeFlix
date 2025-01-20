// Get a reference to the email input field
const emailField = document.getElementById("email");

// Retrieve the email from localStorage and populate the field
const storedEmail = localStorage.getItem("email");
if (storedEmail) {
  emailField.value = storedEmail;
}

document.getElementById("registrationForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Prevent default form submission

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const birthdate = document.getElementById("birthdate").value || null;
    const profilePictureInput = document.getElementById("profilePicture");

    // Validate passwords match
    if (password.length < 8) {
        alert("Passwords should be at least 8 characters long.");
        return;
    }
    if (password !== confirmPassword) {
        alert("Passwords do not match. Please try again.");
        return;
    }

    if (Date.parse(birthdate) > Date.now() || Date.parse(birthdate) < Date.parse('1900-01-01')) {
        alert("Birthday not valid");
        return;
    }

    // Create form data for file upload
    const formData = new FormData();
    formData.append("username", username);
    formData.append("password", password);
    if (birthdate) formData.append("birthdate", birthdate);
    if (profilePictureInput.files.length > 0) {
        formData.append("profilePicture", profilePictureInput.files[0]);
    }

    try {
        const response = await fetch("http://localhost:3000/api/users", {
            method: "POST",
            body: formData,
        });

        if (response.ok) {
            const data = await response.json();
            
            if (data.token) { // Check if a token is returned
                // Store the token in localStorage or sessionStorage
                localStorage.setItem("jwtToken", data.token);

                alert("Registration successful!");
                window.location.href = "./homepage.html"; // Redirect to Fakeflix on success
            } else {
                alert("Registration failed: " + (data.message || "Invalid input"));
            }

        } else {
            alert("Error: Unable to connect to the server.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
});
