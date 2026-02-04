const auth = require('../middlewares/jwtConfig');
const { validatePicture, UserModel } = require('../models/usersModel');

const router = require('express').Router();

router.put('/addpicture/:id',auth, async (req, res) => {
  let idEdit = req.params.id;
  let { error } = validatePicture(req.body);
  if (error) {
      return res.status(400).json(error.details[0].message);
  }
  try {
      let data = await UserModel.updateOne({ _id: idEdit}, {$push:{pictures: req.body.url}});
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
  module.exports = router;