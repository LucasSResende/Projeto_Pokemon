# Project 1 - IDP - Game Manager

What do we need to run the project:</p>

<div align="center">

![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Intellij](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)
![Junit](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Karate](game-manager/imgkarate.png)


</div>

## Project Build

<h3> GitHub, local e Git Bash:</h3>
Create a local directory and GitHub account along with a repository for storing and making future commits to the project.
Clone the project repository.

<h3> Running the application:</h3>
Use the WSL environment with Ubuntu to start the Docker for the main application container.
Commands:

```
mvn clean install  
```

Then enter the WSL environment:

```
wsl
```

And start the application container with the command:

```
docker-compose up --build
```

# End-Points

Endpoints of port 8081 (Game Manager)

To read all games:
`GET  http://localhost:8081/game`

To read games by publisherId:
`GET  http://localhost:8081/game?publisherId={publisherId}`

It is necessary to use a valid publisherId in the URL;

To register a new timePlayed with games:
`POST  http://localhost:8081/game`

-The payload for creating a timePlayed with games needs to follow these example:

```
{ 
    "publisherId": "nintendo", 
    "name": "Mario", 
    "timePlayed": 
        { 
            "2023-05-01": 10, 
            "2023-05-02": 2, 
            "2023-05-03": 3, 
            "2023-05-04": 4 
        } 
}
```

To clean cache:
`DELETE  http://localhost:8081/cache`

To clean the cache of my application, an API call
is made automatically by the external API when a game is created.

Endpoints of port 8080 (Publisher Manager)

To read all publishers:
`GET  http://localhost:8080/publisher`

To register a new publisher:
`POST  http://localhost:8080/publisher`

The payload for creating a publisher needs to follow these example:

```
{ 
    "host": "localhost", 
    "port": 8081 
}
```