FROM openjdk:10
ADD out/artifacts/es3_v2_jar/Es3_v2.jar es3_v2.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "es3_v2.jar"]