FROM tomcat:10.1.26-jdk17

COPY target/flyingdutchman-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD [ "catalina.sh", "run" ]