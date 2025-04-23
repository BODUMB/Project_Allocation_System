document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.querySelector(".logout-btn");
    const studentForm = document.getElementById("studentForm");
    const studentList = document.getElementById("studentList");
    const studentCount = document.getElementById("studentCount");
    const facultyForm = document.getElementById("FacultyForm");
    const facultyList = document.getElementById("FacultyList");
    const facultyCount = document.getElementById("FacultyCount");
    const showStudentFormButton = document.getElementById("showStudentForm");
    const showFacultyFormButton = document.getElementById("showFacultyForm");
    const showManageGroupsButton = document.getElementById("showManageGroups");
    const addStudentSection = document.getElementById("addStudent");
    const addFacultySection = document.getElementById("addFaculty");
    const manageGroupsSection = document.getElementById("manageGroups");
    const defaultContent = document.getElementById("defaultContent");
    const reallocateGroupForm = document.getElementById("reallocateGroupForm");
    const groupList = document.getElementById("groupList");
    const groupCount = document.getElementById("groupCount");
    const apiBaseUrl = "http://localhost:5353/admin"; // Change this to match your API base URL
    
    let students = [];
    let faculties = [];
    let groups = [];

    // Load initial data
    fetchStudents();
    fetchFaculties();

    logoutButton.addEventListener("click", function () {
        alert("Logging out...");
        window.location.href = "index.html";
    });

    showStudentFormButton.addEventListener("click", function (event) {
        event.preventDefault();
        addStudentSection.style.display = "block";
        addFacultySection.style.display = "none";
        manageGroupsSection.style.display = "none";
        defaultContent.style.display = "none";
    });

    showFacultyFormButton.addEventListener("click", function (event) {
        event.preventDefault();
        addFacultySection.style.display = "block";
        addStudentSection.style.display = "none";
        manageGroupsSection.style.display = "none";
        defaultContent.style.display = "none";
    });

    showManageGroupsButton.addEventListener("click", function (event) {
        event.preventDefault();
        manageGroupsSection.style.display = "block";
        addStudentSection.style.display = "none";
        addFacultySection.style.display = "none";
        defaultContent.style.display = "none";
        fetchAllGroups();
        populateFacultyDropdown();
    });

    studentForm.addEventListener("submit", function (event) {
        event.preventDefault();
        
        const name = document.getElementById("studentName").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;
        const role = "STUDENT"; // Assuming role is fixed for students
        const groupId = document.getElementById("studentGroupId")?.value || null;
        
        if (name === "" || password === "" || email === "") {
            alert("Please fill in all required fields");
            return;
        }
        
        const student = { 
            name,
            email,
            password, 
            role,
            groupId: groupId ? parseInt(groupId) : null
        };
        
        // Send to the backend API
        registerStudent(student);
    });

    facultyForm.addEventListener("submit", function (event) {
        event.preventDefault();
        
        const name = document.getElementById("FacultyName").value;
        const email = document.getElementById("FacultyEmail").value;
        const password = document.getElementById("FacultyPassword").value;
        const role = "FACULTY"; // Assuming role is fixed for faculties
        
        if (name === "" || password === "" || email === "") {
            alert("Please fill in all required fields");
            return;
        }
        
        const faculty = { 
            name,
            email,
            password,
            role
        };
        
        // Send to the backend API
        registerFaculty(faculty);
    });

    // Add event listener for the reallocate group form
    if (reallocateGroupForm) {
        reallocateGroupForm.addEventListener("submit", function(event) {
            event.preventDefault();
            
            const groupId = document.getElementById("groupId").value;
            const newFacultyId = document.getElementById("newFacultyId").value;
            
            if (!groupId || !newFacultyId) {
                alert("Please fill in all required fields");
                return;
            }
            
            reallocateGroup(groupId, newFacultyId);
        });
    }

    // Fetch students from backend
    function fetchStudents() {
        fetch(`${apiBaseUrl}/getStudents`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch students');
                }
                return response.json();
            })
            .then(data => {
                students = data;
                updateStudentList();
            })
            .catch(error => {
                console.error("Error fetching students:", error);
                alert("Failed to load students data. Please try again later.");
            });
    }

    // Fetch faculties from backend
    function fetchFaculties() {
        fetch(`${apiBaseUrl}/faculties`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch faculties');
                }
                return response.json();
            })
            .then(data => {
                faculties = data;
                updateFacultyList();
                populateFacultyDropdown(); // Update faculty dropdown when fetching faculties
            })
            .catch(error => {
                console.error("Error fetching faculties:", error);
                alert("Failed to load faculty data. Please try again later.");
            });
    }

    // Fetch all groups
    function fetchAllGroups() {
        fetch(`${apiBaseUrl}/getGroups`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch groups');
                }
                return response.json();
            })
            .then(data => {
                groups = data;
                updateGroupList();
            })
            .catch(error => {
                console.error("Error fetching groups:", error);
                alert("Failed to load group data. Please try again later.");
            });
    }

    // Populate faculty dropdown for group reallocation
    function populateFacultyDropdown() {
        const facultyDropdown = document.getElementById("newFacultyId");
        if (facultyDropdown) {
            // Clear existing options
            facultyDropdown.innerHTML = '';
            
            // Add default empty option
            const defaultOption = document.createElement("option");
            defaultOption.value = "";
            defaultOption.textContent = "-- Select Faculty --";
            facultyDropdown.appendChild(defaultOption);
            
            // Add options for each faculty
            faculties.forEach(faculty => {
                const option = document.createElement("option");
                option.value = faculty.facultyID;
                option.textContent = `${faculty.name} (ID: ${faculty.facultyID})`;
                facultyDropdown.appendChild(option);
            });
        }
    }

    // Register a new student
    function registerStudent(student) {
        fetch(`${apiBaseUrl}/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(student)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to register student');
            }
            return response.json();
        })
        .then(data => {
            students.push(data);
            updateStudentList();
            studentForm.reset();
            alert("Student registered successfully!");
        })
        .catch(error => {
            console.error("Error registering student:", error);
            alert("Failed to register student. Please try again.");
        });
    }

    // Register a new faculty
    function registerFaculty(faculty) {
        fetch(`${apiBaseUrl}/register`, {  // Using the same endpoint assuming your API handles different roles
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(faculty)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to register faculty');
            }
            return response.json();
        })
        .then(data => {
            faculties.push(data);
            updateFacultyList();
            facultyForm.reset();
            alert("Faculty registered successfully!");
        })
        .catch(error => {
            console.error("Error registering faculty:", error);
            alert("Failed to register faculty. Please try again.");
        });
    }

    // Get groups for a specific faculty
    function getGroupsByFaculty(facultyId) {
        fetch(`${apiBaseUrl}/getGroups/${facultyId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Faculty groups not found');
                }
                return response.json();
            })
            .then(data => {
                // Display the faculty's groups in the group management section
                manageGroupsSection.style.display = "block";
                addStudentSection.style.display = "none";
                addFacultySection.style.display = "none";
                defaultContent.style.display = "none";
                
                groups = data;
                updateGroupList();
                
                // Pre-select the faculty in the dropdown
                const facultyDropdown = document.getElementById("newFacultyId");
                if (facultyDropdown) {
                    facultyDropdown.value = facultyId;
                }
            })
            .catch(error => {
                console.error("Error fetching faculty groups:", error);
                alert("Failed to load faculty groups. Please try again later.");
            });
    }

    // Reallocate a group to a different faculty
    async function reallocateGroup(e) {
        e.preventDefault();
    
        const groupId = parseInt(document.getElementById("groupId").value);
        const newFacultyId = parseInt(document.getElementById("newFacultyId").value);
    
        console.log("GroupId:", groupId, "newFacultyId:", newFacultyId);
    
        if (isNaN(groupId) || isNaN(newFacultyId)) {
            alert("Please fill in all required fields");
            return;
        }
    
        try {
            const response = await fetch("http://localhost:5353/admin/reallocateGroup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    GroupId: groupId,
                    newFacultyId: newFacultyId
                })
            });
    
            const result = await response.text();
            alert(result);
        } catch (err) {
            console.error("Error reallocating group:", err);
            alert("An error occurred while reallocating the group.");
        }
    }
    
    document.getElementById("reallocateGroupForm").addEventListener("submit", reallocateGroup);
    
    

    // Create a new group
    function createGroup(groupData) {
        fetch(`${apiBaseUrl}/createGroup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(groupData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to create group');
            }
            return response.json();
        })
        .then(data => {
            groups.push(data);
            updateGroupList();
            document.getElementById("createGroupForm").reset();
            alert("Group created successfully!");
        })
        .catch(error => {
            console.error("Error creating group:", error);
            alert("Failed to create group. Please try again.");
        });
    }

    function updateStudentList() {
        studentList.innerHTML = "";
        students.forEach((student, index) => {
            let li = document.createElement("li");
            li.textContent = `${index + 1}. ${student.name} - ${student.email}`;
            
            // Add action buttons if needed
            let actionDiv = document.createElement("div");
            actionDiv.className = "student-actions";
            
            // Example: delete button
            let deleteBtn = document.createElement("button");
            deleteBtn.textContent = "Delete";
            deleteBtn.className = "delete-btn";
            deleteBtn.onclick = () => deleteStudent(student.id);
            
            actionDiv.appendChild(deleteBtn);
            li.appendChild(actionDiv);
            
            studentList.appendChild(li);
        });
        studentCount.textContent = students.length;
    }

    function updateFacultyList() {
        facultyList.innerHTML = "";
        faculties.forEach((faculty, index) => {
            let li = document.createElement("li");
            li.textContent = `${index + 1}. ${faculty.name} - ${faculty.email}`;
            
            // Add action buttons
            let actionDiv = document.createElement("div");
            actionDiv.className = "faculty-actions";
            
            // View groups button
            let viewGroupsBtn = document.createElement("button");
            viewGroupsBtn.textContent = "View Groups";
            viewGroupsBtn.className = "view-groups-btn";
            viewGroupsBtn.onclick = () => getGroupsByFaculty(faculty.id);
            
            // Delete button
            let deleteBtn = document.createElement("button");
            deleteBtn.textContent = "Delete";
            deleteBtn.className = "delete-btn";
            deleteBtn.onclick = () => deleteFaculty(faculty.id);
            
            actionDiv.appendChild(viewGroupsBtn);
            actionDiv.appendChild(deleteBtn);
            li.appendChild(actionDiv);
            
            facultyList.appendChild(li);
        });
        facultyCount.textContent = faculties.length;
    }

    function updateGroupList() {
        if (groupList) {
            groupList.innerHTML = "";
            groups.forEach((group, index) => {
                let li = document.createElement("li");
                li.className = "group-item";
                
                // Create group info div
                let groupInfo = document.createElement("div");
                groupInfo.className = "group-info";
                groupInfo.innerHTML = `
                    <h4>Group ${group.groupId}</h4>
                    <h5>Group Name :  ${group.groupName}
                    <p>Faculty ID: ${group.facultyId || "Not Assigned"}</p>
                    <p>Members: ${group.members || 0}</p>
                `;
                
                // Create action buttons div
                let actionDiv = document.createElement("div");
                actionDiv.className = "group-actions";
                
                // Select group button
                let selectBtn = document.createElement("button");
                selectBtn.textContent = "Select";
                selectBtn.className = "select-group-btn";
                selectBtn.onclick = () => {
                    document.getElementById("groupId").value = group.id;
                    window.scrollTo({
                        top: document.getElementById("reallocateGroupForm").offsetTop - 20,
                        behavior: "smooth"
                    });
                };
                
                // Delete group button
                let deleteBtn = document.createElement("button");
                deleteBtn.textContent = "Delete";
                deleteBtn.className = "delete-btn";
                deleteBtn.onclick = () => deleteGroup(group.id);
                
                actionDiv.appendChild(selectBtn);
                actionDiv.appendChild(deleteBtn);
                
                // Append to li
                li.appendChild(groupInfo);
                li.appendChild(actionDiv);
                
                groupList.appendChild(li);
            });
            
            if (groupCount) {
                groupCount.textContent = groups.length;
            }
        }
    }

    // Delete a student
    function deleteStudent(studentId) {
        if (confirm("Are you sure you want to delete this student?")) {
            fetch(`${apiBaseUrl}/students/${studentId}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete student');
                }
                students = students.filter(s => s.id !== studentId);
                updateStudentList();
                alert("Student deleted successfully!");
            })
            .catch(error => {
                console.error("Error deleting student:", error);
                alert("Failed to delete student. Please try again.");
            });
        }
    }

    // Delete a faculty
    function deleteFaculty(facultyId) {
        if (confirm("Are you sure you want to delete this faculty?")) {
            fetch(`${apiBaseUrl}/faculties/${facultyId}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete faculty');
                }
                faculties = faculties.filter(f => f.id !== facultyId);
                updateFacultyList();
                alert("Faculty deleted successfully!");
            })
            .catch(error => {
                console.error("Error deleting faculty:", error);
                alert("Failed to delete faculty. Please try again.");
            });
        }
    }

    // Delete a group
    function deleteGroup(groupId) {
        if (confirm("Are you sure you want to delete this group?")) {
            fetch(`${apiBaseUrl}/groups/${groupId}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to delete group');
                }
                groups = groups.filter(g => g.id !== groupId);
                updateGroupList();
                alert("Group deleted successfully!");
            })
            .catch(error => {
                console.error("Error deleting group:", error);
                alert("Failed to delete group. Please try again.");
            });
        }
    }
});