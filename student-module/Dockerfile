FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .

COPY student-module/pom.xml ./student-module/
COPY enrollment-module/pom.xml ./enrollment-module/
COPY gateway-module/pom.xml ./gateway-module/

RUN mvn dependency:go-offline -f student-module/pom.xml || true

COPY student-module/src ./student-module/src

RUN mvn clean package -DskipTests -pl student-module -am

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN apk add --no-cache wget

RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /build/student-module/target/*.jar app.jar

USER spring

EXPOSE 8081

ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]