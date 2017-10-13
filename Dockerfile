FROM alpine/git

RUN ls -la

WORKDIR /app
RUN git clone https://github.com/spring-projects/spring-petclinic.git

RUN ls -la

FROM gradle:alpine
WORKDIR /app

COPY --from=0 /app/utils /app

RUN ls -la

RUN gradle clean test