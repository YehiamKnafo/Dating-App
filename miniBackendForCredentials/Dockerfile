FROM node:22.22-alpine
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY db /app/db
COPY secret /app/secret
COPY middlewares /app/middlewares
COPY routes /app/routes
COPY app.js ./


CMD ["npm","start"]