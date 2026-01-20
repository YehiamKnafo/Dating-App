# Environment Variables Configuration and important notes
- [`frontend`](https://github.com/yehiamtheone/forrealdatingapp/tree/Frontend): Contains JavaFX code  
- [`backend`](https://github.com/yehiamtheone/forrealdatingapp/tree/Backend): Contains Node.js/API code  
- `main`: Environment Variables Configuration and important notes

This document outlines the environment variables required for different deployment scenarios and application components.

## Docker Compose / Jenkins Credentials / System Variables

For Docker Compose deployments or system-wide configuration:

```bash
EXPRESS=http://<ip>:<port>/
TCP=<ip>:<port>
CLOUDINARY_URL=cloudinary://<your_api_key>:<your_api_secret>@<your_cloud_name>
JWT_SECRET=<any_string>
MONGO_URL=mongodb://localhost:27017/<your_db_name> || mongodb+srv://<username>:<password>@cluster0.eyhd9.mongodb.net/<your_db_name>
GMAIL_SMTP_EMAIL=<your_emaiPASSWORD=<your_api_password_from_google_account_settings>
GITHUB_TOKEN=<your_git_hub_PAT_token>
```

## Backend Environment (.env)

For backend-only configuration:

```bash
EXPRESS=http://<ip>:<port>/
TCP=<ip>:<port>
JWT_SECRET=<any_string>
MONGO_URL=<mongodb://localhost:27017/ || mongodb+srv://<username>:<password>@cluster0.eyhd9.mongodb.net/>
GMAIL_SMTP_EMAIL=<your_emaiPASSWORDWORD=<your_api_password_from_google_account_settings>
```

## Frontend Environment (.env)

For frontend-only configuration:

```bash
EXPRESS=http://<ip>:<port>/
TCP=<ip>:<port>
CLOUDINARY_URL=cloudinary://<your_api_key>:<your_api_secret>@<your_cloud_name>

```

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
- **Backend**: Express.js (Node.js)
- **Database**: MongoDB
- **Real-time Communication**: Socket.io
- **Media Storage**: Cloudinary (optional)
- **Build Tool**: Gradle with Launch4j plugin

### Port Configuration
- **Backend API**: Port 3000
- **Socket Server**: Port 4000
- **Jenkins**: Port 8080

### Platform Support
- **Windows**: Full support with standalone executable
- **macOS/Linux**: Manual runtime setup required (not packaged as standalone)

## Important Notes

- **Official App Limitation**: If running the official app without connection handling, manual environment configuration is required
- **Cloudinary Integration**: Follow the existing Cloudinary API implementation; do not substitute with different APIs
- **Database Instructions**: Refer to the backend branch for MongoDB setup and connection strings
- **Cross-Platform**: While Gradle supports dynamic runtime for macOS/Linux, standalone app packaging is Windows-only

## Troubleshooting

1. **Build Issues**: Ensure all environment variables are properly set
2. **Connection Problems**: Verify backend server is running on the specified port
3. **Database Errors**: Check MongoDB connection string in backend configuration
4. **Jenkins Failures**: Confirm the correct branch is specified and Unix agent is available

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
- **Jenkins Pipeline**: Ensure correct branch and agent configuration

## Setup Pipeline (`jenkins-file-for-setwin/Jenkinsfile`)

**Purpose**: Environment setup and automatic release download for Windows

**Features**:
- Injects environment variables automatically
- Downloads latest release to workspace
- Windows-specific setup commands

**Stages**:
- **Checkout**: Verify Git repository access
- **Add Environment Variables**: Configure Windows environment using `setx` commands
- **Download Latest Release**: Fetch the latest executable from GitHub releases



<!-- ## License

[Add your license information here] -->


