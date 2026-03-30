const http = require('http');
const express = require('express');
const cors = require('cors');
const connectToDB = require('./db/mongoConnect.js');
const Routes = require('./routes/config_routes.js');
const { EXPRESS_PORT, ALLOWED_ORIGIN } = require('./secret/secretConf.js');
const { Server } = require("socket.io");

const socketHandler = require('./socketio/websocket.js');

// Initialize Express app
const app = express();
const corsOptions = {
  origin: ALLOWED_ORIGIN, // Only this origin can access
  methods: ['GET', 'POST', 'PUT', 'DELETE'], // Allowed HTTP methods
  credentials: true, // Allow cookies/credentials (if needed)
};
// Middleware
// app.use(express.static(path.join(__dirname, "public")));
// app.use(express.static('public'));
app.use(cors(corsOptions)); // Enable CORS
app.use(express.json()); // Parse JSON request bodies

// Serve static files (if needed)

// Connect to MongoDB
connectToDB();

// Register routes
Routes(app);
app.get('/version', (req, res) => {
    res.send("Version 1.1.0 - Clean structure");
});
// Create HTTP server
const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "*",
    methods: ["GET", "POST"],
    credentials: true
  },
  allowEIO3: true, // ← This enables support for older clients
  transports: ['websocket', 'polling']
});
socketHandler(io);

// Start the server
server.listen(EXPRESS_PORT || 3000, ()=>{
  
  console.log(`express server up on port ${server.address().port}`);
});


// Handle unhandled promise rejections
process.on('unhandledRejection', (err) => {
  console.error('Unhandled Rejection:', err);
  process.exit(1);
});

// Handle uncaught exceptions
process.on('uncaughtException', (err) => {
  console.error('Uncaught Exception:', err);
  process.exit(1);
});
