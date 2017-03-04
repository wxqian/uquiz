FROM java
RUN mkdir -p /u01/log
RUN mkdir -p /u01/file
VOLUME /tmp
ADD uquiz-1.0-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]