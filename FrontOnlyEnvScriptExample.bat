@echo off
setlocal

:: Set environment variables permanently using setx

:: EXPRESS variable
setx EXPRESS "http://<ip>:<port>/" 

:: TCP variable
setx TCP "<ip>:<port>" 

:: CLOUDINARY_URL variable
setx CLOUDINARY_URL "cloudinary://<your_api_key>:<your_api_secret>@<your_cloud_name>" 

:: TOKEN_SECRET variable
setx JWT_SECRET "<any_string>" 

echo Environment variables have been set permanently.
echo You may need to restart your command prompt or applications to see the changes.

pause
endlocal