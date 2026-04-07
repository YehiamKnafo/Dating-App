const auth = require('../middlewares/auth')
const { EXPRESS, CLOUDINARY_URL } = require('../secret/dotenvconf');

module.exports = function(app){
  app.get('/health', (req, res) => {
  res.status(200).send('Server is up and running');
});
app.get('/api/config', auth , async(_req, res) => {
  try {
      const credentials ={
        expressUrl: EXPRESS,
        cloudinaryUrl: CLOUDINARY_URL
      }
      return res.status(200).json(credentials);
    
  } catch (error) {
    return res.statusCode(500);
    
  }  
});

  

}