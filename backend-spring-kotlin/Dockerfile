FROM eclipse-temurin:21.0.9_10-jre-alpine
VOLUME /tmp
COPY build/libs/app.jar app.jar
RUN apk -U upgrade
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
