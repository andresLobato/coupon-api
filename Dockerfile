FROM anapsix/alpine-java:jdk8

MAINTAINER lobato.andres@gmail.com

COPY ./target/universal /app

USER root
RUN /bin/bash -c 'chmod +x ./app/stage/bin/coupon-api'

WORKDIR /app
EXPOSE 9000

CMD [ "/bin/bash", "/app/stage/bin/coupon-api"]





