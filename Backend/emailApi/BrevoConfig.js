const { BREVO_API_KEY, MY_BREVO_EMAIL } = require("../secret/secretConf");

const API_KEY = BREVO_API_KEY;
function generateOtp() {
  return Math.floor(100000 + Math.random() * 900000).toString(); // 6-digit OTP
}
async function sendOTP(email,otp) {

  const res = await fetch("https://api.brevo.com/v3/smtp/email", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "api-key": API_KEY
    },
    body: JSON.stringify({
      sender: {
        name: "dating app",
        email: MY_BREVO_EMAIL
      },
      to: [
        { email: email }
      ],
      subject: "Your OTP Code",
      textContent: `Your OTP is ${otp}`
    })
  });
return res;
}
async function sendOtp(email) {
   const otp = generateOtp();
   try {
       
        await sendOTP(email, otp);
        // console.log(res);
        
       return otp; 
   } catch (error) {
        console.log(error);
    
   }
}
module.exports = sendOtp;