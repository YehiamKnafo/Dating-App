const auth = require('../middlewares/jwtConfig');
const {UserModel, validateProfileUpdate, validatePicture } = require('../models/usersModel');
const { Message } = require("../models/MessageModel");
const LikeModel = require('../models/LikeModel');
const { MatchModel } = require('../models/MatchModel');
const { DislikeModel } = require('../models/DislikeModel');
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
  try {
    const updates = {};
    const { birthDate, age, gender, preferredGender, bio, minPreferredAge, maxPreferredAge, firstName, lastName } = req.body;

    // Only update fields that are provided
    if (birthDate !== undefined) updates.birthDate = birthDate;
    if (age !== undefined && age > 17) updates.age = age;
    if (gender !== undefined) updates.gender = gender;
    if (preferredGender !== undefined) updates.preferredGender = preferredGender;
    if (bio !== undefined && bio !== "") updates.bio = bio;
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
    console.log(typeof req.body.url); // This should log 'string'
    
    
    const { error } = validatePicture(req.body.url);
    if (error) {
      console.log(error);
      
      return res.status(403).json({err: error.details[0].message});
      
    }
    try {
      await UserModel.updateOne(
        {_id: req.tokenData._id},
        {$set:{profilePicture: req.body.url}}
      );
      return res.sendStatus(200);
      
      
      
    } catch (error) {
      return res.sendStatus(500);

      
    }

});
router.delete("/deleteUser", auth, async (req, res) => {
  try {
    const myId = req.tokenData._id;

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
      message: "Account and all associated data wiped successfully"
    });

  } catch (error) {
    console.log(error);
    return res.status(500).json({ status: false, message: "Internal Server Error" });
  }
});
module.exports = router;  