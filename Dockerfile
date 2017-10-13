FROM gradle

RUN ["gradle", "clean"]
RUN ["gradle", "test"]