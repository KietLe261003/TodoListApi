# TodoListApi

A backend API for a to-do list application built with Spring Boot. This project provides CRUD operations for managing tasks, task dependency tracking, and a scheduled notification system for upcoming or overdue tasks. It uses MongoDB as the database and is containerized with Docker.

## Features
- Create, read, update, and delete tasks
- Manage task dependencies with cycle detection
- Background scheduler fo notifying upcoming/overdue tasks
- Using Dfs to check circurlar dependencies
- Using Bfs to get all depends on task and create levels for task
- Dockerized deployment with MongoDB integration

## Setup
1. Clone the repository: `git clone https://github.com/KietLe261003/TodoListApi.git`
2. Install dependencies: `mvn install`
3. Run Docker: `docker-compose up -d`

## Endpoints
- `GET /tasks`: Retrieve all tasks
- `POST /tasks`: Create a new task
- `PUT /tasks/{id}`: Update a task
- `DELETE /tasks/{id}`: Delete a task
- `POST /task/{id}/dependencies/{dependencyId}`: Create depends on task
- `GET /task/getalldependsontask/{id}`: Get All Depends on task