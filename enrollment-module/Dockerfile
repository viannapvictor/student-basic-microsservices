FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .

COPY student-module/pom.xml ./student-module/
COPY enrollment-module/pom.xml ./enrollment-module/
COPY gateway-module/pom.xml ./gateway-module/

RUN mvn dependency:go-offline -f enrollment-module/pom.xml || true

COPY enrollment-module/src ./enrollment-module/src

RUN mvn clean package -DskipTests -pl enrollment-module -am

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache wget

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /build/enrollment-module/target/*.jar app.jar

USER spring

EXPOSE 8082

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]