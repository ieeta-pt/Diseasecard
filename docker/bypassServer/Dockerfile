FROM node:alpine as build

ARG SERVERNAME

WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH
COPY package*.json ./


RUN npm install --silent express axios mime morgan cors
RUN npm install --silent

COPY . ./
RUN sed -i "s/TOREPLACE/$SERVERNAME/" package.json

EXPOSE 3000
CMD ["node", "index.js"]
