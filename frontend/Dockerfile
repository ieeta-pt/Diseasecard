#####################################################################################
# Build and Run React Application
#####################################################################################

FROM node:alpine as build

ARG SERVERNAME
ARG REVERSEPROXY_PORT

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json ./
COPY package-lock.json ./
RUN npm install --silent
RUN npm install react-scripts@3.4.1 -g --silent
COPY . ./
RUN sed -i "s/TOREPLACE/$SERVERNAME:$REVERSEPROXY_PORT/" package.json
RUN npm run build


FROM nginx:alpine as prod
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 3000