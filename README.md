# KIIT Project Allocation and Marks Upload System

## Overview
The **KIIT Project Allocation and Marks Upload System** is a web-based application designed to streamline the process of project allocation, marks management, and faculty-student collaboration. This system provides a user-friendly interface for faculty members to manage projects, allocate marks, schedule meetings, and view notifications, while also enabling students to track their project progress and receive updates.

## Features
### Faculty Dashboard
- **Profile Management**: View and manage faculty details such as name, email, experience, and skills.
- **Project Allocation**: Assign projects to student groups with details like title, domain, description, and submission deadlines.
- **Marks Upload**: Allocate marks for tasks, presentations, and reports, with real-time total calculation.
- **Meeting Scheduling**: Schedule project meetings with student groups, including date, time, and additional comments.
- **Final Marks Viewport**: View and filter final marks by academic year and department, with an option to export data to Excel.
- **Notifications**: Receive and manage notifications related to mentorship requests, meetings, and project updates.

### Student Dashboard
- **Project Tracking**: View assigned projects, submission deadlines, and task statuses.
- **Mentorship Requests**: Request mentorship from faculty members.
- **Notifications**: Receive updates on project assignments, meetings, and marks.

## Technologies Used
### Frontend
- **HTML5**, **CSS3**, **JavaScript**
- **Responsive Design** for seamless user experience across devices.

### Backend
- **Spring Boot** for RESTful API development.
- **Spring Security** for secure login and session management.
- **MySQL** for database management.
- **Spring Mail** for email notifications.

### Additional Tools
- **Git** for version control.
- **Maven** for dependency management.

## Installation and Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/project-allocation-system.git
2. Navigate to the project directory:
   ```bash
   cd Project_Allocation_System
3. Set up the backend:
   - Import the backend project into your IDE (e.g., IntelliJ IDEA, Eclipse, or Spring Tool Suite).
   - Configure the database connection in the `application.properties` file located in the `src/main/resources` directory.
   - Run the Spring Boot application using your IDE or the following command:
     ```bash
     mvn spring-boot:run
     ```

4. Set up the frontend:
   - Open the `Frontend` folder in your preferred IDE or text editor.
   - Use a local server (e.g., Live Server in VS Code) to serve the HTML files.

5. Access the application:
   - Faculty Dashboard: `http://localhost:5501/Frontend/Faculty.html`
   - Student Dashboard: `http://localhost:5501/Frontend/student.html`

## Usage
1. **Faculty Login**: Faculty members can log in to access their dashboard.
2. **Project Allocation**: Assign projects to student groups and set deadlines.
3. **Marks Upload**: Allocate marks for tasks, presentations, and reports.
4. **Meeting Scheduling**: Schedule meetings with student groups.
5. **Final Marks Viewport**: View and export final marks.

## Folder Structure
### Frontend
- `Faculty.html`: Faculty dashboard interface.
- `student.html`: Student dashboard interface.
- `css/`: Stylesheets for the application.
- `js/`: JavaScript files for interactivity.

### Backend
- `src/main/java/com/project/`: Contains controllers, models, and repositories.
- `application.properties`: Configuration for database and email services.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Commit your changes and push them to your fork.
4. Submit a pull request.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact
For any queries or suggestions, feel free to contact:
- **Email**: ramanvir2108@gmail.com
- **GitHub**: [BODUMB](https://github.com/BODUMB)
