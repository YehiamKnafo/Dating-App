# Use official Node.js v20.14.0 image
FROM node:20.14.0-alpine

# Set working directory
WORKDIR /app

# Copy package files first (for caching)
COPY ./package*.json ./

# Install dependencies
RUN npm ci

# Copy the rest of the app
COPY ./db ./db
COPY ./public ./public
COPY ./middlewares ./middlewares
COPY ./models ./models
COPY ./routes ./routes
COPY ./app.js ./
COPY ./NodemailerConfig.js ./


# Command to run the app (adjust to your entry file)
CMD  ["npm" , "start"]