FROM docker.rori.dev/nginx-brotli
LABEL author="Robert Rittwag <mail@rori.info>"

COPY nginx/nginx.conf /etc/nginx/nginx.conf
RUN rm /etc/nginx/conf.d/default.conf
COPY dist/ /usr/share/nginx/html
