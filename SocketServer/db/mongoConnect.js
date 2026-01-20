const mongoose = require('mongoose');


const connectToDB = async() =>  {
    // mongoose.set('strictQuery' , false);

    await mongoose.connect("mongodb+srv://yehiam:159654qa@cluster0.eyhd9.mongodb.net/forrealdatingapp");
    console.log("mongo connect started");
    // use `await mongoose.connect('mongodb://user:password@127.0.0.1:27017/test');` if your database has auth enabled
}
module.exports = connectToDB;