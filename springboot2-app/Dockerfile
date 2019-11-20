FROM openjdk:11.0.5-jdk-slim as BUILDER

WORKDIR /workdir

# Dependencies
COPY .mvn /workdir/.mvn
COPY pom.xml /workdir/pom.xml
COPY mvnw /workdir/mvnw
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Project sources
COPY src /workdir/src
RUN ./mvnw package -DskipTests

FROM openjdk:11-jre-slim

RUN groupadd -g 999 springboot && \
    useradd -r -u 999 -g springboot springboot
USER springboot

COPY --from=BUILDER /workdir/target/*.jar /springboot2-app.jar

EXPOSE 8080

CMD ["java", "-jar", "/springboot2-app.jar"]
