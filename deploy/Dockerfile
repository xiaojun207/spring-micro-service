FROM openjdk:16-alpine
WORKDIR /app
COPY deploy/entrypoint.sh /app/entrypoint.sh
ENTRYPOINT ["sh", "/app/entrypoint.sh"]
ARG jarPath
ARG port
COPY $jarPath /app/app.jar
EXPOSE $port
