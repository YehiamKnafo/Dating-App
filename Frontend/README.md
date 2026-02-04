# Real Dating App - Frontend

A Java-based frontend application for a real-time dating platform with MongoDB backend and socketio integration.

For the already deployed version check the [official App Release](https://github.com/yehiamtheone/forrealdatingapp/releases/download/official-app-exe/forrealdatingapp.zip)
<!-- should go with static backend for this the spare one -->
<!-- ## Prerequisites


- **Media Storage**: Cloudinary account (optional, for image uploads)

## Quick Start



## Development Setup

### Local Development

1. **Set environment variables (Windows)**
   ```bat
   setx EXPRESS "http://127.0.0.1:3000"
   setx TCP "127.0.0.1:4000"
   setx CLOUDINARY_URL "your_cloudinary_url_here"
   ```

2. **Clone and navigate to project directory**
   ```bash
   git clone -b Frontend https://github.com/yehiamtheone/forrealdatingapp.git frda-frontend
   cd frda-frontend
   ```

3. **Build the application**
   ```bash
   ./gradlew clean build
   ./gradlew downloadJre
   ./gradlew createExe
   ./gradlew zipLaunch4j
   ```
   **Extract and run**
   - Navigate to `build/distribution/`
   - Extract `forrealdatingapp.zip`
   - Run the executable

   *or for a preview before creating the executable run those commands instead:*
   ```bash
      ./gradlew clean build
      ./gradlew run
   ```



## Jenkins CI/CD Pipeline

### Pipeline Configuration

1. **Jenkins Setup**
   - Access Jenkins on port 8080
   - Go to: Dashboard → Pipeline → Configure → Pipeline script from SCM
   - Set Repository URL to this repository
   - Configure branch specifier for the correct branch

2. **Node Configuration**
   - Navigate to: Manage Jenkins → Nodes
   - Create or configure existing node
   - Set label to `unix` for Unix-based builds

### Pipeline Files

#### Release Pipeline (`jenkins-file-for-release/Jenkinsfile`)

**Purpose**: Creates executable JAR and standalone Windows executable

**Requirements**: 
- Unix agent/container (labeled 'unix')
- Gradle build tool

**Stages**:
- **Checkout**: Verify Git repository access
- **Dotenv Creation**: Create the enviorment for the cloud and server -- Extracted from jenkins credintals
- **Build**: Clean build using Gradle (`./gradlew clean build`)
- **Add Runtime**: Bundle JRE for standalone execution
- **Create EXE**: Generate executable using Launch4j (`./gradlew createExe`)
- **Create ZIP**: Bundle JRE with executable for distribution
- **Publish GitHub Release**: Upload distribution ZIP 

**Notes**

I injected my own dotenv with my cloud and server into it, 
you could easily use your own local server and cloud using 
the setx command (for windows only).
 -->
