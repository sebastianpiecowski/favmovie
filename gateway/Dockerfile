FROM openjdk:8u171
WORKDIR /opt/javaSrv
CMD java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=n -jar app.jar
EXPOSE 8080
COPY build/libs/gateway-0.0.1.jar app.jar
