# build original Library app to get jar
# library-migration must be in the same parent directory as library
# run docker build command from parent directory:
# docker build -f library-migration/Dockerfile -t ilyacyclone/otus-library-migration:0.0.1 .

FROM maven:3.6.1-jdk-8 AS BUILD_LIBRARY_STAGE
ENV PROJECT_DIR=/library-app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
#COPY ./library/maven-settings.xml /root/.m2/settings.xml
COPY ./library/pom.xml $PROJECT_DIR
RUN mvn dependency:go-offline -B
COPY ./library/src/ $PROJECT_DIR/src
RUN mvn -DskipTests=true clean package



FROM maven:3.6.1-jdk-8 AS BUILD_STAGE
ENV PROJECT_DIR=/library-migration-app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
#COPY ./library/maven-settings.xml /root/.m2/settings.xml
#COPY ./library/maven-settings_local-nexus.xml /root/.m2/settings.xml
COPY ./library/maven-settings_docker-nexus.xml /root/.m2/settings.xml
COPY --from=BUILD_LIBRARY_STAGE /library-app/target/otus-library-app-to-import.jar $PROJECT_DIR/
RUN mvn install:install-file -Dfile=/library-migration-app/otus-library-app-to-import.jar -DgroupId=cyclone.otus-spring-hw -DartifactId=library -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -Dclassifier=app-to-import
COPY ./library-migration/pom.xml $PROJECT_DIR
RUN mvn dependency:go-offline -B
COPY ./library-migration/src/ $PROJECT_DIR/src
RUN mvn -DskipTests=true clean package



FROM openjdk:8-jre-alpine

ENV PROJECT_DIR=/library-migration-app
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR

COPY --from=BUILD_STAGE $PROJECT_DIR/target/otus-library-migration.jar $PROJECT_DIR/

ENTRYPOINT ["java", "-jar", "/library-migration-app/otus-library-migration.jar"]