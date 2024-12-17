document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the form from submitting

    // Get values from the form
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Simple validation
    if (email === "" || password === "") {
        alert("Please fill in both fields.");
        return;
    }

    // Simulate login (replace with real authentication logic)
    if (email === "user@example.com" && password === "password123") {
        alert("Login successful!");
        // Redirect to another page or handle login success
    } else {
        alert("Invalid email or password.");
    }
});

// Optional: Forgot Password link
document.getElementById("forgotPassword").addEventListener("click", function(event) {
    event.preventDefault();
    alert("Password recovery feature is coming soon.");
});
