const express = require('express')
const routesHandler = require('./routes/routes');
const connectToDB = require('./db/mongoConnect');
const { PORT } = require('./secret/dotenvconf');
const app = express();
app.use(express.json());
const port = PORT;
routesHandler(app);
const startServer = async () => {
  try {
    // 1. Wait for DB first
    await connectToDB();
    console.log("Database connected successfully");

    // 2. Then start listening
    app.listen(port, () => {
      console.log(`App listening on port ${port}`);
    });
  } catch (error) {
    console.error("Failed to start server:", error);
    process.exit(1); // Kill the process if DB fails
  }
};

startServer();
