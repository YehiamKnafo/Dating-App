const { createServer } = require("http");
const { Server } = require("socket.io");
const { SOCKET_PORT } = require("./secretConf");

const httpServer = createServer();
const io = new Server(httpServer, { /* options */ });

io.on("connection", (socket) => {
  // ...
});

httpServer.listen(SOCKET_PORT);