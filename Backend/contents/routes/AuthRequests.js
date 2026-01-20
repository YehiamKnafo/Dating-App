const { OtpModel, OtpValidation } = require('../models/OtpModel');
const { UserModel, validateSignup, createToken, validateLogin } = require('../models/usersModel');
const sendOtp = require('../NodemailerConfig.js');
const router = require('express').Router();
const bcrypt = require('bcrypt');
router.post("/", async (req, res) => {
    let { error } = validateSignup(req.body);
    
    if (error) {
      return res.status(400).json(error.details[0].message);
    }
    try {
      let details = req.body;

      details.password = await bcrypt.hash(details.password, 12);
      let user = new UserModel(details);
      await user.save();
      details.password = "******";
      res.status(201).json(details);
    }
    catch (err) {
      if (err.code == 11000) {
        return res.status(500).json({ msg: "Email already in system, try log in", code: 11000 })
  
      }
      console.log(err);
      res.status(500).json({ msg: "err", err })
    }
  });
  router.post('/signup', async (req, res) => {
        let { error } = validateSignup(req.body);
        if (error){
          return res.status(400).json({err: error});
          
        }
        
          let user = req.body;
          user.password = await bcrypt.hash(user.password, 12);
          let db = new UserModel(user);
          await db.save();
          return res.status(201).json({msg: "success"});
          
  
        
    
        // const { firstName, lastName, age, email, username,pictures, birthDate,gender, password } = req.body;
  
       
  
  
  });
  router.post("/login" , async (req, res) => {
      // console.log(req.body);
      
      let { error } = validateLogin(req.body);
      if (error) {
        return res.status(400).json({err: error});
        
      }
      try {
        let user = await UserModel.findOne({ username: req.body.username })
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
  
// Route to send OTP
router.post('/send-otp', async (req, res) => {
    const { email, type } = req.body;
  
    if (!email) {
      return res.status(400).send('Email is required');
    }
    try {
      let otpObj;
      let otpStorage = {
        email: email
      }; 
      let otp;
      let db = await UserModel.findOne({email: email});
      if(type == "reset-otp"){
        if(!db) return res.status(404).send('account is not exist');
        otp = await sendOtp(email); 
        otpStorage.otp = otp;
        otpObj = new OtpModel(otpStorage);
        await otpObj.save();
     
      }
      else if(type == "signup-otp"){
  
        if(db) return res.status(403).send('email exist');
        otp = await sendOtp(email); 
        otpStorage.otp = otp; // Save OTP for verification
        otpObj = new OtpModel(otpStorage);
        await otpObj.save();
      }
      return res.status(200).send('valid');
    } catch (error) {
      return res.status(500).send('Failed to send OTP');
    }
  });
  
  // Route to verify OTP
  router.post('/verify-otp', async (req, res) => {
    const { email, otp } = req.body;
    try {
      const { error } = OtpValidation(req.body);
    
      if(error){
        return res.status(401).send(error.details[0].message);
      }
      // Check if OTP is a valid numeric string (e.g., "123456")
      if (typeof otp === 'string' && /^\d+$/.test(otp)) {
        const deletedOtp = await OtpModel.findOneAndDelete({ email, otp });

        if (deletedOtp) {
            res.status(200).send('OTP verified successfully');
        } else {
            res.status(400).send('Invalid OTP'); // No matching OTP found
        }
      } else {
          res.status(400).send('OTP must be a numeric code'); // Invalid format
      }
    
    } catch (error) {
      console.log(error);
      return res.sendStatus(500);
      
      
    }
    
 
  });
  router.put('/resetpassword', async(req, res)=>{
    const newPassword = req.body.password;
    const email = req.body.email;
    try {
      const user = await UserModel.findOne({email: email});
      if(user){
        let passwordMatch = await bcrypt.compare(newPassword, user.password);
        if(passwordMatch) return res.sendStatus(403);
  
        const hashedPass = await bcrypt.hash(newPassword, 12);
        user.password = hashedPass; 
        await user.save();
      }
      else{
        return res.sendStatus(404);
      }
      return res.sendStatus(201);
  
      
    } catch (error) {
      console.log(error);
      
      return res.sendStatus(500);
      
    }
  });
  module.exports = router;