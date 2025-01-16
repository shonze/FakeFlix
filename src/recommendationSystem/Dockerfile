FROM gcc:latest

COPY . /usr/src/myapp

WORKDIR /usr/src/myapp

RUN g++ -pthread -o myapp src/recommendationSystem/*.cpp 

ENTRYPOINT ["./myapp"]