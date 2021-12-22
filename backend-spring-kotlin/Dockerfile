FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY build/libs/app.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
