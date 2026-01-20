const nodemailer = require('nodemailer');
const { GMAIL_SMTP_EMAIL, GMAIL_SMTP_PASSWORD } = require('./secret/secretConf.js');
// Configure transporter for sending emails
const transporter = nodemailer.createTransport({
  host: 'smtp.gmail.com',
  port: 465,
  secure: true,
  auth: {
    user: GMAIL_SMTP_EMAIL,
    pass: GMAIL_SMTP_PASSWORD
  }
});


// Generate a random OTP
function generateOtp() {
  return Math.floor(100000 + Math.random() * 900000).toString(); // 6-digit OTP
}

// Send OTP to the user's email
async function sendOtp(email) {
  const otp = generateOtp();

  const mailOptions = {
    from: GMAIL_SMTP_EMAIL, // Sender address
    to: email, // Receiver email
    subject: 'Your OTP Code',
    text: `Your OTP code is: ${otp}`
  };

  try {
    await transporter.sendMail(mailOptions);
    console.log('OTP sent to:', email);
    return otp; // Return OTP for verification
  } catch (error) {
    console.error('Error sending email:', error);
    throw new Error('Failed to send OTP');
  }
}

module.exports = sendOtp;
