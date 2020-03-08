FROM node:12-alpine AS BUILDER

WORKDIR /workdir

# Dependencies
COPY package.json /workdir/package.json
COPY package-lock.json /workdir/package-lock.json
RUN npm install

ARG buildtype

# Project sources
COPY . /workdir
RUN npm run $buildtype

FROM nginx:1.17.5-alpine
COPY --from=BUILDER /workdir/dist/angular8-springboot-client /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
