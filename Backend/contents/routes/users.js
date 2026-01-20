// const express = require('express');
// const router = express.Router();
// const bcrypt = require('bcrypt');
// const { UserModel, validateSignup, validateLogin ,createToken,validateProfileUpdate, validatePicture} = require('../models/usersModel');
// const auth = require('../middlewares/auth.js');
// const { DislikeModel } = require('../models/DislikeModel.js');
// const LikeModel = require('../models/LikeModel.js');
// const { MatchModel, ValdiateUnMatch } = require('../models/MatchModel.js');
// const { Message } = require('../models/MessageModel.js');










// // router.delete('/:idDel', async (req, res) => {
// //     try {
// //         let idDel = req.params.idDel;
// //         let data = await UserModel.deleteOne({ _id: idDel });
// //         res.json(data);
// //     } catch (err) {
// //         console.log(err);
// //         res.status(500).json(data)

// //     }
// // });

// router.post('/signup/bulk', async (req, res) => {
//   try {
//     const users = req.body; // Expecting an array of users
    
//     if (!Array.isArray(users)) {
//       return res.status(400).json({ error: "Expected an array of users" });
//     }

//     // Hash passwords for all users
//     const usersWithHashedPasswords = await Promise.all(
//       users.map(async user => {
//         let { error } = validateSignup(user);
//         if (error) throw error;
        
//         const hashedPassword = await bcrypt.hash(user.password, 12);
//         return { ...user, password: hashedPassword };
//       })
//     );

//     // Insert all users
//     const result = await UserModel.insertMany(usersWithHashedPasswords);
//     return res.status(201).json({ msg: "success", count: result.length });
//   } catch (error) {
//     return res.status(400).json({ error: error.message });
//   }
// });









// module.exports = router;