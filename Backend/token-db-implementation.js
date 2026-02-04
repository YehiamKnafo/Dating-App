const express = require('express');
const UserModel = require('../models/user');
const auth = require('../middleware/auth');

const router = express.Router();

// Sample route that uses the `auth` middleware to verify the token
router.get('/user-profile', auth, async (req, res) => {
  // Instead of using `req.user`, you directly query the user using the token data
  const token = req.header("x-api-key");
  const tokenData = jwt.verify(token, TOKEN_STR);

  try {
    const user = await UserModel.findById(tokenData._id); // Retrieve user by decoded _id

    if (!user) {
      return res.status(404).json({ msg: "User not found" });
    }

    res.json({ user });
  } catch (err) {
    res.status(500).json({ msg: "Error retrieving user profile" });
  }
});

module.exports = router;
const jwt = require('jsonwebtoken');
const { TOKEN_STR } = require('../secret/secretConf');
const UserModel = require('../models/user');

const auth = async (req, res, next) => {
  let token = req.header("x-api-key");

  if (!token) {
    return res.status(401).json({ msg: "Send a token in the header to continue", token: "" });
  }

  try {
    // Verify the JWT token and decode the payload
    const tokenData = jwt.verify(token, TOKEN_STR); // { _id: ... }

    // Query the database for the user using the decoded _id
    const user = await UserModel.findById(tokenData._id);

    if (!user) {
      return res.status(401).json({ msg: "User not found", token: "" });
    }

    // Check if the token is expired
    if (new Date() > user.tokenExpiredAt) {
      return res.status(401).json({ msg: "Token is expired", token: "" });
    }

    // You can skip storing user in req here. Instead, you just verify if the token is valid and return the appropriate response.
    // You can access the user data directly when needed in the route handler.

    next();
  } catch (err) {
    return res.status(401).json({ msg: "Token is invalid or expired", token: "" });
  }
};

module.exports = auth;
