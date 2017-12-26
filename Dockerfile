FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/price-search-2.5.0-SNAPSHOT.jar /app

EXPOSE 8083

CMD ["java", "-jar", "price-search-2.5.0-SNAPSHOT.jar"]