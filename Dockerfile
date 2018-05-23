FROM openjdk:10
VOLUME /tmp
ADD target/lab3.jar app.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=prod -jar /app.jar