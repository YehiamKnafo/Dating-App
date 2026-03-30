package forrealdatingapp.otps;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.routes.AuthRequests;
import forrealdatingapp.signUpScenes.UserDetails;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

import static forrealdatingapp.App.*;

public class SendOTPScreen  {


    
    public void ShowSendOTPScreen(Stage stage) {
        assert progressIndicator != null;
        progressIndicator.setVisible(false);
        serverStatusIndicator.setTextFill(null);
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
        Button verifyOtpBtn = new Button("Verify OTP");
        TextField emailVerifyTxt = new TextField();
        verifyOtpSetup(emailVerifyLabel, emailVerifyTxt, verifyOtpBtn);
        root.getChildren().addAll(skipEmailScene(stage) ,title,emailField,sendOtpButton,emailVerifyLabel,emailVerifyTxt,verifyOtpBtn);
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

    private void getEmail(Stage stage,TextField emailField,Button sendOtpBtn,Label emailVerifyLabel,TextField emailVerifyTxt,Button verifyOtpBtn) {
        String email = emailField.getText().trim();
        if (isValidEmail(email)) {
//            System.out.println("valid email backend test below");

            Task<JSONObject> sendOtpTask = new Task<JSONObject>() {
                @Override
                protected JSONObject call() throws Exception {

                    return AuthRequests.sendOtpRequest(email,"signup-otp");

                }
            };
            sendOtpTask.setOnScheduled(e ->{
                Platform.runLater(()->{
                    progressIndicator.setVisible(true);
                });
            });
            sendOtpTask.setOnSucceeded(e->{
                try {

//                    System.out.println(sendOtpTask.get());
                    JSONObject res = sendOtpTask.get();
                    String bodyAsString = res.getString("body");
                    JSONObject resBody  = new JSONObject(bodyAsString);
                    switch (res.getInt("code")){
                        case 200: {
                            User newUser = new User();
                            newUser.setEmail(email);
                            UnloggedUserManager.setUser(newUser);
                            showAlert("Success", "OTP sent successfully! check your email box for the code!!", AlertType.INFORMATION);
//                                OTPVerificationScreen verify = new OTPVerificationScreen();
//                                verify.ShowOTPVerificationScreen(stage, user);
                                //TEST
                                emailVerifyLabel.setVisible(true);
                                emailVerifyTxt.setVisible(true);
                                verifyOtpBtn.setVisible(true);

                                verifyOtpBtn.setOnAction(event -> {
                                    String otp = emailVerifyTxt.getText();
                                    boolean valid = AuthRequests.verifyOtpRequest(email, otp);
                                    if(valid) {
                                        System.out.println("move to the next screen -- UserDetails");
                                        UserDetails userDetails =  new UserDetails();
                                        userDetails.showUserDetails(stage);


                                    }
                                    else showAlert("Error", "Please enter a valid otp", AlertType.ERROR);

                                });
                                //TEST END



                            break;
                        }
                        case 403: {

                            showAlert("Error", "email alredy exist in the system", AlertType.ERROR);
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
                    Platform.runLater(()->{
                        progressIndicator.setVisible(false);
                        serverStatusIndicator.setTextFill(null);
                    });

                } catch (Exception ex) {
                    throw new RuntimeException(ex);

                }
            });
            new Thread(sendOtpTask).start();
            //TASK TEST END
            //PROD
//            String res =  AuthRequests.sendOtpRequest(email,"signup-otp");  // Assume this sends the OTP request
//            System.out.println(res);
//            switch (res) {
//                case "valid": {
//                    User user = new User();
//                    user.setEmail(email);
//                    showAlert("Success", "OTP sent successfully! check your email box for the code!!", AlertType.INFORMATION);
//                    OTPVerificationScreen verify = new OTPVerificationScreen();
//                    verify.ShowOTPVerificationScreen(stage, user);
//                    break;
//                }
//                case "email exist": showAlert("Error", "email alredy exist in the system", AlertType.ERROR);
//                                    break;
//
//                default: showAlert("Error", "Please enter a valid email address.", AlertType.ERROR);

            //PROD END
            
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
    public static void showTimedAlert(int ttl, Button okButton){

        okButton.setDisable(true);

        final int[] secondsLeft = {ttl};

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsLeft[0]--;
            if (secondsLeft[0] > 0) {
                okButton.setText("Wait (" + secondsLeft[0] + ")");
            } else {
                okButton.setText("Send OTP");
                okButton.setDisable(false);
            }
        }));

        timeline.setCycleCount(secondsLeft[0]);
        timeline.play();

    }
    public static void verifyOtpSetup(Label emailVerifyLabel, TextField emailVerifyTxt, Button verifyOtpBtn) {
        emailVerifyLabel.setVisible(false);
        emailVerifyTxt.setPromptText("ex. 111111");
        emailVerifyTxt.setVisible(false);
        verifyOtpBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 5; -fx-padding: 10;");
        verifyOtpBtn.setPrefWidth(250);
        verifyOtpBtn.setVisible(false);
    }
}
