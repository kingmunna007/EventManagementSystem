document.getElementById("loginForm").addEventListener("submit", async function(event) {
    event.preventDefault(); // Prevent the form from submitting

    // Get values from the form
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    // Simple validation
    if (email === "" || password === "") {
        alert("Please fill in both fields.");
        return;
    }

    // Send login data to backend (POST with JSON body)
    try {
        // Add Basic Auth header (admin user/pass3)
        const basicAuth = 'Basic ' + btoa('admin user:pass3');
        const res = await fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': basicAuth
            },
            body: JSON.stringify({ email, password })
        });
        const text = await res.text();
        if (res.ok) {
            // Try to extract userId and role from response if available, else prompt for it
            let userId = null;
            let userRole = null;
            try {
                const json = JSON.parse(text);
                userId = json.id || json.userId;
                userRole = json.role || json.userRole;
            } catch (e) {
                // If not JSON, fallback
            }
            if (!userId) {
                userId = prompt('Enter your user ID (for demo, backend should return this):');
            }
            if (userId) localStorage.setItem('userId', userId);
            if (userRole === 'ADMIN') {
                alert("Admin login successful!");
                window.location.href = "admin-dashboard.html";
            } else {
                alert("Login successful!");
                window.location.href = "home.html";
            }
        } else {
            alert("Login failed: " + text);
        }
    } catch (err) {
        alert("Login failed: " + err.message);
    }
});

// Optional: Forgot Password link
document.getElementById("forgotPassword").addEventListener("click", function(event) {
    event.preventDefault();
    alert("Password recovery feature is coming soon.");
});
