FROM adoptopenjdk/openjdk11:ubi
ENV TZ="Asia/Kolkata"
COPY ./target/simulator-1.0.jar /c.jar
ENTRYPOINT ["java","-jar","/c.jar"]
