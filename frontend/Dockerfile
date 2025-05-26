FROM node:18-alpine

WORKDIR /app

# Instala dependencias primero (caché eficiente)
COPY package.json package-lock.json ./
RUN npm install

# Copia configuración (no montada por volumen)
COPY tsconfig.app.json ./
COPY tsconfig.json ./
COPY tsconfig.spec.json ./

# El código fuente se montará via volumen
EXPOSE 4200

CMD ["ng", "serve", "--host", "0.0.0.0", "--port", "4200"]

