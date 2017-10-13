FROM alpine/git

RUN ls -la

WORKDIR /app
RUN git clone https://github.com/NLatyshev/utils.git

RUN ls -la

FROM gradle:alpine

RUN ls -la

VOLUME "$PWD":/home/gradle/project
WORKDIR /home/gradle/project

COPY --from=0 /app/utils /home/gradle/project


RUN ls -la

RUN gradle clean test