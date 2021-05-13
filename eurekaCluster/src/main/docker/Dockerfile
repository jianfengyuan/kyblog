FROM java:8
VOLUME /tmp
ADD eurekaCluster-1.0-SNAPSHOT.jar /eureka-server-8001.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/eureka-server-8001.jar"]