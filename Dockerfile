FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/gestionchambre-1.0.0.jar app.jar

EXPOSE 8090

# Étape 5 : lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
