const Joi = require('joi');
const jwt = require('jsonwebtoken');
const mongoose = require('mongoose');
const { JWT_SECRET } = require('../secret/secretConf');


const usersSchema = new mongoose.Schema({
firstName:String , 
lastName:String , 
age:Number ,
minPreferredAge:Number,
maxPreferredAge:Number,
email:String,
username: String,
profilePicture: String,
pictures:[String],
birthDate: Date,
gender: String,
preferredGender:String,
bio: String,
password:String,
token:String
})

// אנחנו צריכים לייצא את המודל שבנוי משם הקולקשן אליו נרצה לשלוח מידע שיעבור בסכמה ומשם הסכמה עצמה
exports.UserModel = mongoose.model("users" , usersSchema)
// שם המודל חייב להתחיל עם אות גדולה + שם הקולקשן חייב להסתיים עם אס 
// TODO:
// Validation

exports.validateSignup = (_reqBody) => {
    const joiSchema = Joi.object({
        firstName: Joi.string().min(2).max(99).required(),
        lastName: Joi.string().min(2).max(99).required(),
        age: Joi.number().min(18).max(99).required(),
        email: Joi.string().trim().min(3).max(77),
        username: Joi.string().min(6).max(99).required(),
        pictures: Joi.array().items(Joi.string().max(200)).required(),
        birthDate: Joi.date()
        .iso()
        .max('now') // Must be in the past
        .min(new Date(new Date().setFullYear(new Date().getFullYear() - 99))) // At most 99 years ago
        .max(new Date(new Date().setFullYear(new Date().getFullYear() - 18))) // At least 18 years ago
        .required()
        .messages({
        'date.min': 'You must be younger than 100.',
        'date.max': 'You must be at least 18 years old.'
        }),
        gender: Joi.string().required().max(15).min(1),
        preferredGender: Joi.string().required().min(4),
        bio: Joi.string().max(50).allow(null, ""),
        password: Joi.string().required().max(200),
        profilePicture: Joi.string().max(200),
        minPreferredAge: Joi.number().min(18).max(99),
        maxPreferredAge: Joi.number().min(18).max(99)
    });
    return joiSchema.validate(_reqBody);
};
exports.validateDateOfBirth = (dateObj)=>{
    const joiSchema = Joi.object({
        birthDate: Joi.date()
        .iso()
        .max('now') // Must be in the past
        .min(new Date(new Date().setFullYear(new Date().getFullYear() - 99))) // At most 99 years ago
        .max(new Date(new Date().setFullYear(new Date().getFullYear() - 18))) // At least 18 years ago
        .required()
        .messages({
        'date.min': 'You must be younger than 100.',
        'date.max': 'You must be at least 18 years old.'
        })
        });
        return joiSchema.validate(dateObj);


}


   
    
  // נבדוק את המידע בצד הלקוח על מנת לחסוך קריאה לדאטה בייס ונבדוק שהמידע תקין ובמידה ולא ניתן הודעה מתאימה בצד לקוח 
// שימו לב למיקום הבדיקה בראוט
// לא נוכל לשלוח מאפיין שלא קיים בסכימה ובנוסף נקבל את השגיאה המדויקת בצד לקוח
exports.validateLogin = (_reqBody) =>{
    const joiSchema = Joi.object({
      username: Joi.string().min(3).max(77).required(),
      password: Joi.string().required().min(6).max(80)
    });
    return joiSchema.validate(_reqBody);
};
exports.validatePicture = (_reqBody) =>{
    const joiSchema = Joi.object({
        url: Joi.string().required().allow("")
    });
    return joiSchema.validate(_reqBody);
};
exports.validatePictureChange = (_reqBody) =>{
    const joiSchema = Joi.object({
        oldUrl: Joi.string().required(),
        newUrl: Joi.string().required()
    });
    return joiSchema.validate(_reqBody);
};
exports.validateProfileUpdate = (_reqBody) =>{
    const joiSchema = Joi.object({
      firstName: Joi.string().min(3).max(77).required(),
      age: Joi.number().required().min(18).max(99),
      bio: Joi.string().required().max(50)
    });
    return joiSchema.validate(_reqBody);
};
exports.createToken = (id) =>{
  const token = jwt.sign( { _id: id }, JWT_SECRET , {expiresIn: "180mins"});
  return token;

  
};

