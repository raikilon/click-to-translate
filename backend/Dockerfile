FROM eclipse-temurin:25-jdk AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies first for better layer caching.
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -q -DskipTests clean package

FROM eclipse-temurin:25-jre AS runtime

WORKDIR /app

COPY --from=build /workspace/target/clicktotranslate-*.jar /app/app.jar

EXPOSE 8080

# Spring Boot will also load external files from /config when mounted.
ENV SPRING_CONFIG_ADDITIONAL_LOCATION=optional:file:/config/

ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/app.jar"]
