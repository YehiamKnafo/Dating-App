# FROM node:22.22-alpine
# WORKDIR /app
# COPY package.json package-lock.json ./
# RUN npm install
# COPY db /app/db
# COPY secret /app/secret
# COPY middlewares /app/middlewares
# COPY routes /app/routes
# COPY app.js ./


# CMD ["npm","start"]
# --- STAGE 1: Build the Website ---
FROM node:22.22-alpine AS website-builder
WORKDIR /build-website
ARG APP_VERSION
ARG VERSION_TYPE
ENV VITE_APP_VERSION=$APP_VERSION 
ENV VITE_VERSION_TYPE=$VERSION_TYPE
COPY website/package*.json ./
RUN npm install

# Copy website source and build it
COPY website/ ./
RUN npm run build 

# --- STAGE 2: The Production Runner ---
FROM node:22.22-alpine
WORKDIR /app
ARG APP_VERSION
ENV APP_VERSION=$APP_VERSION
# Install only production dependencies for the backend
COPY package.json package-lock.json ./
RUN npm install --production

# Copy your backend folders exactly as they are in your repo
COPY db ./db
COPY secret ./secret
COPY middlewares ./middlewares
COPY routes ./routes
COPY app.js ./

# Copy the static assets from the builder stage into a 'public' folder
COPY --from=website-builder /build-website/dist ./public


EXPOSE 10000

CMD ["node", "app.js"]