RUN ls -la

FROM alpine/git
WORKDIR /app
RUN git clone https://github.com/spring-projects/spring-petclinic.git

RUN ls -la

FROM gradle:alpine
WORKDIR /app

RUN gradle clean test