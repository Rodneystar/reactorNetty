FROM openjdk:10-jre-slim
COPY ./build/libs/reactornettytcp-all-1.0.jar /app.jar
EXPOSE 9000
CMD ["java", "-jar", "/app.jar"]
