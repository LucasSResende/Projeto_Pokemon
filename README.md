<!--Have you not prepared a installation or deployment diagram? That is an important part of the Read.me file-->

<h1 align="center"> Project 1 - IDP - Game Manager</h1>

You need to install the following technology: <!--This section needs a title to be separated from the others. Something like "prerequisite", for example.-->

<div align="left">

![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)

</div>

---

## About the Project <!--Change this title to "summary" or "introduction"-->

This is the first IDP project with closed scope. The project consists of a game manager,
in which the user can save and list games. The target audience for this project is all
users who like games and want to better manager <!--manage--> their time played.

In this project I used the  [Mysql](https://hub.docker.com/_/mysql)
and [adautomendes/publisher-manager](https://hub.docker.com/r/adautomendes/publisher-manager) containers,
and some knowledge acquired in alura courses <!--list the subjects you have to understand to develop this project-->.
---

## Diagrams

1. The following operation diagram shows the macro vision of distribution <!--working of the microservices--> of the microservices

<h1 align="center">
    <img align="center" src="modeling/OperationDiagram-Operation_Diagram.png" width="500px"/>
</h1> <!--replace user element by an interface or platform the user can use such as Postman, for example-->
The user can perform three operations: 

- Read all games in database, following the next sequence diagram <!--Read all games in database, according to the following sequence diagram-->:

<h1 align="center">
    <img align="center" src="modeling/SequenceDiagramGetAll.png" width="500px"/>
</h1><!--what is the ellipsis in brackets for? Add the scenario of failure or success of requests to the diagram-->

- Read only games specified by publisherId, following the next sequence diagram <!--update this phrase as the previous comment-->:

<h1 align="center">
    <img align="center" src="modeling/SequenceDiagramGetByPublisher.png" width="500px"/>
</h1><!--what is the ellipsis in brackets for? Add the scenario of failure or success of requests to the diagram-->

- Save a new game in database, following the next sequence diagram <!--update this phrase as the previous comment-->:

<h1 align="center">
    <img align="center" src="modeling/SequenceDiagramPost.png" width="500px"/>
</h1><!--Involve the valid and invalid cases in a bigger square with "Alt" as its title for alternative. This means that one the cases will be executed based on the validity of the request or response. The way the diagram is means that both of the cases will be executed and that is not true.-->

---

## Technologies

What I used to development <!--develop-->the project:</p>

<div align="left">

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

---

## Project <!--this section is about steps to run the project. Change the title-->

<h3> Running the application:</h3>
Use the following command to clone the project: <!--use where?-->

```bash
$ https://github.com/LucasSResende/Project1_IDP.git
```

To build the project, use this command: <!--use where?-->

```bash
$ mvn clean install -DskipTests
```

Then enter <!--you mean start?--> the Ubuntu terminal <!--is Linux a prerequisite? If so, it should be listed as a prerequisite. Otherwise, change this to be generic.-->, in the same directory where the pom file is present.
And start the application container with the following command:

```bash
$ docker-compose up --build
```

---

# End-Points <!--I think the endpoints need to be highlighted somehow. They deserve special attention and must be discrimated from each other. Also, separate the input payload and output with different subsections-->

Endpoints of port 8081 (Game Manager) <!--This is confusing. Do ports have endpoints?-->

To read all games: <!--read all games:-->
`GET  http://localhost:8081/game`

To read games by publisherId: <!--update according to the previous comment-->
`GET  http://localhost:8081/game?publisherId={publisherId}`

It is necessary to use a valid publisherId in the URL; <!--Elaborate a table with the mandatory and optional parameters of the endpoints or payloads-->

To register a new timePlayed with games: <!--update according to the previous comment-->
`POST  http://localhost:8081/game`

-The input payload for creating a timePlayed with games needs to follow these example:

```bash
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

After save game, an output will be return following these example:

```bash
{
    "id": "004ce1bf-951d-4193-b9fe-6bfdbe58544a"
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

To clean cache: <!--update according to the previous comment-->
`DELETE  http://localhost:8081/publishermanagercache`

To clean the cache of my application, an API call
is made automatically by the external API when a publisher is created.

Endpoints of port 8080 (Publisher Manager) <!--read the previous comments for updating this-->

To read all publishers: <!--update according to the previous comment-->
`GET  http://localhost:8080/publisher`

To register a new publisher: <!--update according to the previous comment-->
`POST  http://localhost:8080/publisher`

The payload for creating a publisher follows the example:

```bash
{
  "id": "microsoft",
  "name": "Microsotf company"
}
```