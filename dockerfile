FROM gradle:alpine

RUN ["gradle", "clean"]
RUN ["gradle", "test"]
