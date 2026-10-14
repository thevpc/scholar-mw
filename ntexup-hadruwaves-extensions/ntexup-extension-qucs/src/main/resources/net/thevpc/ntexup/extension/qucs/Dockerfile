FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update -qq && \
    apt-get install -y -qq --no-install-recommends \
        build-essential \
        cmake \
        flex \
        bison \
        gperf \
        dos2unix \
        git \
        ca-certificates && \
    git clone --depth 1 https://github.com/ra3xdh/qucsator_rf.git /tmp/qucsator_rf && \
    mkdir -p /tmp/qucsator_rf/build && \
    cd /tmp/qucsator_rf/build && \
    cmake -DCMAKE_BUILD_TYPE=Release .. && \
    make -j$(nproc) && \
    make install && \
    ln -s /usr/local/bin/qucsator_rf /usr/local/bin/qucsator && \
    rm -rf /tmp/qucsator_rf && \
    apt-get purge -y --auto-remove build-essential cmake flex bison gperf dos2unix git && \
    rm -rf /var/lib/apt/lists/*

ENTRYPOINT ["qucsator"]
CMD ["--help"]
