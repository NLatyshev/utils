FROM alpine/git

RUN id
RUN ls -la

VOLUME "$PWD":/home/gradle/project
WORKDIR /home/gradle/project
RUN git clone https://github.com/NLatyshev/utils.git

RUN cmod 777 *

RUN ls -la

FROM gradle:alpine

RUN id

RUN ls -la

VOLUME "$PWD":/home/gradle/project
WORKDIR /home/gradle/project

COPY --from=0 /home/gradle/project/utils /home/gradle/project


RUN ls -la

RUN gradle clean test