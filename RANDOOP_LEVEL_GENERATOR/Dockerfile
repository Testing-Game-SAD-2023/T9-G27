FROM openjdk:17
WORKDIR /
ARG JAR_FILE=target/task9-1.0.jar
COPY ${JAR_FILE} app.jar
COPY eseguibili /eseguibili
ENTRYPOINT ["java", "-jar","/app.jar"]

