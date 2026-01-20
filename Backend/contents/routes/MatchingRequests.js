const auth = require('../middlewares/jwtConfig');
const { DislikeModel } = require('../models/DislikeModel');
const LikeModel = require('../models/LikeModel');
const { MatchModel, ValdiateUnMatch } = require('../models/MatchModel');
const { Message } = require('../models/MessageModel');
const { UserModel } = require('../models/usersModel');

const router = require('express').Router();

router.get('/',auth, async (req, res) => {
    let user = await UserModel.findOne({_id: req.tokenData._id});
    try {
        let data = await UserModel.find({_id: { $ne: req.tokenData._id },
        gender: user.preferredGender
        });
        return res.json(data);
    } catch (err) {
        res.status(500).json({ msg: "err", err });
    }
    
});
router.post('/dislike', auth , async (req, res) => {


  let document = {
    user_id: req.tokenData._id,
    disliked_user_id: req.body._id
  };
  try {
    let dislikeCollection = new DislikeModel(document);
    await dislikeCollection.save();
    return res.status(201).send('success');
    
  } catch (error) {
    return res.sendStatus(500);
  }
  
});
router.post('/checkmatch',auth,async (req, res) => {
  // Check if User B already liked User A
  const likeSenderID = req.tokenData._id;
  const likeReciverID = req.body._id;
  const mutualLike = await LikeModel.findOne({ user_id: likeReciverID, liked_user_id: likeSenderID});
try {
  

  if (mutualLike) {
      // It's a match!
      await MatchModel.create([
          { user_id: likeSenderID, matched_user_id: likeReciverID },
          { user_id: likeReciverID, matched_user_id: likeSenderID }
      ]);

      // Clean up the like entries
      await LikeModel.deleteOne({ user_id: likeSenderID, liked_user_id: likeReciverID });
      await LikeModel.deleteOne({ user_id: likeReciverID, liked_user_id: likeSenderID });

      console.log("It's a match!");
      return res.status(200).json({match:true});
      
  } else {
      // No mutual like yet, just add the like
      await LikeModel.create({ user_id: likeSenderID, liked_user_id: likeReciverID });
      return res.status(200).json({match: false});
  }
} catch (error) {
  return res.sendStatus(500);
  
}
})
router.get('/getmatches', auth, async (req, res) => {
  const maxQueries = 10;
  let skips = parseInt(req.query.page);
  skips = isNaN(skips) || skips < 1 ? 1 : skips;
  try {
      // Get the IDs of matched users
      const matchedUserIds = await MatchModel.find({ user_id: req.tokenData._id })
          .distinct("matched_user_id");

      if (matchedUserIds.length === 0) {
          return res.status(404).json([]); // No matches, return empty array
      }

      // Fetch full user profiles of matched users
      const matchedUsers = await UserModel.find({ _id: { $in: matchedUserIds } })
      .limit(maxQueries).skip((skips - 1) * 10);//show 10 skip 0, show 10 skip 10 ,etc....
      
      return res.json(matchedUsers);
  } catch (err) {
      res.status(500).json({ msg: "err", err });
  }
});
router.post('/unmatch', auth, async (req, res) => {
  const userID = req.tokenData._id;
  const matchID = req.body._matchId;
  const userDB = await UserModel.findOne({_id:userID}).select("username");
  const matchDB = await UserModel.findOne({_id:matchID}).select("username");
  const { error } = ValdiateUnMatch(req.body);
  if(error){
    return res.status(400).json({error: error.details[0].message,approved: false});
  }
  try {
    let validReq = await MatchModel.findOne({
      user_id: userID,
      matched_user_id: matchID

    });
    if(validReq){
      await MatchModel.deleteMany({
        $or: [
          { user_id: userID, matched_user_id: matchID },
          { user_id: matchID, matched_user_id: userID }
        ]
      });
      await Message.deleteMany({
        $or: [
            { senderUsername: userDB.username, receiverUsername: matchDB.username },
            { senderUsername: matchDB.username, receiverUsername: userDB.username }
        ]
    });
      return res.status(200).json({approved:true});
    }
    return res.status(404).json({approved: false});
    
   
  } catch (error) {
    return res.sendStatus(500);
  }
  
});
router.get('/querygetmatches', auth, async (req, res) => {
  try {
    // 1. Get the user and validate
    const sourceUser = await UserModel.findById(req.tokenData._id);
    if (!sourceUser) return res.status(404).json({ msg: "User not found" });

    // 2. Set up pagination
    const limit = 10;
    const lastId = req.query.lastId || null; // If no lastId, start from the beginning
    // Fetch exclusion lists in parallel
    let excludeIds = await Promise.all([
      DislikeModel.find({ user_id: req.tokenData._id }).distinct("disliked_user_id"),
      MatchModel.find({ user_id: req.tokenData._id }).distinct("matched_user_id"),
      LikeModel.find({ user_id: req.tokenData._id }).distinct("liked_user_id")
  ]);

  // Combine and deduplicate
  let excludedArray = Array.from(new Set([...excludeIds[0], ...excludeIds[1], ...excludeIds[2]]));
  console.log(excludedArray);
  
    // 3. Build the query (same as your original filters)
    const query = {
      _id: { $ne: req.tokenData._id } , // "$ne" = "not equal" 
      gender: sourceUser.preferredGender,
      preferredGender: sourceUser.gender,
      age: { $gte: sourceUser.minPreferredAge, $lte: sourceUser.maxPreferredAge },
      ...(excludedArray.length > 0 && { _id: { $nin: excludedArray } }),
      // Add cursor if lastId exists
      ...(lastId && { _id: { $gt: lastId } }) // Only newer IDs than the last seen
    };

    // 4. Fetch users (sorted by _id for consistency)
    const users = await UserModel.find(query)
      .sort({ _id: 1 })  // Oldest first (ascending order)
      .limit(limit);

    // 5. Send response (no hasMore checks, no extra queries)
    res.json(users);

  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

router.post('/getMatchedProfile',auth, async (req, res) => {
  try {
    let isValidMatch = await MatchModel.findOne(
      {
        user_id: req.tokenData._id,
        matched_user_id: req.body.matchID
      });
      if(isValidMatch){
        let matched_user_id = await UserModel.findOne({_id: req.body.matchID});
        return res.status(200).json(matched_user_id);

      }
      return res.status(403).send(null);

  } catch (error) {
      return res.status(500).send(null);
  }
  
});
  module.exports = router;