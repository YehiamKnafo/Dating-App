FROM node
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY db /app/db
COPY models /app/models
COPY auth.js ./
COPY routes.js ./
COPY app.js ./


CMD ["npm","start"]