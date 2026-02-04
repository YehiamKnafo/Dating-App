const auth = require('./auth')
const bcrypt = require('bcrypt');
const { UserModel, createToken, validateLogin } = require('./models/usersModel');
const { EXPRESS, CLOUDINARY_URL } = require('./dotenvconf');

module.exports = function(app){
app.get('/',auth, (_req, res) => {
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
  app.post("/login" , async (req, res) => {
      // console.log(req.body);
      // console.log("check");
      
      let { error } = validateLogin(req.body);
      if (error) {
        return res.status(400).json({err: error});
        
      }
      try {
        let user = await UserModel.findOne({username: req.body.username})
        if (!user) {
          return res.status(401).json({ msg: "Password or email is worng ,code:1" ,token:""});
        }
        let Password = await bcrypt.compare(req.body.password, user.password);
        
        if (!Password) {
          return res.status(401).json({ msg: "Password or email is wrong ,code:2",token:"" });
          
        }
        return res.status(200).json({msg: "login succesful!!!",token:createToken(user._id),_id: user._id});
      }
      catch (err) {
        console.log(err)
        return res.status(500).json({ msg: "catch err", token:""})
      }
    });
  

}