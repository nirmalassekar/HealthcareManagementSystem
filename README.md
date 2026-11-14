Healthcare Management System

A Java-based Healthcare Management System designed to manage patient records, doctor details, and medical appointments. The application connects a web-based frontend with a Java backend and MariaDB database.

Features

- Patient Registration and Listing
- Doctor Registration and Listing
- Appointment Booking
- Appointment Update and Delete
- Form Validation
- Dashboard with Live Database Data
- MariaDB Database Integration

Technologies Used

- Frontend: HTML, CSS, JavaScript
- Backend: Java
- Database: MariaDB
- JDBC Driver: MariaDB Java Client
- Server: Java Built-in HTTP Server

Project Structure

HealthcareManagementSystem/
│
├── Frontend/
│   ├── HTML files
│   ├── CSS files
│   └── JavaScript files
│
├── lib/
│   └── mariadb-java-client.jar
│
├── Main.java
├── Patient.java
├── PatientService.java
├── Doctor.java
├── DoctorService.java
├── Appointment.java
├── AppointmentService.java
├── DBConnection.java
├── PatientApiServer.java
└── README.md

Database

Database name: "healthcare_db"

Main tables:

- "patients" – Stores patient information.
- "doctors" – Stores doctor information.
- "appointments" – Stores appointment details and patient/doctor relationships.

Setup and Run

1. Clone the Repository

git clone YOUR_GITHUB_REPOSITORY_URL
cd HealthcareManagementSystem

2. Configure Database

Create the MariaDB database and required tables.

Update the database URL, username, and password in "DBConnection.java".

3. Compile Java Files

javac --add-modules jdk.httpserver -cp ".:lib/mariadb-java-client.jar" *.java

4. Start the Server

java --add-modules jdk.httpserver -cp ".:lib/mariadb-java-client.jar" PatientApiServer

5. Open the Application

Visit:

http://localhost:8080/

Future Enhancements

- User Authentication
- Role-Based Access
- Improved Appointment Management
- Enhanced Dashboard and Reporting

Project Status

Core patient, doctor, and appointment management features are implemented and tested with MariaDB integration.

---

Developed as a Java Full Stack Learning Project.