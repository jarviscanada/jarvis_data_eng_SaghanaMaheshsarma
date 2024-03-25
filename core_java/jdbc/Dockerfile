FROM amazoncorretto:11-alpine3.19
COPY target/jdbc-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/app/jdbc/lib/jdbc.jar
COPY src/main/resources/properties.txt /usr/local/app/jdbc/resources/properties.txt
COPY src/main/resources/logback.xml /usr/local/app/jdbc/resources/logback.xml
ENTRYPOINT ["java","-jar","/usr/local/app/jdbc/lib/jdbc.jar"]