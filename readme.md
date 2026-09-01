# Project management system for a video production company.

This is a system, written in Java, JavaScript, HTML and CSS, which aims to fulfill the video production company's needs regarding a system for managing their projects.

## Guide for using the system

### Start-up

This project is a Spring Boot application and requires Java 17 to run.

1. Make sure Java 17 is installed and set as JAVA_HOME.
2. Open a terminal in the project root and run the application:
   - On Windows: `./mvnw spring-boot:run`
   - Or run the main class directly in your IDE: `com.p3.Film.Application`
3. Once Spring Boot has started successfully, open the application in a browser at `http://localhost:8080`.
4. If Maven wrapper files are missing, install Maven and run `mvn spring-boot:run` instead.

The main entry point is located here: `src/main/java/com/p3/Film/Application.java`.

Note: this project stores its default data in `src/main/resources/data/` and uses local file paths while running, so it is intended to be started from the project root in a local development environment.

### Login

In order to gain access to the system after typing the url, the user is prompted with a login form. Default users have been created in order to access the system as both the administrator- and editor role.

#### Administrator access

Username: admin@admin.dk <br />
Password: password

#### Editor access

Username: editor@editor.dk <br />
Password: password
