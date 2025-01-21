document.getElementById("loginForm").addEventListener("submit", async function (event) {
    event.preventDefault(); // Prevent default form submission

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("http://localhost:3000/api/tokens", { // Corrected the URL
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const data = await response.json();

            if (data.token) { // Check if a token is returned
                // Store the token in localStorage or sessionStorage
                localStorage.setItem("jwtToken", data.token);

                alert("Login successful!");
                window.location.href = "./homepage.html"; // Redirect to Fakeflix on success
            } else {
                alert("Login failed: " + (data.message || "Invalid credentials"));
            }
        } else {
            alert("Error: Unable to connect to the server.");
        }
    } catch (error) {
        alert("An error occurred: " + error.message);
    }
});
