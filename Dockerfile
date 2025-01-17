# our base build image
FROM maven:3.8.1-jdk-8 as build

# set working directory
WORKDIR /app

# copy the project files
COPY ./pom.xml ./pom.xml
COPY ./src ./src

# build for release
RUN mvn clean package

# our final base image
FROM maven:3.8.1-jdk-8

# set deployment directory
WORKDIR /eCommerce

# copy over the built artifact from the maven image
COPY --from=build /app/target/*.jar ./eCommerce-1.0.jar

# set the startup command to run your binary
CMD ["java", "-jar", "eCommerce-1.0.jar"]