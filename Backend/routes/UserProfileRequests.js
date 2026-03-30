const auth = require('../middlewares/jwtConfig');
const {UserModel, validateProfileUpdate, validatePicture, validateDateOfBirth } = require('../models/usersModel');
const { Message } = require("../models/MessageModel");
const LikeModel = require('../models/LikeModel');
const { MatchModel } = require('../models/MatchModel');
const { DislikeModel } = require('../models/DislikeModel');
const cloudinary = require('cloudinary').v2;
const getPublicIdFromUrl = require('../cloudinaryApi/cloudinaryHelper');
const router = require('express').Router();


router.get('/', auth ,async (req, res) => {
  try {
    // console.log(req.tokenData);
    let user = await UserModel.findOne({_id: req.tokenData._id});
    if(user){
      return res.status(201).json(user);
    }
    else{
      return res.status(401).json({err: "cannot fetch user"});
    }
  } catch (error) {
    return res.status(500).json({err: error});
    
  }
});
router.put('/updateprofile/:id',auth, async (req, res) => {
  
  
    let idEdit = req.params.id;
    let { error } = validateProfileUpdate(req.body);
    if (error) {
        return res.status(400).json(error.details[0].message);
    }
    try {
        let data = await UserModel.updateOne({ _id: idEdit}, {$set:{
           firstName: req.body.firstName,
           age: req.body.age,
           bio: req.body.bio
          }});
        res.json(data);

    } catch (err) {
        console.log(err);
        res.status(500).json({ msg: "err : ", err });

    }
});

router.patch("/updatePreferrences", auth, async (req, res) => {
  const validationObject = {
    birthDate: req.body.birthDate
  }
  const {error} = validateDateOfBirth(validationObject);
  if (error) {
      return res.status(400).json(error.details[0].message);
}
  try {
    const updates = {};
    const { birthDate, age, gender, preferredGender, bio, minPreferredAge, maxPreferredAge, firstName, lastName } = req.body;

    // Only update fields that are provided
    if (birthDate !== undefined) updates.birthDate = birthDate;
    if (age !== undefined && age > 17) updates.age = age;
    if (gender !== undefined) updates.gender = gender;
    if (preferredGender !== undefined) updates.preferredGender = preferredGender;
    if (bio !== undefined) updates.bio = bio;
    if (minPreferredAge !== undefined) updates.minPreferredAge = minPreferredAge;
    if (maxPreferredAge !== undefined) updates.maxPreferredAge = maxPreferredAge;
    if (firstName !== undefined && firstName !== "") updates.firstName = firstName;
    if (lastName !== undefined && lastName !== "") updates.lastName = lastName;
    console.log(updates);
    
    if (Object.keys(updates).length === 0) {
      return res.status(400).json({ error: "No valid fields provided for update" });
    }

    await UserModel.updateOne({ _id: req.tokenData._id }, { $set: updates });
    return res.sendStatus(200);
  } catch (error) {
    console.error(error);
    return res.sendStatus(500);
  }
});
router.patch('/updateBio', auth, async (req, res) => {
  try {
   
    const { bio } = req.body;
    if (bio !== undefined) {
      await UserModel.updateOne({ _id: req.tokenData._id }, { $set: {bio: bio} });
      return res.sendStatus(200);
    }
    else
      return res.status(400).json({ error: "No valid fields provided for update" });

  } catch (error) {
      console.error(error);
      return res.sendStatus(500);
  }
  
});
router.put("/changeprofilepic", auth, async (req, res) => {
    const url = req.query.url   
    const obj  ={
      url
    } 
    
    const { error } = validatePicture(obj);
    if (error) {
      console.log(error);
      
      return res.status(403).json({err: error.details[0].message});
      
    }
    try {
      await UserModel.updateOne(
        {_id: req.tokenData._id},
        {$set:{profilePicture: url}}
      );
      return res.sendStatus(200);
      
      
      
    } catch (error) {
      return res.sendStatus(500);

      
    }

});
router.delete("/deletepicture", auth, async(req, res)=>{
  const myId = req.tokenData._id;
  const urlToDelete = req.query.url;
  const obj = {
    url: urlToDelete
  }
  const { error } = validatePicture(obj);
    if (error) {
      console.log(error);
      
      return res.status(403).json({err: error.details[0].message});
      
    }
    try {
      await UserModel.findByIdAndUpdate(myId,
        {$pull:{pictures: urlToDelete}}
      );
      return res.sendStatus(200);
    } catch (error) {
      console.log(error);
      
      return res.sendStatus(500);
      
    }

});
router.delete("/deleteUser", auth, async (req, res) => {
  try {
    const myId = req.tokenData._id;
    const profile = await UserModel.findOne({_id: myId});
    console.log(profile);
    
    const pictureList = profile.pictures;
    const profilePic = profile.profilePicture;
    const allPics = [...new Set([...pictureList, profilePic].filter(Boolean))]; 
    allPics.forEach(async (url) => {
      const publicId = getPublicIdFromUrl(url);
      if (publicId) await cloudinary.uploader.destroy(publicId);
    });   
    // 1. Clean up all associated data first
    await Promise.all([
      Message.deleteMany({ $or: [{ senderID: myId }, { receiverID: myId }] }),
      LikeModel.deleteMany({ $or: [{ user_id: myId }, { liked_user_id: myId }] }),
      MatchModel.deleteMany({ $or: [{ user_id: myId }, { matched_user_id: myId }] }),
      DislikeModel.deleteMany({ $or: [{ user_id: myId }, { disliked_user_id: myId }] })
    ]);

    // 2. Finally, delete the actual user
    const userResult = await UserModel.deleteOne({ _id: myId });

    if (userResult.deletedCount === 0) {
      return res.status(404).json({ status: false, message: "User already deleted or not found" });
    }

    return res.status(200).json({
      status: true,
      message: "Account and all associated data wiped successfully",
      
    });

  } catch (error) {
    console.log(error);
    return res.status(500).json({ status: false, message: "Internal Server Error" });
  }
});
module.exports = router;  