# Palace (awslabs) built with its own CMake superbuild, which compiles MFEM, hypre, MUMPS and
# the other dependencies at the versions the release pins
FROM ubuntu:24.04
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates git curl build-essential gfortran cmake pkg-config python3 \
    libopenmpi-dev openmpi-bin libopenblas-dev liblapack-dev \
    && rm -rf /var/lib/apt/lists/*
ARG PALACE_VERSION=v0.18.1
RUN git clone --depth 1 --branch ${PALACE_VERSION} https://github.com/awslabs/palace.git /opt/palace-src
RUN mkdir /opt/palace-build && cd /opt/palace-build && \
    cmake /opt/palace-src -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX=/opt/palace \
          -DPALACE_WITH_MUMPS=ON -DPALACE_WITH_OPENMP=ON && \
    make -j 24 && rm -rf /opt/palace-build
ENV PATH=/opt/palace/bin:$PATH
ENV OMPI_ALLOW_RUN_AS_ROOT=1 OMPI_ALLOW_RUN_AS_ROOT_CONFIRM=1
RUN palace --help | head -5
CMD ["palace", "--help"]
