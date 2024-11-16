# Etapa 1: Base para ejecutar k6
FROM loadimpact/k6:latest as k6-runner

# Crear un directorio de trabajo
WORKDIR /scripts

# Copiar el script de prueba al contenedor
COPY test.js /scripts/test.js

# Etapa 2: Contenedor final
FROM alpine:latest

# Instalar bash para comandos
RUN apk add --no-cache bash

# Copiar k6 desde la etapa anterior
COPY --from=k6-runner /usr/bin/k6 /usr/bin/k6

# Crear un directorio de trabajo
WORKDIR /scripts

# Copiar el script de prueba al contenedor
COPY test.js /scripts/test.js

# Comando por defecto
CMD ["sh"]
