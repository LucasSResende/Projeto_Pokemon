FROM openjdk:17-oracle

EXPOSE 8081
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} game-manager-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","game-manager-0.0.1-SNAPSHOT.jar"]
