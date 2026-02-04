# Environment Variables Configuration and important notes
- `frontend`: Contains JavaFX code  
- `backend`: Contains Node.js/API code  
- `backend for credentials`: It purpose is to validate the user before giving credentials as i cant use something like cors cause frontend is offline client. 
- `other`: Environment Variables Configuration and important notes

This document outlines the environment variables required for different deployment scenarios and application components.

## Environment Variables Reference

### Core Configuration
- `EXPRESS`: HTTP server endpoint URL (e.g., `http://127.0.0.1:3000/`)
- `TCP`: TCP server endpoint for socket connections (e.g., `127.0.0.1:4000`)
- `JWT_SECRET`: Secret key for JWT token generation and validation (use any secure random string)

### Database Configuration
- `MONGO_URL`: MongoDB connection string
  - Local: `mongodb://localhost:27017/`
  - Atlas: `mongodb+srv://<username>:<password>@cluster0.eyhd9.mongodb.net/`
  - Docker: `mongodb://<your_db_service_name>:27017/<your_db_name>`

### File Upload Configuration
- `CLOUDINARY_URL`: Cloudinary service URL for image/file uploads
  - Format: `cloudinary://<your_api_key>:<your_api_secret>@<your_cloud_name>`

### Email Configuration
- `GMAIL_SMTP_EMAIL`: Gmail address for SMTP email sending
- `GMAIL_SMTP_PASSWORD`: App-specific password from Google Account settings (not your regular Gmail password)

## Notes

- Replace all placeholder values (`<>`) with your actual configuration values
- FoPASSWORD`, use an App Password generated from your Google Account security settings, not your regular password
- The `JWT_SECRET` should be a long, random string for security purposes
- MongoDB URL format depends on whether you're using a local instance or MongoDB Atlas cloud service
- For deployment web servers like render etc the backend ip interface will be selected automatically by the service, for custom domains its better go by the dotenv configuration 

## Architecture Notes

### Technology Stack
- **Frontend**: Java with Gradle build system
- **Backend**: Express.js (Node.js) and WebSocket.io
- **Database**: MongoDB
- **Real-time Communication**: WebSocket.io
- **Media Storage**: Cloudinary (optional)
- **Build Tool**: Gradle for frontend Npm for backend
- **DevOps***: Cloud service is render and image on docker hub

*render is free and was fitted for my first project.


### Port Configuration
- **Backend API**: Port 3000
- **Jenkins**: Port 8080

*i would guess render serving with nginx so its just fyi*

---


### Platform Support
- **Windows**: Full support with standalone executable
- **macOS/Linux**: Manual runtime setup required (not packaged as standalone)

## Important Notes

- **Official App Limitation**: If running the official app without connection handling, manual environment configuration is required
- **Cloudinary Integration**: Follow the existing Cloudinary API implementation; do not substitute with different APIs
- **Database Instructions**: Refer to the backend branch for MongoDB setup and connection strings
- **Cross-Platform**: While Gradle supports dynamic runtime for macOS/Linux, standalone app packaging i choose for Windows-only for obvious purposes.

## Troubleshooting

1. **Build Issues**: Ensure all environment variables are properly set
2. **Connection Problems**: Verify backend server is running on the specified port
3. **Database Errors**: Check MongoDB connection string in backend configuration

<!-- ## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request -->

## Support

For issues related to:
- **Backend Setup**: Check the backend branch documentation
- **Database Configuration**: Refer to MongoDB connection setup in backend




<!-- ## License

[Add your license information here] -->


