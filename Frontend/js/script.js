document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault();
    
    let userId = document.getElementById("userid").value;
    let password = document.getElementById("password").value;
    let role = document.getElementById("role").value;
    
    if (userId === "" || password === "") {
        alert("Please fill in all fields");
        return;
    }
    
    let apiUrl;
    let requestBody = {
        email: userId,
        password: password
    };
    
    if (role === "student") {
        apiUrl = "http://localhost:5353/student/login";
    } else if (role === "Faculty") {
        apiUrl = "http://localhost:5353/faculty/login";
    } else if (role === "admin") {
        apiUrl = "http://localhost:5353/admin/login"; // 🔁 Optional: create admin login API if needed
    } else {
        alert("Invalid role selected!");
        return;
    }

    // Make API call
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include', // 🔑 Enables cookie-based session tracking
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (response.ok) {
            return response.json(); // 🟢 Expecting a faculty or student object now
        } else {
            if (response.status === 401) {
                throw new Error("Invalid credentials");
            } else if (response.status === 404) {
                throw new Error("User not found");
            } else {
                throw new Error("Login failed");
            }
        }
    })
    .then(user => {
        // 🗂 Store full user info for session-based access
        sessionStorage.setItem("userInfo", JSON.stringify(user));
        sessionStorage.setItem("userRole", role);

        // ✅ Redirect based on role
        if (role === "student") {
            window.location.href = "student.html";
        } else if (role === "Faculty") {
            window.location.href = "Faculty.html";
        } else if (role === "admin") {
            window.location.href = "admin.html";
        }
    })
    .catch(error => {
        alert(error.message);
    });
});
