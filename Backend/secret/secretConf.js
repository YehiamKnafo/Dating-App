const dotenv = require('dotenv');
dotenv.config();

module.exports = {
    EXPRESS_PORT: Number(process.env.PORT) || 3000,
    JWT_SECRET: process.env.JWT_SECRET,
    MONGO_URL: process.env.MONGO_URL,
    GMAIL_SMTP_EMAIL: process.env.GMAIL_SMTP_EMAIL,
    GMAIL_SMTP_PASSWORD: process.env.GMAIL_SMTP_PASSWORD
};
