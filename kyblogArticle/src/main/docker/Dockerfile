FROM java:8
VOLUME /tmp
ADD kyblogArticle-1.0-SNAPSHOT.jar /article-service-7002.jar
EXPOSE 7002
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/article-service-7002.jar"]
