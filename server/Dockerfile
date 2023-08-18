# build stage
FROM maven:3.9-amazoncorretto-17 AS build

# Set the working directory
WORKDIR /app

# Copy the project code into the container
COPY . .

# Install dependencies
RUN mvn clean install

# run stage
FROM tomcat:10.0.27-jre17

# Copy the war from the target folder from your build stage (step 4) to the webapps directory of tomcat server
COPY --from=build /app/target/MES-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/mes.war

# Expose port 8080
EXPOSE 8080