const auth = require('../middlewares/jwtConfig');
const { UserModel, validateProfileUpdate } = require('../models/usersModel');

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
  
})
module.exports = router;  