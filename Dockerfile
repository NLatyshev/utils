FROM alpine/git

RUN ls -la

WORKDIR /app
RUN git clone https://github.com/NLatyshev/utils.git

RUN ls -la

FROM gradle:alpine
WORKDIR /app

COPY --from=0 /app/utils /app

RUN ls -la

RUN gradle clean test