From openjdk:8
ADD target/transfer-0.0.1-SNAPSHOT.jar docker-spring-boot.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","docker-spring-boot.jar"]