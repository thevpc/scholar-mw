FROM ubuntu:22.04
ENV DEBIAN_FRONTEND=noninteractive
ENV INSTALL_DIR=/usr/local
ENV OPENEMS_VERSION=v0.0.36

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    git wget build-essential cmake pkg-config \
    libhdf5-dev libvtk7-dev \
    libboost-all-dev libcgal-dev libtinyxml-dev \
    gengetopt help2man groff bison flex libtool \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /root/

RUN git clone --recursive \
    --branch ${OPENEMS_VERSION} \
    https://github.com/thliebig/openEMS-Project.git

# Allow failure — openEMS binary installs at 72%, rest are optional bindings
RUN cd openEMS-Project && \
    bash update_openEMS.sh ${INSTALL_DIR} || true

# Verify the binary actually exists — fail here if it doesn't
RUN test -f /usr/local/bin/openEMS && echo "openEMS binary OK"

CMD ["openEMS", "--help"]
