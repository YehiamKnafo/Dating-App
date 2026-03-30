const dotenv = require('dotenv');
dotenv.config();

module.exports = {
    EXPRESS_PORT: Number(process.env.PORT) || 3000,
    JWT_SECRET: process.env.JWT_SECRET,
    MONGO_URL: process.env.MONGO_URL,
    ALLOWED_ORIGIN: process.env.ALLOWED_ORIGIN,
    MY_BREVO_EMAIL:process.env.MY_BREVO_EMAIL,
    BREVO_API_KEY: process.env.BREVO_API_KEY,
    REDIS_URL: process.env.REDIS_URL,
    CLOUDINARY_API_SECRET: process.env.CLOUDINARY_API_SECRET,
    CLOUDINARY_API_KEY: process.env.CLOUDINARY_API_KEY,
    CLOUDINARY_CLOUD_NAME: process.env.CLOUDINARY_CLOUD_NAME

    

};
