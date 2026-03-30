package forrealdatingapp.otps;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.passwordReset.ChoosePassword;
import forrealdatingapp.routes.AuthRequests;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class OTPVerificationScreenReset{

    
    public void ShowOTPVerificationScreenReset(Stage stage, User user) {
        VBox root = new VBox(10);

        TextField otpField = new TextField();
        otpField.setPromptText("Enter the OTP");

        Button verifyOtpButton = new Button("Verify OTP");


                // LoginWindow lg = new LoginWindow();
                // lg.showLoginWindow(stage, null);
                // LoginWindow.error.setTextFill(Color.GREEN);
                // LoginWindow.error.setText("Password been reset successfully");




        UnloggedUserManager.setUser(user);
        root.getChildren().addAll(otpField, verifyOtpButton);
        App.BackToLoginBtn(root, stage, user);

        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Reset");
        stage.setScene(scene);
        stage.show();
    }

        private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    

   
}

