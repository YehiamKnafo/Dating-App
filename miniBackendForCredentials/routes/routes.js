const auth = require('../middlewares/auth')
const { EXPRESS, CLOUDINARY_URL, APP_VERSION, WEBSITE_URL } = require('../secret/dotenvconf');
/**
 * @param {import('express').Application} app 
 */
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
app.get('/api/checkForUpdates', async (req, res) => {
  const currentClientVersion = req.query.v;
  const latestVersion = APP_VERSION;
if (currentClientVersion === latestVersion) {
    // 200 is fine, just tell the app 'no update needed'
    return res.status(200).json({ 
      updateAvailable: false, 
      msg: "You are running the latest version." 
    });
  }

  // New version found
  return res.status(200).json({
    updateAvailable: true,
    msg: "Update to " + latestVersion + " Ready"
  });
  
  

  
})


  

}