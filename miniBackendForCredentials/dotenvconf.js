const dotenv = require('dotenv');
dotenv.config();
module.exports ={
    MONGO_URL:process.env.MONGO_URL,
    JWT_SECRET:process.env.JWT_SECRET,
    EXPRESS: process.env.EXPRESS ,
    CLOUDINARY_URL: process.env.CLOUDINARY_URL
}