# Use a lightweight base image with necessary tools
FROM --platform=linux/amd64 ubuntu:latest

# Specify the mkcert version you want to install
ARG MKCERT_VERSION="v1.4.4"
ARG LOCAL_BIN="/usr/local/bin"

WORKDIR /storage

# Install required packages
RUN apt-get update && apt-get install -y \
    openssl \
    bash \
    curl \
    && rm -rf /var/lib/apt/lists/*  # Clean up to reduce image size

# Install mkcert
RUN echo "Installing mkcert locally..." && \
    curl -fsSL "https://github.com/FiloSottile/mkcert/releases/download/${MKCERT_VERSION}/mkcert-${MKCERT_VERSION}-linux-amd64" -o "${LOCAL_BIN}/mkcert" && \
    chmod +x "${LOCAL_BIN}/mkcert" && \
    echo "mkcert installed successfully for the current user."

# Install crypt4gh
RUN echo "Installing crypt4gh locally..." && \
    curl -fsSL "https://raw.githubusercontent.com/neicnordic/crypt4gh/master/install.sh" | sh -s -- -b "$LOCAL_BIN" && \
    chmod +x "$LOCAL_BIN/crypt4gh" && \
    echo "crypt4gh installed successfully for the current user."

COPY .env .env
RUN mkdir -p "certs"
COPY confs confs
COPY scripts/generate_certs.sh scripts/copy_confs_and_change_ownerships.sh scripts/replace_template_variables.sh ./
RUN chmod +x generate_certs.sh copy_confs_and_change_ownerships.sh replace_template_variables.sh

# We recursively set chmod 777 on the volumes/ directory and its subdirectories.
# Next, we adjust ownership and permissions more granularly using the
# copy_confs_and_change_ownerships.sh script, tailored to the requirements of
# each container (e.g., PostgreSQL and RabbitMQ containers).
ENTRYPOINT [ "/bin/sh", "-c", "chmod -R 777 /volumes && ./generate_certs.sh && ./copy_confs_and_change_ownerships.sh && ./replace_template_variables.sh && touch /storage/ready && tail -f /dev/null" ]

# Add a HEALTHCHECK to verify readiness
HEALTHCHECK --interval=5s --timeout=3s --retries=5 \
    CMD [ "/bin/sh", "-c", "[ -f /storage/ready ] || exit 1" ]
