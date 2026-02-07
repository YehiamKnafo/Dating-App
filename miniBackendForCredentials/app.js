const express = require('express')
const routesHandler = require('./routes');
const connectToDB = require('./db/mongoConnect');
const { PORT } = require('./dotenvconf');
const app = express();
app.use(express.json());
const port = PORT;
connectToDB();
routesHandler(app);
app.listen(port, () => {
  console.log(`app listening on port ${port}`)
});
