FROM openjdk:8-jdk AS build

COPY . /app

ARG DB_HOST
ARG DB_PORT
ARG DB_NAME
ARG DB_USERNAME
ARG DB_PASSWORD
ENV DB_HOST=$DB_HOST
ENV DB_PORT=$DB_PORT
ENV DB_NAME=$DB_NAME
ENV DB_USERNAME=$DB_USERNAME
ENV DB_PASSWORD=$DB_PASSWORD

RUN echo "DB_HOST value: $DB_HOST"
RUN echo "DB_HOST value: $DB_PORT"
RUN echo "DB_HOST value: $DB_NAME"
RUN echo "DB_HOST value: $DB_USERNAME"
RUN echo "DB_HOST value: $DB_PASSWORD"

WORKDIR /app

RUN ./mvnw clean package -DskipTests

RUN chmod +x /app/target/banking-transaction-management.jar

ENTRYPOINT ["java", "-jar", "/app/target/banking-transaction-management.jar"]