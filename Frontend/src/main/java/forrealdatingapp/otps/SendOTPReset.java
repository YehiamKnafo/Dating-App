package forrealdatingapp.otps;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.passwordReset.ChoosePassword;
import forrealdatingapp.routes.AuthRequests;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONObject;

import static forrealdatingapp.App.progressIndicator;
import static forrealdatingapp.App.serverStatusIndicator;
import static forrealdatingapp.otps.SendOTPScreen.showTimedAlert;
import static forrealdatingapp.otps.SendOTPScreen.verifyOtpSetup;

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


        Label emailVerifyLabel = new Label("Enter Code From Email:");
        TextField emailVerifyTxt = new TextField();
        Button verifyOtpBtn = new Button("Verify OTP");
        verifyOtpSetup(emailVerifyLabel, emailVerifyTxt, verifyOtpBtn);
        root.getChildren().addAll(title, emailField, sendOtpButton,emailVerifyLabel,emailVerifyTxt,verifyOtpBtn);
        StackPane stackPane = new StackPane(root, progressIndicator, serverStatusIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);
        StackPane.setAlignment(serverStatusIndicator, Pos.CENTER);
        StackPane.setMargin(serverStatusIndicator, new Insets(100, 0, 0, 0));
        App.BackToLoginBtn(root, stage);

        // Scene setup
        sendOtpButton.setOnAction(event -> getEmail(stage, emailField, sendOtpButton,emailVerifyLabel,emailVerifyTxt,verifyOtpBtn));

        Scene scene = new Scene(stackPane, 600, 800);
        scene.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER)
                getEmail(stage, emailField, sendOtpButton,emailVerifyLabel,emailVerifyTxt,verifyOtpBtn);

        });
        stage.setTitle("Send Otp");
        stage.setScene(scene);
        stage.show();
    }



    private void getEmail(Stage stage, TextField emailField, Button sendOtpBtn,Label emailVerifyLabel,TextField emailVerifyTxt,Button verifyOtpBtn) {
        String email = emailField.getText().trim();
        if (isValidEmail(email)) {

            //TASK START
            Task<JSONObject> sendOtpTask = new Task<JSONObject>() {
                @Override
                protected JSONObject call() throws Exception {
                    return AuthRequests.sendOtpRequest(email,"reset-otp");
                }
            };
            sendOtpTask.setOnScheduled(e ->{

                    progressIndicator.setVisible(true);

            });
            sendOtpTask.setOnSucceeded(e->{

                try {
//                    System.out.println(sendOtpTask.get());
                    JSONObject res = sendOtpTask.get();
                    String bodyAsString = res.getString("body");
                    JSONObject resBody  = new JSONObject(bodyAsString);

                    switch (res.getInt("code")){
                        case 200: {
                            
                            showAlert("Success", "OTP sent successfully! check your email box for the code!!", AlertType.INFORMATION);
//                            OTPVerificationScreenReset otpVerificationScreenReset = new OTPVerificationScreenReset();
//                            otpVerificationScreenReset.ShowOTPVerificationScreenReset(stage, user);
                                User newuser = new User();
                                newuser.setEmail(email);
                                UnloggedUserManager.setUser(newuser);
                                emailVerifyLabel.setVisible(true);
                                emailVerifyTxt.setVisible(true);
                                verifyOtpBtn.setVisible(true);
                                verifyOtpBtn.setOnAction(event -> {
                                String otp = emailVerifyTxt.getText();
                                boolean valid = AuthRequests.verifyOtpRequest(email, otp);
                                if(valid) {
                                    ChoosePassword ChoosePassword = new ChoosePassword();
                                    ChoosePassword.showChoosePassword(stage, email);
                                }
                                else showAlert("Error", "Please enter a valid otp", AlertType.ERROR);

                            });
    
                            break;
                        }
                        case 404:{
                            showAlert("Error", "Account does not exist", AlertType.ERROR);
                            break;
                        }
                        case 429:{

                            //TODO:SHOW TIMER IN ALERT
                            showTimedAlert(resBody.getInt("sec"), sendOtpBtn );
                            showAlert("Error",resBody.getString("msg"), AlertType.ERROR);
                            break;
                        }

                        default:{

                            showAlert("Error", "Please enter a valid email address.", AlertType.ERROR);
                        }

                    }
                        serverStatusIndicator.setTextFill(null);
                        progressIndicator.setVisible(false);


                } catch (Exception ex) {
                    throw new RuntimeException(ex);

                }
            });
            new Thread(sendOtpTask).start();
            //TASK END
//            String res =  AuthRequests.sendOtpRequest(email,"reset-otp");  // Assume this sends the OTP request
//            System.out.println(res);
//            switch (res) {
//                case "valid": {
//                    User user = new User();
//                    user.setEmail(email);
//                    showAlert("Success", "OTP sent successfully! check your email box for the code!!", AlertType.INFORMATION);
//                    OTPVerificationScreenReset otpVerificationScreenReset = new OTPVerificationScreenReset();
//                    otpVerificationScreenReset.ShowOTPVerificationScreenReset(stage, user);
//                    break;
//                }
//                case "email exist": showAlert("Error", "email alredy exist in the system", AlertType.ERROR);
//                                    break;
//                case "account is not exist":showAlert("Error", "no accounts linked to this email", AlertType.ERROR);
//                                            break;
//
//                default: showAlert("Error", "Please enter a valid email address.", AlertType.ERROR);
//            }
            
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
        alert.show();
    }


}
