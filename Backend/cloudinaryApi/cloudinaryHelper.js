const { CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, CLOUDINARY_API_SECRET } = require('../secret/secretConf');
const cloudinary = require('cloudinary').v2;
cloudinary.config({
  cloud_name: CLOUDINARY_CLOUD_NAME,
  api_key: CLOUDINARY_API_KEY,
  api_secret: CLOUDINARY_API_SECRET,
  secure: true // Ensures you use HTTPS
});
// Helper to extract Public ID from a URL
const getPublicIdFromUrl = (url) => {
  // Example URL: https://res.cloudinary.com/demo/image/upload/v12345/folder/sample.jpg
  const parts = url.split('/');
  const lastPart = parts.pop(); // "sample.jpg"
  const publicId = lastPart.split('.')[0]; // "sample"
  
  // If your images are in folders, you might need the folder name too:
  // const folderPath = parts.slice(parts.indexOf('upload') + 2).join('/');
  // return folderPath ? `${folderPath}/${publicId}` : publicId;
  
  return publicId;
};
module.exports = getPublicIdFromUrl;