#docker build -t ilyacyclone/otus-library:0.0.1 .
# docker run -e SPRING_DATA_MONGODB_HOST=<mongodb_host> ilyacyclone/otus-library:0.0.1
# -v "$HOME/.m2":/root/.m2 --- add this to reuse local maven repo (or not?)

FROM maven:3.6.1-jdk-8 AS MAVEN_BUILD
#FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD # build failed

VOLUME $HOME/.m2:/root/.m2
ENV PROJECT_DIR=/app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY ./pom.xml $PROJECT_DIR
RUN mvn dependency:resolve
# .dockerignore will ignore node_modules when copying
COPY ./src/ $PROJECT_DIR/src

RUN mvn -DskipTests=true clean package


FROM openjdk:8-jre-alpine

ENV PROJECT_DIR=/app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY --from=MAVEN_BUILD $PROJECT_DIR/target/otus-library.jar $PROJECT_DIR/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/otus-library.jar"]