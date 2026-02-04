const mongoose = require('mongoose');
const { MONGO_URL } = require('../secretConf');


const connectToDB = async() =>  {
    // mongoose.set('strictQuery' , false);

    await mongoose.connect(MONGO_URL);
    console.log("mongo connect started");
    // use `await mongoose.connect('mongodb://user:password@127.0.0.1:27017/test');` if your database has auth enabled
}
module.exports = connectToDB;