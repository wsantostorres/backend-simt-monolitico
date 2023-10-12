FROM openjdk:17

WORKDIR /backend/simt

COPY target/simt-0.0.1-SNAPSHOT.jar /backend/simt/simt.jar

ENTRYPOINT ["java", "-jar", "simt.jar"]