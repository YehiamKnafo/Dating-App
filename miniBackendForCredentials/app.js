const express = require('express')
const routesHandler = require('./routes');
const connectToDB = require('./db/mongoConnect');
const app = express();
app.use(express.json());
const port = 4000;
connectToDB();
routesHandler(app);
app.listen(port, () => {
  console.log(`app listening on port ${port}`)
});
