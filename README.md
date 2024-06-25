# Flight Planner Application

This Spring Boot application enables users to manage and search for flights through a web interface. It utilizes an H2 in-memory database for data storage. Below is a guide to help you set up and run the application.

## How to Install and Run the Project

### Prerequisites
- **Java Development Kit (JDK):** Ensure you have JDK 21 installed. You can download it from [Oracle's official website](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).
- **Gradle:** Make sure you have Gradle installed. You can download it from [Gradle's official website](https://gradle.org/install/).

### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/madmad7/flight-planner
   cd flight-planner

2. Build the Project:
   Run the following command to build the project:
   
    ```bash   
   ./gradlew build

### Running the Application

1. Start the Application:
   Run the following command to start the application:
   ```bash
   ./gradlew bootRun   
2. The application should now be running on http://localhost:8080.
3. You can access the H2 database console at http://localhost:8080/h2-console with the following credentials:

    ```bash
    JDBC URL: jdbc:h2:mem:db
    Username: codelex
    Password: password

### Dependencies

    Spring Boot 3.2.5
    JDK 21
    H2 Database 2.2.224
    Liquibase Core
