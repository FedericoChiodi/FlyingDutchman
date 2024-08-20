FROM tomcat:10.1.7-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

WORKDIR /home/sanpc/Desktop/flyingdutchman

COPY target/flyingdutchman-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD [ "catalina.sh", "run" ]