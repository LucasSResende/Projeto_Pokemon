<h1 align="center"> Project 1 - IDP - Game Manager</h1>

You need to install the following technology:

<div align="left">

![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-3069DE?style=for-the-badge&logo=kubernetes&logoColor=white)
![Helm](https://img.shields.io/badge/Helm-0F1689?style=for-the-badge&logo=Helm&labelColor=0F1689)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)

</div>

---

## About the Project

This is the first IDP project with closed scope. The project consists of a game manager,
in which the user can save, list, update and delete games. The target audience for this project is all
users who like games and want to better manage their time played.

In this project I used the [MySQL](https://hub.docker.com/_/mysql)
and [Publisher Manager](https://hub.docker.com/r/lucassresende/publisher-manager) microservices,
knowledge acquired about Java, Spring Boot, Kubernetes, Helm, Jenkins and more.

---

## End-Points (Game Manager - Port 8081)

### 1. Read all games:
`GET http://localhost:8081/game`

### 2. Read games by publisherId:
`GET http://localhost:8081/game?publisherId={publisherId}`

### 3. Read game by id:
`GET http://localhost:8081/game/{id}`

### 4. Register a new game:
`POST http://localhost:8081/game`

Payload example:
```json
{
    "publisherId": "nintendo",
    "name": "Mario",
    "timePlayed": {
        "2023-05-01": 10,
        "2023-05-02": 2
    }
}
```

### 5. Update an existing game (NEW):
`PUT http://localhost:8081/game/{id}`

Payload example:
```json
{
    "publisherId": "nintendo",
    "name": "Super Mario Odyssey",
    "timePlayed": {
        "2023-05-01": 15,
        "2023-05-02": 5
    }
}
```
Returns `200 OK` with the updated game, or `404 Not Found` if id does not exist.

### 6. Delete a game (NEW):
`DELETE http://localhost:8081/game/{id}`

Returns `204 No Content` on success, or `404 Not Found` if id does not exist.

### 7. Clean cache (Downstream callback):
`DELETE http://localhost:8081/publishercache` (or `/publishermanagercache`)
Automatically triggered by Publisher Manager whenever a publisher is added, updated, or removed.
