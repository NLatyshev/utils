FROM gradle:alpine

RUN ["gradlew", "clean"]
RUN ["gradlew", "test"]