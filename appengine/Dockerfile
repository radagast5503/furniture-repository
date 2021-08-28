FROM amazoncorretto:16-alpine
ARG JAR_FILE=JAR_FILE_MUST_BE_SPECIFIED_AS_BUILD_ARG
ARG ENVIRONMENT=local
RUN mkdir -p /app
COPY ${JAR_FILE} /app
RUN ls /app
WORKDIR /app
ENTRYPOINT ["java","-jar","furniture-repository.jar"]