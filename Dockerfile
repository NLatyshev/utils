FROM gradle:alpine

RUN ls -la

RUN ["gradle", "clean"]
RUN ["gradle", "test"]