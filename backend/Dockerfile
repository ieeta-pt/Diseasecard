#####################################################################################
# Build Spring Boot Application
#####################################################################################

FROM adoptopenjdk:11-jre-hotspot as builder

RUN mkdir -p /srv/app/diseasecard-backend
WORKDIR /srv/app/diseasecard-backend

COPY . /srv/app/diseasecard-backend
RUN mvn clean package

# Layered Jars (https://www.baeldung.com/spring-boot-docker-images)
RUN java -Djarmode=layertools -jar bakcend.jar extract


#####################################################################################
# Run Application
#####################################################################################

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]