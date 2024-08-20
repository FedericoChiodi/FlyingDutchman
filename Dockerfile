FROM tomcat:10.1.7-jdk17

RUN mkdir -p /home/sanpc/uploads

RUN rm -rf /usr/local/tomcat/webapps/*

WORKDIR /srv/flyingdutchman

COPY /uploads /home/sanpc/uploads

WORKDIR /home/sanpc/Desktop/flyingdutchman

COPY /target/flyingdutchman-0.0.1-SNAPSHOT /usr/local/tomcat/webapps

EXPOSE 8080

CMD [ "catalina.sh", "run" ]