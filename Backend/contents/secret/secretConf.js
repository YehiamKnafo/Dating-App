const dotenv = require('dotenv');
dotenv.config();

module.exports = {
    EXPRESS_PORT : parseInt(process.env.EXPRESS?.split(":")[2]) || 0,
    JWT_SECRET: process.env.JWT_SECRET,
    SOCKET_PORT: parseInt(process.env.TCP?.split(":")[1]) || 0,
    MONGO_URL: process.env.MONGO_URL,
    GMAIL_SMTP_EMAIL: process.env.GMAIL_SMTP_EMAIL,
    GMAIL_SMTP_PASSWORD: process.env.GMAIL_SMTP_PASSWORD

}
