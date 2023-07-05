FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY build/libs/demo.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
