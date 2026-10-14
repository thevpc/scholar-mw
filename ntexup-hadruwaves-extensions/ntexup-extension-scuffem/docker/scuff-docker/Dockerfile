FROM jfeist/scuff-em:latest
USER root
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y --no-install-recommends gmsh \
    && rm -rf /var/lib/apt/lists/*
ENTRYPOINT ["/usr/local/bin/scuff"]
