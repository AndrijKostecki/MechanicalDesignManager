Mechanical Design Manager
Overview
Mechanical Design Manager is a Spring Boot web application designed to manage engineering projects, tasks, work logs, and generate time-based reports for users and projects.
 The application was created as a portfolio project to demonstrate practical backend development skills using Java and Spring ecosystem.
Features
    • User management with roles (ADMIN / USER)
    • Project, unit and task management
    • Task work logging (hours, dates, assignments)
    • User reports (hours grouped by project and task)
    • Project reports (hours grouped by unit)
    • PDF export for reports
    • Authentication and authorization (Spring Security)
    • Server-side rendering with Thymeleaf
Tech Stack
    • Java 17+
    • Spring Boot
    • Spring MVC
    • Spring Data JPA (Hibernate)
    • Spring Security
    • Thymeleaf
    • H2 Database (in-memory)
    • Maven
    • JUnit 5
    • Mockito
    • OpenHTMLToPDF
Architecture
The application follows a layered architecture:
    • Controller layer – handles HTTP requests
    • Service layer – business logic and report generation
    • Repository layer – database access (Spring Data JPA)
    • Domain model – entities (User, Project, Task, Unit, TaskWorkLog)
    • DTO / Report objects – UserReport, ProjectReport
Reports
    • User Report
        ◦ Total hours per task
        ◦ Tasks grouped by project
    • Project Report
        ◦ Total hours per unit
        ◦ Overall project hours
Reports can be exported to PDF using Thymeleaf templates.
Testing
    • Unit tests written with JUnit 5
    • Repository dependencies mocked using Mockito
    • Focus on testing service-level business logic (report generation)
How to Run
git clone https://github.com/AndrijKostecki/MechanicalDesignManager.git
cd MechanicalDesignManager
mvn spring-boot:run

Application will be available at:
http://localhost:8080


Purpose
This project was built to:
    • practice real-world Spring Boot development
    • understand JPA, Hibernate, and entity relationships
    • learn testing with Mockito
    • demonstrate backend skills for Junior Java Developer positions
Future Improvements
    • Validation layer (Bean Validation)
    • REST API version
    • Pagination and filtering
    • Docker support
    • PostgreSQL database
    • Improved test coverage
Author
Andrii Kostetskyi
 Mechanical Engineer transitioning into Java backend development
