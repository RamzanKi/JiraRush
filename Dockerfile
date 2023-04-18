FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY ./resources/view /resources/view
COPY ./resources/mails /resources/mails
COPY ./resources/static /resources/static

ENTRYPOINT ["java","-jar","/app.jar"]
#COPY src/main/resources/logback-spring.xml logback-spring.xml

#COPY src/main/resources/mails /app/mails
#COPY src/main/resources/static /app/static
#COPY src/main/resources/view /app/view

#COPY src/main/resources/db/changelog.sql changelog.sql

#COPY resources/view /app/resources/view
#COPY resources/mails /app/resources/mails

