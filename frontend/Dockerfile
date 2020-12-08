#####################################################################################
# Build and Run React Application
#####################################################################################

FROM node:15.3.0-alpine3.10

RUN mkdir -p /srv/app/diseasecard-frontend
WORKDIR /srv/app/diseasecard-frontend

COPY package.json /srv/app/diseasecard-frontend
COPY package-lock.json /srv/app/diseasecard-frontend

RUN npm install

COPY . /srv/app/diseasecard-frontend

CMD ["npm", "start"]