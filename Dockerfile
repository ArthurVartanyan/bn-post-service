FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*.jar bn-post-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bn-post-service.jar"]