FROM ubuntu:14.04

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8

RUN echo "deb http://apt-archive.postgresql.org/pub/repos/apt/ precise-pgdg main 9.6" > /etc/apt/sources.list.d/pgdg.list

RUN apt-get update && apt-get -y -q install software-properties-common libjpeg-dev vim 

RUN apt-get install -y wget default-jre default-jdk 

RUN (cd /opt &&  wget http://archive.apache.org/dist/lucene/solr/4.7.2/solr-4.7.2.tgz && tar -xvf solr-4.7.2.tgz ) && ( cp -R /opt/solr-4.7.2/example /opt/solr)

CMD [ "-D", "FOREGROUND" ]
ENTRYPOINT cd opt/solr && java -jar start.jar



#################################
# docker build -t mysolr:4.7.2 .
# docker run -it --name solr -p 9999:8983 mysolr:4.7.2
# cd opt/solr && java -jar start.jar
# open browser: http://localhost:9999/solr/
# docker start solr 
