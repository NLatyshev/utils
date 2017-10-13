FROM alpine/git

RUN ls -la

WORKDIR /app
RUN git clone https://github.com/NLatyshev/utils.git

RUN ls -la

FROM gradle:alpine
WORKDIR /app

RUN ls -la

COPY --from=0 /app/utils /app

RUN ls -la

RUN sudo gradle clean test