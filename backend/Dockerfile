#####################################################################################
# Build Spring Boot Application
#####################################################################################
FROM maven:3.6.1-jdk-8-alpine as builder

COPY . ./diseasecard
WORKDIR /diseasecard

RUN mvn clean package -DskipTests 


#####################################################################################
# Run Application
#####################################################################################
FROM openjdk:8-jdk-alpine

COPY --from=builder ./diseasecard/target/diseasecard-0.0.1-SNAPSHOT.jar diseasecard-0.0.1-SNAPSHOT.jar
COPY --from=builder ./diseasecard/src/main/resources/configuration /configuration
RUN mkdir -p /submittedFiles/endpoints/

# Add docker-compose-wait tool -------------------
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

CMD /wait && java -jar /diseasecard-0.0.1-SNAPSHOT.jar -javaagent:spring-instrument-{version}.jar
#ENTRYPOINT ["java","-jar","/diseasecard-0.0.1-SNAPSHOT.jar"]