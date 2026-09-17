FROM ubuntu:22.04
ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates getdp gmsh \
    && rm -rf /var/lib/apt/lists/*

CMD ["getdp", "--help"]
