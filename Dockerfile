FROM gradle:8.7.0-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/project
WORKDIR /home/gradle/project
RUN gradle build -x test

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
