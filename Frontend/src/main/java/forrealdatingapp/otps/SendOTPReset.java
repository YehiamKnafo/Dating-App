package forrealdatingapp.otps;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.AuthRequests;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SendOTPReset  {


    
    public void ShowSendOTPReset(Stage stage) {
        VBox root = new VBox(20);  // Spacing between elements
        root.setAlignment(Pos.CENTER);  // Center alignment of elements

        // Styling for modern UI
        root.setStyle("-fx-background-color: #f4f4f9; -fx-padding: 30;");

        // Title
        Text title = new Text("What's your email?");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.DARKSLATEGRAY);

        // Email text field with modern styling
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle("-fx-border-radius: 5; -fx-border-color: #ccc; -fx-padding: 10;");
        emailField.setPrefWidth(250);

        // Send OTP button with styling
        Button sendOtpButton = new Button("Send OTP");
        sendOtpButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5; -fx-padding: 10;");
        sendOtpButton.setPrefWidth(250);

        // Action handler for sending OTP
        sendOtpButton.setOnAction(event -> getEmail(stage, emailField));
    
        

        root.getChildren().addAll(title, emailField, sendOtpButton);
        App.BackToLoginBtn(root, stage);

        // Scene setup
        Scene scene = new Scene(root, 500, 600);
        scene.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
                getEmail(stage, emailField);

        });
        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.show();
    }

    private void getEmail(Stage stage, TextField emailField) {
        String email = emailField.getText().trim();
        if (isValidEmail(email)) {
            System.out.println("valid");
            String res =  AuthRequests.sendOtpRequest(email,"reset-otp");  // Assume this sends the OTP request
            System.out.println(res);
            switch (res) {
                case "valid": {
                    User user = new User();
                    user.setEmail(email);
                    showAlert("Success", "OTP sent successfully! check your email box for the code!!", AlertType.INFORMATION);
                    OTPVerificationScreenReset oTPVerificationScreenReset = new OTPVerificationScreenReset();
                    oTPVerificationScreenReset.ShowOTPVerificationScreenReset(stage, user);
                    break;
                }
                case "email exist": showAlert("Error", "email alredy exist in the system", AlertType.ERROR);
                                    break;
                case "account is not exist":showAlert("Error", "no accounts linked to this email", AlertType.ERROR);
                                            break;
                
                default: showAlert("Error", "Please enter a valid email address.", AlertType.ERROR);
            }
            
        } else {
            showAlert("Error", "Please enter a valid email address.", AlertType.ERROR);
        }
    }

    // Simple email validation method
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    }

    // Method to show alert dialogs
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
