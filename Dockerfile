# we are extending everything from tomcat:8.0 image
FROM tomcat:8.0.51-jre8-alpine

MAINTAINER dermacon

# COPY path-to-your-application-war path-to-webapps-in-docker-tomcat
COPY ./target/workshop-organizer.war /usr/local/tomcat/webapps/workshop-organizer.war

EXPOSE 8080

CMD ["catalina.sh", "run"]