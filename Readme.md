# To-Do Application

This is a simple application, where you can make a list of your tasks. You can access this list trough a web interface,
 which communicates on a websocket, or if you like, you can do the same with REST endpoints.

## Running the application in a Docker container

Prerequisite: Software installed: Git, JDK8, Docker Compose.


The application is prepared to run in a Docker container. For this, use the following commands:

```
git clone git@github.com:bartlbalazs/TodoApp.git
cd TodoApp
./mvnw clean install
docker-compose build
docker-compose up
```

After this, the application will start on port 8080.

Additional ports:

6379: postgresql db

5432: redis

###  dev mode support

If you would like to run this application from an IDE, you should

- run the main class, which is TodoApplication

- to serve the UI resources with live reload: in the project issue ./mvnw generate-resources, then run ./ng build --watch

### REST endpoints

```
GET /tasks returns all tasks in an array
```
```
GET /tasks/{id} returns a single task with the given ID, or returns with  HTTP 404
```
```
POST /tasks creates a new task by a JSON input object
Sample input:
{
    "description": "task description"
}
```

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - Backend
* [Angular](https://angular.io/) - Frontend
* [PostgreSQL](https://www.postgresql.org/) - because it integrates well with Spring, and I had no reason to use a NO-SQL db
* [Redis](https://redis.io/) - because its lightweight and has an embedded version, which is handy at tests


## Author

* **Balázs Bartl**