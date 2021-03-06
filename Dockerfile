FROM alpine/git as clone
RUN adduser -u 1000 -D -g '' git
USER git
WORKDIR /home/git
RUN git clone https://github.com/NLatyshev/utils.git

FROM gradle:alpine
WORKDIR /home/gradle
COPY --from=clone /home/git/utils /home/gradle
USER root
RUN chown -R gradle *
USER gradle
RUN gradle clean test