FROM gradle:alpine

RUN git clone https://github.com/NLatyshev/utils.git

RUN ["gradle", "clean"]
RUN ["gradle", "test"]