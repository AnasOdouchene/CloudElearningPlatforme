# E-Learning Platform (E-Book)

This project is an e-learning platform built to provide students and teachers with an easy way to access, share, and manage educational resources like books, courses, projects, and assignments. The platform leverages Firebase for authentication and Azure Blob Storage for document management, with a backend built in Spring Boot and a frontend in HTML, CSS, and JavaScript.

## Features

- **User Authentication**: Firebase Authentication for student and teacher login (supports email/password login).
- **Document Management**: Azure Blob Storage integration for uploading, downloading, and managing educational documents (e.g., courses, assignments, projects).
- **Frontend Pages**:
  - **HomePage**
  - **Login/Register**: User registration and login page (Student, Professor).
  - **Course Management**: View and download various educational resources categorized by subjects and categories.
- **Role-Based Access**: Different user roles (student, teacher) to manage access to various features of the platform.
  
## Tech Stack

- **Frontend**: 
  - HTML, CSS, JavaScript
  - Firebase Authentication
- **Backend**:
  - Spring Boot (Java)
  - Azure Blob Storage for file management
- **Cloud Services**:
  - Firebase for authentication
  - Azure Blob Storage for document storage
- **Containerization**:
  - Docker for containerization
  - Docker Compose for managing multiple services (backend, frontend)
- **Deployment**:
  - Heroku (for hosting both backend and frontend)

## Installation

### Prerequisites

1. Install [Docker](https://www.docker.com/get-started) on your machine.
2. Set up an account for [Firebase](https://firebase.google.com/) and [Azure](https://azure.microsoft.com/en-us/free/).
3. Clone this repository.

### Local Development Setup

#### 1. **Backend Setup (Spring Boot)**

1. Clone the backend repository:
   ```bash
   git clone https://github.com/yourusername/yourrepo-backend.git
   cd yourrepo-backend
   ```

2. Build the backend project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the backend locally:
   ```bash
   mvn spring-boot:run
   ```

#### 2. **Frontend Setup**

1. Clone the frontend repository:
   ```bash
   git clone https://github.com/yourusername/yourrepo-frontend.git
   cd yourrepo-frontend
   ```

2. To run the frontend locally, you can use a simple server like Nginx (if using Docker) or a live-server (for development):
   ```bash
   npm install -g live-server
   live-server
   ```

#### 3. **Run Both Services with Docker**

If you want to run both the frontend and backend together using Docker, use Docker Compose:

1. Create a `docker-compose.yml` file in the root of your project (if you don’t have one already):
   ```yaml
   version: '3'
   services:
     backend:
       build: ./backend
       ports:
         - "8080:8080"
     frontend:
       build: ./frontend
       ports:
         - "80:80"
   ```

2. In the terminal, navigate to the root of your project and run the following command:
   ```bash
   docker-compose up --build
   ```

3. Access the backend at `http://localhost:8080` and the frontend at `http://localhost`.

### Heroku Deployment

1. Make sure you have [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) installed and are logged in:
   ```bash
   heroku login
   ```

2. Create a new Heroku app:
   ```bash
   heroku create your-app-name
   ```

3. Build the Docker images and push them to Heroku:
   ```bash
   heroku container:login
   heroku container:push web --app your-app-name
   heroku container:release web --app your-app-name
   ```

4. Open the app in your browser:
   ```bash
   heroku open --app your-app-name
   ```

## Configuration

### Firebase Authentication

1. Set up Firebase project and obtain your credentials file (JSON).
2. Add your Firebase configuration details in the `application.properties` file of the backend:
   ```properties
   firebase.database.url=<your-firebase-database-url>
   firebase.project-id=<your-firebase-project-id>
   firebase.credentials.path=<path-to-your-firebase-adminsdk-json>
   ```

### Azure Blob Storage

1. Set up an Azure Storage account and create a Blob container.
2. Add your Azure Blob Storage configuration in the `application.properties` of the backend:
   ```properties
   spring.cloud.azure.storage.blob.connection-string=<your-azure-connection-string>
   spring.cloud.azure.storage.blob.container-name=<your-container-name>
   ```

## Usage

- **Login/Registration**: Go to the login page, register a new user (student/teacher), and log in to access the platform.
- **Document Management**:
  - Students and teachers can view and download course materials stored in Azure Blob Storage.
  - Teachers can upload new documents to the platform.

## Project Structure

```
/backend
  ├── /src
  ├── /target
  ├── Dockerfile
  ├── application.properties
  └── pom.xml
/frontend
  ├── /src
  ├── /public
  ├── Dockerfile
  ├── index.html
  └── package.json
/docker-compose.yml
/Procfile
README.md
```

## Contributing

Feel free to fork this project and submit pull requests. For any questions or suggestions, create an issue on the GitHub repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
