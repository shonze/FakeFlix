FROM debian:bullseye

RUN apt-get update && apt-get install -y \
    g++ \
    cmake \
    && rm -rf /var/lib/apt/lists/*

COPY . /usr/src/project/

WORKDIR /usr/src/project

RUN mkdir build
WORKDIR /usr/src/project/build

RUN cmake .. && make

CMD ["./runTests"]