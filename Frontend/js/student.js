document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.querySelector(".logout-btn");

    // Sidebar menu items
    const menuItems = document.querySelectorAll(".sidebar ul li a");

    // Sections
    const sections = {
        profile: document.getElementById("profile"),
        groupDetails: document.getElementById("groupDetails"),
        projects: document.getElementById("projects"),
        Allocation: document.getElementById("Allocation"),
        notifications: document.getElementById("notifications")
    };

    // Profile Form Elements
    const profileForm = document.getElementById("profileForm");
    const studentIdField = document.getElementById("studentId"); // Fixed Field
    const nameInput = document.getElementById("name");
    const emailInput = document.getElementById("email");
    const departmentInput = document.getElementById("department");
    const skillsInput = document.getElementById("skills");

    // Logout Functionality
    logoutButton.addEventListener("click", function () {
        alert("Logging out...");
        window.location.href = "index.html";
    });

    // Function to show a specific section and hide others
    function showSection(sectionId) {
        for (let key in sections) {
            sections[key].style.display = key === sectionId ? "block" : "none";
        }
    }

    // Add event listeners to sidebar menu items
    menuItems.forEach(item => {
        item.addEventListener("click", function (event) {
            event.preventDefault();
            const sectionId = event.target.getAttribute("data-section");
            showSection(sectionId);
        });
    });

    // Fetch Student Profile from API
    function loadProfile() {
        fetch("/api/student/profile")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch profile");
                }
                return response.json();
            })
            .then(data => {
                document.getElementById("studentId").value = data.studentId; // Fixed Field
                document.getElementById("name").value = data.name || "";
                document.getElementById("email").value = data.email || "";
                document.getElementById("department").value = data.department || "";
                document.getElementById("skills").value = data.skills || "";
            })
            .catch(error => console.error("Error fetching profile:", error));
    }

    // Save Profile Data to API
    profileForm?.addEventListener("submit", function (event) {
        event.preventDefault();

        let updatedProfile = {
            studentId: document.getElementById("studentId").value,
            name: document.getElementById("name").value.trim(),
            email: document.getElementById("email").value.trim(),
            department: document.getElementById("department").value.trim(),
            skills: document.getElementById("skills").value.trim(),
        };

        fetch("/api/student/updateProfile", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedProfile),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to update profile");
                }
                return response.json();
            })
            .then(data => {
                alert("Profile updated successfully!");
                loadProfile(); // Reload profile after update
            })
            .catch(error => console.error("Error updating profile:", error));
    });

    // Global functions for group and member management
    window.createGroup = function() {
        console.log("Create Group function called");
        let groupName = document.getElementById("groupName").value;
        
        if (groupName.trim() === "") {
            console.log("Group name is empty");
            alert("Please enter a group name");
            return;
        }

        // Show group details section
        document.getElementById("groupDetailsSection").style.display = "block";
        
        // Update group size
        document.getElementById("groupSize").innerText = "1 / 4";
        
        // Clear group name input
        document.getElementById("groupName").value = "";
        
        // Show success alert
        alert("Group created successfully");
    };

    window.searchMember = function() {
        let searchQuery = document.getElementById("searchMember").value;
        if (searchQuery.trim() === "") {
            alert("Please enter a roll number or user ID");
            return;
        }
        alert("Searching for: " + searchQuery);
    };

    window.addMember = function() {
        let table = document.getElementById("group-details-table").getElementsByTagName('tbody')[0];
        let rowCount = table.rows.length;
        
        if (rowCount >= 4) {
            alert("Group is already full");
            return;
        }
        
        let rollNo = prompt("Enter Roll No:");
        let name = prompt("Enter Name:");
        
        if (rollNo && name) {
            let newRow = table.insertRow();
            newRow.innerHTML = `<td>${rowCount + 1}</td><td>${rollNo}</td><td>${name}</td>`;
            document.getElementById("groupSize").innerText = `${rowCount + 1} / 4`;
        }
    };

    // Initialize Dashboard Data
    loadProfile();
    showSection("profile"); // Default to showing Profile section
});