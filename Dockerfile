FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/superhost-2.5.0-SNAPSHOT.jar /app

EXPOSE 8085

CMD ["java", "-jar", "superhost-2.5.0-SNAPSHOT.jar"]