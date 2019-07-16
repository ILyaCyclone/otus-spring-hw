#docker build -t otus-library:0.0.1 .
#spring-shell issue: https://stackoverflow.com/questions/51449432/spring-shell-appilication-inside-docker
#spring-shell issue: https://github.com/spring-projects/spring-shell/issues/167

FROM maven:3.6.1-jdk-8 AS MAVEN_BUILD

ENV PROJECT_DIR=/app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY ./pom.xml $PROJECT_DIR
RUN mvn dependency:resolve
# .dockerignore will ignore node_modules when copying
COPY ./src/ $PROJECT_DIR/src

RUN mvn clean package -Dmaven.test.skip=true


#FROM openjdk:8-jre-alpine
FROM openjdk:8

ENV PROJECT_DIR=/app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY --from=MAVEN_BUILD $PROJECT_DIR/target/library-*.jar $PROJECT_DIR/

EXPOSE 8080

CMD ["java", "-jar", "/app/library-0.0.1-SNAPSHOT.jar"]