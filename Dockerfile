FROM alpine/git as clone
RUN adduser -u 1000 -D -g '' git
USER git
WORKDIR /home/git
RUN git clone https://github.com/NLatyshev/utils.git

FROM gradle:alpine
WORKDIR /home/gradle
COPY --chown=gradle --from=0 /home/git/utils /home/gradle
RUN gradle clean test