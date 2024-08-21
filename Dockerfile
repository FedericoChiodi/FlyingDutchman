FROM tomcat:10.1.24-jdk17

RUN cp -r $CATALINA_HOME/webapps.dist/* $CATALINA_HOME/webapps

COPY target/flyingdutchman-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD [ "catalina.sh", "run" ]