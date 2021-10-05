FROM openjdk:8-jdk-slim

ENV PORT 8080
ENV CLASSPATH /opt/lib

COPY target/flux-0.0.1-SNAPSHOT.jar /flux.jar

CMD ["sh", "-c", "java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap $APPLICATION_ARGS -jar /flux.jar"]