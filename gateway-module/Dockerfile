FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .

COPY student-module/pom.xml ./student-module/
COPY enrollment-module/pom.xml ./enrollment-module/
COPY gateway-module/pom.xml ./gateway-module/

RUN mvn dependency:go-offline -f gateway-module/pom.xml || true

COPY gateway-module/src ./gateway-module/src

RUN mvn clean package -DskipTests -pl gateway-module -am

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache wget

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /build/gateway-module/target/*.jar app.jar

USER spring

EXPOSE 8080

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]