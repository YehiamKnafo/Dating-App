
const authRoutes = require('./AuthRequests.js');
const matchingRoutes = require('./MatchingRequests.js');
const profileRoutes = require('./UserProfileRequests.js');
const messageRoutes = require('./MessageRequests.js');
const fileRoutes = require('./FileRequests.js');
const Routes = (app) => {
    app.use("/profile", profileRoutes );
    app.use("/auth", authRoutes );
    app.use("/matches", matchingRoutes );
    app.use("/file", fileRoutes);
    app.use("/messages", messageRoutes );
   
};

module.exports = Routes;