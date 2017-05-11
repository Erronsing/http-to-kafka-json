FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/rt-server.jar /rt-server/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/rt-server/app.jar"]
