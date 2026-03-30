const auth = require('../middlewares/jwtConfig');
const { validatePicture, UserModel, validatePictureChange } = require('../models/usersModel');

const router = require('express').Router();

router.put('/addpicture/:id',auth, async (req, res) => {
  const myId = req.tokenData._id;
  let { error } = validatePicture(req.body);
  if (error) {
      return res.status(400).json(error.details[0].message);
  }
  try {
      let data = await UserModel.updateOne({ _id: myId}, {$push:{pictures: req.body.url}});
      res.json(data);

  } catch (err) {
      console.log(err);
      res.status(500).json({ msg: "err : ", err });

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
router.put("/changepic", auth, async (req, res) => {
    const { oldUrl, newUrl } = req.body; // Expecting both
    const { error } = validatePictureChange(req.body);
      if (error) {
      console.log(error);
      
      return res.status(403).json({err: error.details[0].message});
      
    }
    try {
        const result = await UserModel.updateOne(
            { 
                _id: req.tokenData._id, 
                pictures: oldUrl 
            },
            { 
                $set: { "pictures.$": newUrl } 
            }
        );

        if (result.matchedCount === 0) {
            return res.status(404).json({ err: "Old URL not found in user's list" });
        }

        return res.sendStatus(200);
    } catch (error) {
        console.error(error);
        return res.sendStatus(500);
    }
});

  module.exports = router;