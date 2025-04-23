document.addEventListener('DOMContentLoaded', function () {
    const BASE_URL = "http://localhost:5353/faculty";

    // Logout Button Handling
    const logoutButton = document.querySelector(".logout-btn");
    logoutButton.addEventListener("click", function () {
        sessionStorage.clear(); // Clear stored session info
        alert("Logging out...");
        window.location.href = "index.html";
    });

    // Try to load faculty from sessionStorage first
    const faculty = JSON.parse(sessionStorage.getItem("userInfo"));
    const role = sessionStorage.getItem("userRole");

    if (faculty && role === "Faculty") {
        displayFacultyProfile(faculty); // 🟢 Use cached info
    } else {
        loadFacultyProfileFromBackend(); // 🔄 Fallback
    }

    // Function to render profile data to HTML
    function displayFacultyProfile(facultyData) {
        document.getElementById("displayFacultyName").textContent = facultyData.name;
        document.getElementById("displayFacultyId").textContent = `FAC${facultyData.facultyID || facultyData.id}`;
        document.getElementById("displayEmail").textContent = facultyData.email;
        document.getElementById("displayExperience").textContent = facultyData.experience || "Not specified";
        document.getElementById("displayRole").textContent = facultyData.role || "Not specified";

        if (document.getElementById("displayDepartment")) {
            document.getElementById("displayDepartment").textContent = facultyData.department || "Not specified";
        }
        if (document.getElementById("displayDomain")) {
            document.getElementById("displayDomain").textContent = facultyData.domain || "Not specified";
        }
        if (document.getElementById("displayContactNumber")) {
            document.getElementById("displayContactNumber").textContent = facultyData.contactNumber || "Not specified";
        }
        if (document.getElementById("displayResearchInterests")) {
            document.getElementById("displayResearchInterests").textContent = facultyData.researchInterests || "Not specified";
        }

        document.getElementById("displaySkills").textContent = facultyData.skills || "Not specified";
    }

    // Optional: Fallback to load from backend if sessionStorage is not available
    async function loadFacultyProfileFromBackend() {
        try {
            const response = await fetch(`${BASE_URL}/profile`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Failed to fetch faculty profile');
            }

            const facultyData = await response.json();

            // Store in sessionStorage for future use
            sessionStorage.setItem("userInfo", JSON.stringify(facultyData));
            sessionStorage.setItem("userRole", "Faculty");

            displayFacultyProfile(facultyData);
        } catch (error) {
            console.error("Error loading faculty profile:", error);
            displayFallbackProfileData();
        }
    }

    // Fallback function for profile data if API fails
    function displayFallbackProfileData() {
        const facultyData = {
            name: "Dr. Rajesh Kumar",
            facultyId: "FAC2023005",
            email: "rajesh.kumar@kiit.ac.in",
            department: "Computer Science",
            domain: "Machine Learning",
            experience: 8,
            contactNumber: "+91 9876543210",
            skills: "Java, Python, Machine Learning, Data Analytics, Web Development",
            researchInterests: "Artificial Intelligence, Big Data Analytics, Computer Vision",
            role: "Faculty"
        };
        
        document.getElementById("displayFacultyName").textContent = facultyData.name;
        document.getElementById("displayFacultyId").textContent = facultyData.facultyId;
        document.getElementById("displayEmail").textContent = facultyData.email;
        document.getElementById("displayExperience").textContent = facultyData.experience;
        document.getElementById("displayRole").textContent = facultyData.role;
        
        if (document.getElementById("displayDepartment")) {
            document.getElementById("displayDepartment").textContent = facultyData.department;
        }
        
        if (document.getElementById("displayDomain")) {
            document.getElementById("displayDomain").textContent = facultyData.domain;
        }
        
        if (document.getElementById("displayContactNumber")) {
            document.getElementById("displayContactNumber").textContent = facultyData.contactNumber;
        }
        
        if (document.getElementById("displayResearchInterests")) {
            document.getElementById("displayResearchInterests").textContent = facultyData.researchInterests;
        }
        
        document.getElementById("displaySkills").textContent = facultyData.skills;
    }

    // Sidebar Navigation
    const sidebarLinks = document.querySelectorAll('.sidebar a');
    const sections = document.querySelectorAll('.section');
    const welcomeText = document.getElementById('welcomeText');

    sidebarLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const sectionName = this.getAttribute('data-section');
            
            // Hide all sections
            sections.forEach(section => {
                section.style.display = 'none';
            });
            
            // Hide welcome text
            welcomeText.style.display = 'none';
            
            // Show the selected section
            const targetSection = document.getElementById(`${sectionName}Section`);
            if (targetSection) {
                targetSection.style.display = 'block';
            }
        });
    });

    // Project Allocation Section
    const loadGroupDetailsBtn = document.getElementById('loadGroupDetailsBtn');
    if (loadGroupDetailsBtn) {
        loadGroupDetailsBtn.addEventListener('click', function() {
            const academicYear = document.getElementById('academicYear').value;
            const studentGroup = document.getElementById('studentGroup').value;
            
            if (!academicYear || !studentGroup) {
                alert('Please select both academic year and student group');
                return;
            }
            
            // Simulate loading group details
            loadGroupDetails(academicYear, studentGroup);
        });
    }

    function loadGroupDetails(academicYear, studentGroup) {
        // In a real application, this would be an API call
        // For now, simulate with dummy data
        try {
            const groupMembers = [
                { rollNo: '2105678', name: 'Rohit Sharma', department: 'Computer Science' },
                { rollNo: '2105679', name: 'Virat Kohli', department: 'Computer Science' },
                { rollNo: '2105680', name: 'Jasprit Bumrah', department: 'Electronics' },
                { rollNo: '2105681', name: 'KL Rahul', department: 'Computer Science' }
            ];
            
            displayGroupMembers(groupMembers);
            
            // Show project assignment section
            document.getElementById('projectAssignmentSection').style.display = 'block';
        } catch (error) {
            console.error('Error loading group details:', error);
            alert('Failed to load group details. Please try again.');
        }
    }

    function displayGroupMembers(members) {
        const container = document.getElementById('groupMembersContainer');
        
        let html = `
            <h3>Group Members</h3>
            <table class="group-members-table">
                <thead>
                    <tr>
                        <th>Roll No</th>
                        <th>Name</th>
                        <th>Department</th>
                    </tr>
                </thead>
                <tbody>
        `;
        
        members.forEach(member => {
            html += `
                <tr>
                    <td>${member.rollNo}</td>
                    <td>${member.name}</td>
                    <td>${member.department}</td>
                </tr>
            `;
        });
        
        html += `
                </tbody>
            </table>
        `;
        
        container.innerHTML = html;
    }

    // Project Assignment Form
    const projectAssignmentForm = document.getElementById('projectAssignmentForm');
    if (projectAssignmentForm) {
        projectAssignmentForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const studentGroup = document.getElementById('studentGroup').value;
            const projectTitle = document.getElementById('projectTitle').value;
            const projectDomain = document.getElementById('projectDomain').value;
            const projectDescription = document.getElementById('projectDescription').value;
            const taskSubmissionDate = document.getElementById('taskSubmissionDate').value;
            const presentationDate = document.getElementById('presentationDate').value;
            const ieeeReportDate = document.getElementById('ieeeReportDate').value;
            const projectReportDate = document.getElementById('projectReportDate').value;
            
            const projectData = {
                projectTitle,
                domain: projectDomain,
                projectDescription,
                taskSubmissionDate,
                presentationDate,
                ieeeReportSubmissionDate: ieeeReportDate,
                reportSubmissionDate: projectReportDate
            };
            
            assignProject(studentGroup, projectData);
        });
    }

    async function assignProject(groupName, projectData) {
        try {
            const response = await fetch(`${BASE_URL}/assignProject?groupName=${encodeURIComponent(groupName)}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(projectData),
                credentials: 'include'
            });
            
            if (!response.ok) {
                throw new Error('Failed to assign project');
            }
            
            const result = await response.text();
            alert(result);
            
            // Reset form
            document.getElementById('projectAssignmentForm').reset();
        } catch (error) {
            console.error('Error assigning project:', error);
            alert('Failed to assign project. Please try again.');
        }
    }

    // View Projects & Add Marks Section
    const loadGroupDetailsForMarksBtn = document.getElementById('loadGroupDetailsForMarksBtn');
    if (loadGroupDetailsForMarksBtn) {
        loadGroupDetailsForMarksBtn.addEventListener('click', function() {
            const academicYear = document.getElementById('academicYearMarks').value;
            const projectGroup = document.getElementById('projectGroupMarks').value;
            
            if (!academicYear || !projectGroup) {
                alert('Please select both academic year and project group');
                return;
            }
            
            // Load group details for marks
            loadGroupDetailsForMarks(academicYear, projectGroup);
        });
    }

    function loadGroupDetailsForMarks(academicYear, groupName) {
        // In a real application, this would be an API call
        // For now, simulate with dummy data
        try {
            // Display group members
            const groupMembers = [
                { rollNo: '2105678', name: 'Rohit Sharma', department: 'Computer Science', domain: 'Web Development' },
                { rollNo: '2105679', name: 'Virat Kohli', department: 'Computer Science', domain: 'Web Development' },
                { rollNo: '2105680', name: 'Jasprit Bumrah', department: 'Electronics', domain: 'Web Development' },
                { rollNo: '2105681', name: 'KL Rahul', department: 'Computer Science', domain: 'Web Development' }
            ];
            
            // Display project tasks
            const projectTasks = [
                { name: 'Task 1: Requirements Gathering', submissionDate: '2025-05-01', status: 'Completed' },
                { name: 'Task 2: Design Document', submissionDate: '2025-05-15', status: 'Completed' },
                { name: 'Task 3: Implementation', submissionDate: '2025-06-01', status: 'In Progress' },
                { name: 'Task 4: Testing', submissionDate: '2025-06-15', status: 'Pending' }
            ];
            
            displayGroupMembersForMarks(groupMembers);
            displayProjectTasks(projectTasks);
            displayMarksAllocationTable(groupMembers);
            
            // Show group details and marks section
            document.getElementById('groupDetailsMarksSection').style.display = 'block';
        } catch (error) {
            console.error('Error loading group details for marks:', error);
            alert('Failed to load group details. Please try again.');
        }
    }

    function displayGroupMembersForMarks(members) {
        const tableBody = document.getElementById('groupMembersTableBody');
        tableBody.innerHTML = '';
        
        members.forEach(member => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${member.rollNo}</td>
                <td>${member.name}</td>
                <td>${member.department}</td>
                <td>${member.domain}</td>
                <td>
                    <button class="btn-view" data-rollno="${member.rollNo}">View Details</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }

    function displayProjectTasks(tasks) {
        const tableBody = document.getElementById('projectTasksTableBody');
        tableBody.innerHTML = '';
        
        tasks.forEach(task => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${task.name}</td>
                <td>${task.submissionDate}</td>
                <td>${task.status}</td>
                <td>
                    <button class="btn-view-task" data-task="${task.name}">View Details</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }

    function displayMarksAllocationTable(members) {
        const tableBody = document.getElementById('marksAllocationTableBody');
        tableBody.innerHTML = '';
        
        members.forEach(member => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${member.name}</td>
                <td><input type="number" class="task-marks" data-rollno="${member.rollNo}" min="0" max="100"></td>
                <td><input type="number" class="presentation-marks" data-rollno="${member.rollNo}" min="0" max="100"></td>
                <td><input type="number" class="report-marks" data-rollno="${member.rollNo}" min="0" max="100"></td>
                <td class="total-marks" data-rollno="${member.rollNo}">0</td>
            `;
            tableBody.appendChild(row);
        });
        
        // Add event listeners for marks calculation
        document.querySelectorAll('.task-marks, .presentation-marks, .report-marks').forEach(input => {
            input.addEventListener('input', calculateTotalMarks);
        });
    }

    function calculateTotalMarks() {
        const rollNo = this.getAttribute('data-rollno');
        const taskMarks = document.querySelector(`.task-marks[data-rollno="${rollNo}"]`).value || 0;
        const presentationMarks = document.querySelector(`.presentation-marks[data-rollno="${rollNo}"]`).value || 0;
        const reportMarks = document.querySelector(`.report-marks[data-rollno="${rollNo}"]`).value || 0;
        
        const total = parseInt(taskMarks) + parseInt(presentationMarks) + parseInt(reportMarks);
        document.querySelector(`.total-marks[data-rollno="${rollNo}"]`).textContent = total;
    }

    // Marks Allocation Form Submission
    const marksAllocationForm = document.getElementById('marksAllocationForm');
    if (marksAllocationForm) {
        marksAllocationForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const groupName = document.getElementById('projectGroupMarks').value;
            
            // Collect marks data
            const taskMarks = [];
            const presentationMarks = [];
            const reportMarks = [];
            
            document.querySelectorAll('.task-marks').forEach(input => {
                taskMarks.push(parseInt(input.value || 0));
            });
            
            document.querySelectorAll('.presentation-marks').forEach(input => {
                presentationMarks.push(parseInt(input.value || 0));
            });
            
            document.querySelectorAll('.report-marks').forEach(input => {
                reportMarks.push(parseInt(input.value || 0));
            });
            
            const marksData = {
                taskMarks,
                presentationMarks,
                reportMarks
            };
            
            submitMarks(groupName, marksData);
        });
    }

    async function submitMarks(groupName, marksData) {
        try {
            // In a real application, you would make an API call here
            // For now, simulate with an alert
            alert(`Marks submitted successfully for group: ${groupName}`);
        } catch (error) {
            console.error('Error submitting marks:', error);
            alert('Failed to submit marks. Please try again.');
        }
    }

    // Meeting Section
    const meetingScheduleForm = document.getElementById('meetingScheduleForm');
    if (meetingScheduleForm) {
        // Populate available groups
        populateAvailableGroups();
        
        meetingScheduleForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const groupName = document.getElementById('availableGroups').value;
            const meetingDate = document.getElementById('meetingDate').value;
            const meetingTime = document.getElementById('meetingTime').value;
            const meetingComment = document.getElementById('meetingComment').value;
            
            const meetingData = {
                groupName,
                meetingDate: formatDate(meetingDate),
                meetingTime: formatTime(meetingTime),
                description: meetingComment || "Project Discussion"
            };
            
            scheduleMeeting(meetingData);
        });
    }

    function populateAvailableGroups() {
        // In a real application, this would be from an API call
        const groups = ['Group A', 'Group B', 'Group C', 'Group D'];
        const select = document.getElementById('availableGroups');
        
        if (select) {
            select.innerHTML = '<option value="">Select Group</option>';
            
            groups.forEach(group => {
                const option = document.createElement('option');
                option.value = group;
                option.textContent = group;
                select.appendChild(option);
            });
        }
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        return `${date.getMonth() + 1}/${date.getDate()}/${String(date.getFullYear()).slice(2)}`;
    }

    function formatTime(timeString) {
        const [hours, minutes] = timeString.split(':');
        const hour = parseInt(hours);
        const ampm = hour >= 12 ? 'PM' : 'AM';
        const hour12 = hour % 12 || 12;
        return `${hour12}:${minutes} ${ampm}`;
    }

    async function scheduleMeeting(meetingData) {
        try {
            const response = await fetch(`${BASE_URL}/scheduleMeeting`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(meetingData),
                credentials: 'include'
            });
            
            if (!response.ok) {
                throw new Error('Failed to schedule meeting');
            }
            
            const result = await response.text();
            alert(result);
            
            // Reset form
            document.getElementById('meetingScheduleForm').reset();
        } catch (error) {
            console.error('Error scheduling meeting:', error);
            alert('Failed to schedule meeting. Please try again.');
        }
    }

    // Final Marks Viewport Section
    const loadFinalMarksBtn = document.getElementById('loadFinalMarksBtn');
    if (loadFinalMarksBtn) {
        loadFinalMarksBtn.addEventListener('click', function() {
            const academicYear = document.getElementById('academicYearFinalMarks').value;
            const department = document.getElementById('departmentFinalMarks').value;
            
            if (!academicYear || !department) {
                alert('Please select both academic year and department');
                return;
            }
            
            loadFinalMarks(academicYear, department);
        });
    }

    function loadFinalMarks(academicYear, department) {
        // In a real application, this would be an API call
        // For now, simulate with dummy data
        try {
            const finalMarks = [
                { rollNo: '2105678', name: 'Rohit Sharma', department: 'Computer Science', domain: 'Web Development', taskMarks: 85, presentationMarks: 90, reportMarks: 88, totalMarks: 263 },
                { rollNo: '2105679', name: 'Virat Kohli', department: 'Computer Science', domain: 'Web Development', taskMarks: 90, presentationMarks: 92, reportMarks: 95, totalMarks: 277 },
                { rollNo: '2105680', name: 'Jasprit Bumrah', department: 'Electronics', domain: 'Web Development', taskMarks: 82, presentationMarks: 88, reportMarks: 85, totalMarks: 255 },
                { rollNo: '2105681', name: 'KL Rahul', department: 'Computer Science', domain: 'Web Development', taskMarks: 80, presentationMarks: 85, reportMarks: 82, totalMarks: 247 }
            ];
            
            displayFinalMarks(finalMarks);
            
            // Show final marks table
            document.getElementById('finalMarksTableContainer').style.display = 'block';
        } catch (error) {
            console.error('Error loading final marks:', error);
            alert('Failed to load final marks. Please try again.');
        }
    }

    function displayFinalMarks(marks) {
        const tableBody = document.getElementById('finalMarksTableBody');
        tableBody.innerHTML = '';
        
        marks.forEach(student => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${student.rollNo}</td>
                <td>${student.name}</td>
                <td>${student.department}</td>
                <td>${student.domain}</td>
                <td>${student.taskMarks}</td>
                <td>${student.presentationMarks}</td>
                <td>${student.reportMarks}</td>
                <td>${student.totalMarks}</td>
            `;
            tableBody.appendChild(row);
        });
    }

    // Export to Excel
    const exportToExcelBtn = document.getElementById('exportToExcelBtn');
    if (exportToExcelBtn) {
        exportToExcelBtn.addEventListener('click', function() {
            exportToExcel();
        });
    }

    function exportToExcel() {
        alert('Exporting to Excel... This feature would download an Excel file in a real application.');
    }

    // Notifications Section
    loadNotifications();

    async function loadNotifications() {
        try {
            const response = await fetch(`${BASE_URL}/getMessages`, {
                method: 'GET',
                credentials: 'include'
            });
            
            if (!response.ok) {
                throw new Error('Failed to load notifications');
            }
            
            const notifications = await response.json();
            displayNotifications(notifications);
        } catch (error) {
            console.error('Error loading notifications:', error);
            displayFallbackNotifications();
        }
    }

    function displayNotifications(notifications) {
        const notificationsPanel = document.getElementById('notificationsPanel');
        
        if (!notificationsPanel) return;
        
        if (notifications.length === 0) {
            notificationsPanel.innerHTML = '<p>No notifications at this time.</p>';
            return;
        }
        
        let html = '';
        
        notifications.forEach(notification => {
            const statusClass = getStatusClass(notification.status);
            
            html += `
                <div class="notification-item ${statusClass}">
                    <div class="notification-header">
                        <span class="notification-title">${notification.title || 'Mentorship Request'}</span>
                        <span class="notification-date">${formatNotificationDate(notification.createdAt)}</span>
                    </div>
                    <div class="notification-body">
                        <p>${notification.message}</p>
                    </div>
                    <div class="notification-footer">
                        <span class="notification-status">${notification.status}</span>
                        ${notification.status === 'PENDING' ? `
                            <div class="notification-actions">
                                <button class="btn-approve" data-id="${notification.id}">Approve</button>
                                <button class="btn-reject" data-id="${notification.id}">Reject</button>
                            </div>
                        ` : ''}
                    </div>
                </div>
            `;
        });
        
        notificationsPanel.innerHTML = html;
        
        // Add event listeners for approve/reject buttons
        document.querySelectorAll('.btn-approve').forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                respondToNotification(id, 'APPROVED');
            });
        });
        
        document.querySelectorAll('.btn-reject').forEach(button => {
            button.addEventListener('click', function() {
                const id = this.getAttribute('data-id');
                respondToNotification(id, 'REJECTED');
            });
        });
    }

    function getStatusClass(status) {
        switch (status) {
            case 'APPROVED':
                return 'status-approved';
            case 'REJECTED':
                return 'status-rejected';
            case 'PENDING':
            default:
                return 'status-pending';
        }
    }

    function formatNotificationDate(dateString) {
        if (!dateString) return 'Unknown date';
        
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    async function respondToNotification(notificationId, status) {
        const message = prompt(`Please provide a message for your ${status.toLowerCase()} response:`);
        
        if (message === null) return; // User cancelled
        
        const responseData = {
            notificationId: parseInt(notificationId),
            status: status,
            message: message
        };
        
        try {
            const response = await fetch(`${BASE_URL}/sendResponse`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(responseData),
                credentials: 'include'
            });
            
            if (!response.ok) {
                throw new Error('Failed to respond to notification');
            }
            
            const result = await response.text();
            alert(result);
            
            // Refresh notifications
            loadNotifications();
        } catch (error) {
            console.error('Error responding to notification:', error);
            alert('Failed to respond to notification. Please try again.');
        }
    }

    function displayFallbackNotifications() {
        const notificationsPanel = document.getElementById('notificationsPanel');
        
        if (!notificationsPanel) return;
        
        const fallbackNotifications = [
            {
                id: 1,
                title: 'Mentorship Request',
                message: 'Group A has requested you to be their project mentor.',
                status: 'PENDING',
                createdAt: new Date().toISOString()
            },
            {
                id: 2,
                title: 'Project Update',
                message: 'Group B has submitted their latest project milestone.',
                status: 'APPROVED',
                createdAt: new Date(Date.now() - 86400000).toISOString() // 1 day ago
            },
            {
                id: 3,
                title: 'Meeting Reminder',
                message: 'Don\'t forget your scheduled meeting with Group C tomorrow at 10:00 AM.',
                status: 'APPROVED',
                createdAt: new Date(Date.now() - 172800000).toISOString() // 2 days ago
            }
        ];
        
        displayNotifications(fallbackNotifications);
    }
});