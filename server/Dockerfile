# build stage
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DwarName=mes

# run stage
FROM tomcat:10.0.27-jre17

COPY --from=build /app/target/mes.war $CATALINA_HOME/webapps/

# Expose port 8080
EXPOSE 8080