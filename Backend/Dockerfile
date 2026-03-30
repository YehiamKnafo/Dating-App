FROM node:22.22-alpine
# Set working directory
WORKDIR /app

# Copy package files first (for caching)
COPY ./package*.json ./

# Install dependencies
RUN npm ci

# Copy the rest of the app
COPY ./secret ./secret
COPY ./db ./db
COPY ./models ./models
COPY ./middlewares ./middlewares
COPY ./routes ./routes
COPY ./socketio ./socketio
COPY ./cloudinaryApi ./cloudinaryApi
COPY ./emailApi ./emailApi
COPY ./app.js ./


# Command to run the app (adjust to your entry file)
CMD  ["npm" , "start"]