FROM alpine:3.9.4
COPY build/native-image/application /opt/docker/application
RUN chmod +x /opt/docker/application
CMD ["/opt/docker/application"]
