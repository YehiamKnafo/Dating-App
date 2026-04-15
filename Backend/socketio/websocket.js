const { MatchModel } = require('../models/MatchModel.js');
const { UserModel } = require('../models/usersModel.js');
const { Message } = require('../models/MessageModel.js');
/**
 * @param {import('socket.io').Server} io
 */
module.exports = function(io){
// server-side
io.on("connection",(socket) => {
  let messageObj = {};


    socket.on("Login", async (userId, callback) => {
        // 1. Tag this socket immediately so other "Login" calls can see it
        socket.userId = userId; 

        // 2. Fetch all sockets that are ALREADY in this user's private room
        const roomName = `user:${userId}`;
        const existingSockets = await io.in(roomName).fetchSockets();

        // 3. Kick EVERYONE else in that room
        for (const existingSocket of existingSockets) {
            if (existingSocket.id !== socket.id) {
                console.log(`Clearing zombie session: ${existingSocket.id}`);
                existingSocket.emit("kicked");
                existingSocket.disconnect(true);
            }
        }

        // 4. Now join the room yourself
        await socket.join(roomName);
        
        console.log(`User ${userId} secured in room ${roomName}`);
        
        return callback({ status: "Allowed" });
    });
   socket.on("usernameAck", (username,callback)=>{
      socket.username = username;
      return callback({
        status: "ok"
      });
      
    });
  socket.on("RequestMatchStatus", async (userId) => {
    try {
        // 1. Fetch matches from your DB
        const matches = await MatchModel.find({ user_id: userId }, { matched_user_id: 1, _id: 0 });
        const matchedUserIds = matches.map(match => match.matched_user_id.toString());

        for (const matchId of matchedUserIds) {
            // 2. Room Logic: Check if the match is online
            // io.in(...) returns an array of sockets in that room
            const onlineSockets = await io.in(`user:${matchId}`).fetchSockets();
            const isOnline = onlineSockets.length > 0;

            // 3. Tell the current user if their match is online
            socket.emit("MatchesPageStatus", {
                matchId: matchId,
                status: isOnline ? "online" : "offline"
            });

            // 4. If they ARE online, notify the match that the current user just came online
            if (isOnline) {
                socket.to(`user:${matchId}`).emit("MatchesPageStatus", {
                    matchId: userId,
                    status: "online"
                });
            }
        }
    } catch (err) {
        console.error("DB Error:", err);
    }
});
socket.on("unmatchSocket", async(matchId, callback)=>{
    const OnlineSocket = await io.in(`user:${matchId}`).fetchSockets();
    const isOnline = OnlineSocket.length > 0;
    if (isOnline){
      socket.to(`user:${matchId}`).emit("Unmatched", {
        socketMatchId: socket.userId
      });
    };
    return callback({
      acknowledged: true
    });

});
// Inside your io.on("connection", ...)
socket.on("start_typing", (data) => {
    const { targetUserId } = data; // The ID of the person I am chatting with

    
    // We send to the target user's room
    // 'socket.userId' and 'socket.username' should be set during Login
    console.log("line 105");
    
    console.log(targetUserId);
    
    
    socket.to(`user:${targetUserId}`).emit("is_typing", {
        fromUserId: socket.userId,
        fromUsername: socket.username
    });
});

socket.on("stop_typing", (data) => {
    const { targetUserId } = data;
    socket.to(`user:${targetUserId}`).emit("stopped_typing", {
        fromUserId: socket.userId,
        fromUsername: socket.username
    });
});
  socket.on("Message", async(message, matchId)=>{
      const match = await MatchModel.findOne({user_id: socket.userId, matched_user_id: matchId});
      if(!match) return;
        const UserSender = await UserModel.findOne({_id: socket.userId});
        const UserReciver = await UserModel.findOne({_id: matchId});
        const onlineSockets = await io.in(`user:${matchId}`).fetchSockets();
        const isOnline = onlineSockets.length > 0;
        if(isOnline){
          console.log("double send check");
          
          socket.to(`user:${matchId}`).emit("MessageRecieved",{
            userSender: UserSender.username,
            message,
            userSenderId: UserSender._id
          } );
        }
        messageObj = {
                    senderID:UserSender._id,
                    senderUsername: UserSender.username,
                    recieverID:UserReciver._id,
                    receiverUsername: UserReciver.username, 
                    message
                }
                let newMsg = new Message(messageObj);
                await newMsg.save();
                await MatchModel.updateOne(
                    { user_id: UserReciver._id, matched_user_id: UserSender._id }, // Match the authenticated user & their matched user
                    { $inc: { messageCounter: 1 } }
                );


  });
  socket.on("handleMatchNotiAmountDb",async(matchid, type) => {
    let user;
    if (type === "increment") {
        user = await UserModel.findByIdAndUpdate(
        matchid,
        { $inc: { matchNotiAmount: 1 } }, // Increment by 1
        { new: true } // Optional: returns the document AFTER the update
        );
        const socketUser = await UserModel.findByIdAndUpdate(
        socket.userId,
        { $inc: { matchNotiAmount: 1 } }, // Increment by 1
        { new: true } // Optional: returns the document AFTER the update
        );
        socket.to(`user:${matchid}`).emit("UiMatchNotiAmount", {
            matchNotiAmount: user.matchNotiAmount
        });
        socket.emit("UiMatchNotiAmount", {
            matchNotiAmount: socketUser.matchNotiAmount
        });
    }
    if (type === "reset") {
        user = await UserModel.findByIdAndUpdate(
        socket.userId,
        {matchNotiAmount: 0 }, // Increment by 1
        { new: true } // Optional: returns the document AFTER the update
        );
         socket.emit("UiMatchNotiAmount", {
            matchNotiAmount: user.matchNotiAmount
        });
    }

  

    
  });
  socket.on("disconnect", async(reason) => {
    console.log(`User ${socket.userId} (Socket ${socket.id}) is gone. Reason: ${reason}`);
    try {
          // 1. Find all people who are matched with this user
          const matches = await MatchModel.find({ user_id: socket.userId });
          const matchedUserIds = matches.map(m => m.matched_user_id.toString());

          // 2. Tell every match that this user is now OFFLINE
          matchedUserIds.forEach(matchId => {
              socket.to(`user:${matchId}`).emit("MatchesPageStatus", {
                  matchId: socket.userId,
                  status: "offline"
              });
          });
      } catch (err) {
          console.error("Error during disconnect notification:", err);
      }
  });
  
});
}