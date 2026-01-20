package forrealdatingapp.otps;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.AuthRequests;
import forrealdatingapp.signUpScenes.UserDetails;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OTPVerificationScreen{

    
    public void ShowOTPVerificationScreen(Stage stage, User user) {
        VBox root = new VBox(10);

        TextField otpField = new TextField();
        otpField.setPromptText("Enter the OTP");

        Button verifyOtpButton = new Button("Verify OTP");

        verifyOtpButton.setOnAction(event -> {
            String otp = otpField.getText();
            String email = user.getEmail(); // You should store this value from the previous screen
            boolean valid = AuthRequests.verifyOtpRequest(email, otp);
            if(valid) {
                System.out.println("move to the next screen -- UserDetails");
                UserDetails userDetails =  new UserDetails();
                userDetails.showUserDetails(stage, user);


        }
            else showAlert("Error", "Please enter a valid otp", AlertType.ERROR);

        });

        root.getChildren().addAll(otpField, verifyOtpButton);
        App.BackToLoginBtn(root, stage);

        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Verify OTP");
        stage.setScene(scene);
        stage.show();
    }

        private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    

   
}

