document.getElementById("registrationForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Prevent default form submission

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const birthdate = document.getElementById("birthdate").value || null;
    const profilePictureInput = document.getElementById("profilePicture");

    // Validate passwords match
    if (password !== confirmPassword) {
        alert("Passwords do not match. Please try again.");
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
        const response = await fetch("http://localhost:3000/api/register", {
            method: "POST",
            body: formData,
        });

        if (response.ok) {
            const data = await response.json();
            if (data.success) {
                alert("Registration successful!");
                window.location.href = "./login.html"; // Redirect to login page
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
