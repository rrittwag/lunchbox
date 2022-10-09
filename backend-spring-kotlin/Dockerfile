FROM eclipse-temurin:17.0.4_8-jre-alpine
VOLUME /tmp
COPY build/libs/app.jar app.jar
RUN apk -U upgrade
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
