document.getElementById('getStartedBtn').addEventListener('click', () => {
    const email = document.getElementById('emailInput').value;

    if (validateEmail(email)) {
        alert('Thank you for getting started!');
    } else {
        alert('Please enter a valid email address.');
    }
});

// Function to validate email
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}
