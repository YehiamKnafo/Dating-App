const dotenv = require('dotenv');
dotenv.config();

module.exports = {
    SOCKET_PORT: Number(process.env.SOCKET_PORT) || 4000,
    MONGO_URL: process.env.MONGO_URL
  
};
