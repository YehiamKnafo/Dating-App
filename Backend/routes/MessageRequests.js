const auth = require('../middlewares/jwtConfig');
const { MatchModel } = require('../models/MatchModel');
const { Message } = require('../models/MessageModel');
const { UserModel } = require('../models/usersModel');

const router = require('express').Router();

async function getChatHistory(user1, user2) {
  console.log(`user1:${user1}\nuser2:${user2}`);
  
  return await Message.find({
    $or: [
      { senderUsername: user1, receiverUsername: user2 },
      { senderUsername: user2, receiverUsername: user1 }
    ]
  })
  .sort({ timestamp: 1 }); // 1 for ascending order
}
router.post('/fetchmessages', auth, async (req, res) => {
  try {
    let matchID = req.body.matchID;
    let userID = req.tokenData._id;
    const UserSender = await UserModel.findOne({_id: userID});
    const UserReciver = await UserModel.findOne({_id: matchID});
    let result = await getChatHistory(UserSender.username, UserReciver.username);
    // console.log(result);
    
    return res.status(200).json(result);
    
  } catch (error) {
    return res.status(500).send(null);
  }
  
});
router.get("/latest-messages", auth , async (req, res) => {
  try {
    const currentUser = req.tokenData._id; // Get current user from middleware
    const currentUserdb = await UserModel.findOne({_id: currentUser}); 

    const latestMessages = await Message.aggregate([
      // 1. Filter messages where currentUser is the sender or receiver
      {
        $match: {
          $or: [
            { senderUsername: currentUserdb.username },
            { receiverUsername: currentUserdb.username}
          ]
        }
      },
      // 2. Sort messages by timestamp in descending order (latest first)
      { $sort: { timestamp: -1 } },
      // 3. Group by match ID and keep only the latest message
      {
        $group: {
          _id: {
            matchId: {
              $cond: {
                if: { $lt: ["$senderUsername", "$receiverUsername"] },
                then: { $concat: ["$senderUsername", "_", "$receiverUsername"] },
                else: { $concat: ["$receiverUsername", "_", "$senderUsername"] }
              }
            }
          },
          lastMessage: { $first: "$$ROOT" } // Take the first (latest) message
        }
      },
      // 4. Replace the root to return only the last message object
      { $replaceRoot: { newRoot: "$lastMessage" } }
    ]);

    res.json(latestMessages); // Send the result as JSON
  } catch (error) {
    console.error("Error fetching latest messages:", error);
    res.status(500).json({ error: "Internal Server Error" });
  }
});
router.put("/update-counter",auth,  async (req, res) => {
  const { messageCounters } = req.body; // Example: { "matchedUserId123": 5, "matchedUserId456": 2 }
  const userId = req.tokenData._id; // Authenticated user's ID

  try {
      // Loop through each matched_user_id and update only the authenticated user's matches
      for (const matchedUserId in messageCounters) {
          const messageCount = messageCounters[matchedUserId];

          await MatchModel.updateOne(
              { user_id: userId, matched_user_id: matchedUserId }, // Match the authenticated user & their matched user
              { $set: { messageCounter: messageCount } }
          );
      }

      res.json({ success: true, message: "Message counters updated." });
  } catch (error) {
      res.status(500).json({ success: false, error: error.message });
  }
});
router.get("/unreadmessages",auth , async (req, res) => {
    const userID = req.tokenData._id;
    try {
      const data = await MatchModel.find({user_id: userID});
      return res.json(data);
      
    } catch (error) {
      console.log(error);
      return res.sendStatus(500);
      
    }

});
router.patch("/resetCounter/:matchid", auth, async (req, res) => {
  const matchID = req.params.matchid;
  const userID = req.tokenData._id;
  console.log("check messages/resetCounter/");
  
  try {
    await MatchModel.updateOne(
      { user_id: userID, matched_user_id: matchID },
      { 
        $set: { 
          messageCounter: 0,
          lastResetAt: new Date()  // Timestamp the reset
          
        }
      }
    );
    return res.sendStatus(201);
    
  } catch (error) {
    console.log(error);
    return res.sendStatus(500);
    
    
  }

});
module.exports = router;