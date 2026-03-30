
const auth = async(req, res, next) =>{
    let javafxHeader = req.header("X-App-Signature");
    if(javafxHeader !== "JavaFX-Client-v1")
        return res.sendStatus(401);   
     try{
       
        next();
        }
        catch(err){
           
        console.log(err);
            
        return res.sendStatus(401);
        }
    };
module.exports = auth;