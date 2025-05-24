document.getElementById("signupForm").addEventListener("submit", async function(event) {
    event.preventDefault(); // Prevent the form from submitting

    // Get values from the form
    const name = document.getElementById("name").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirmPassword").value;

    // Simple validation
    if (name === "" || email === "" || password === "" || confirmPassword === "") {
        alert("Please fill in all fields.");
        return;
    }

    if (password !== confirmPassword) {
        alert("Passwords do not match.");
        return;
    }

    // Send signup data to backend
    try {
        const adminUsername = 'admin user';
        const adminPassword = 'pass3';
        const basicAuth = 'Basic ' + btoa(adminUsername + ':' + adminPassword);
        const response = await fetch('http://localhost:8080/api/users/post', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': basicAuth
            },
            body: JSON.stringify({
                username: name,
                email: email,
                password: password
            })
        });
        if (response.ok) {
            alert("Account created successfully!");
            window.location.href = "login.html";
        } else {
            const error = await response.text();
            alert("Signup failed: " + error);
        }
    } catch (err) {
        alert("Signup failed: " + err.message);
    }
});
