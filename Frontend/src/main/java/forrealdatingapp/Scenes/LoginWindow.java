package forrealdatingapp.Scenes;
import static forrealdatingapp.App.progressIndicator;
import static forrealdatingapp.App.serverStatusIndicator;
import static forrealdatingapp.utilities.RouterUtils.createTokenManger;
import static forrealdatingapp.utilities.RouterUtils.manageToken;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.App;
import forrealdatingapp.WebSocket;
import forrealdatingapp.dtos.LoginClass;
import forrealdatingapp.otps.SendOTPReset;
import forrealdatingapp.otps.SendOTPScreen;
import forrealdatingapp.routes.AuthRequests;
import forrealdatingapp.utilities.RouterUtils;
import io.socket.client.Ack;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginWindow {
    static ObjectMapper om = new ObjectMapper();
    public static  Label error = new Label();
    public static Stage passStage;

    public void showLoginWindow(Stage stage) throws JsonProcessingException {
        App.isTokenOnline = false;
        // Main layout pane
        //ProgressIndicator
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMinWidth(80);
        progressIndicator.setStyle("-fx-progress-color: black;");
        progressIndicator.setVisible(false);
        serverStatusIndicator.setText(null);

        //END ProgressIndicator
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 800);

        // Set background color
        scene.setFill(Color.web("#f4f4f4"));

        // Create a VBox for the form layout
        VBox formLayout = new VBox(15);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 10;");
        
        // Logo
        Image logoSrc= new Image(Objects.requireNonNull(getClass().getResourceAsStream("/datingapplogo.png")));
        ImageView logo = new ImageView(logoSrc);
        logo.setFitHeight(250);
        logo.setFitWidth(250);

        // Username and Password Fields
        Label usernameLabel = new Label("user name:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your user name");
        usernameField.setStyle("-fx-border-radius: 5px; -fx-padding: 10px; -fx-border-color: #cccccc;");
        
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-border-radius: 5px; -fx-padding: 10px; -fx-border-color: #cccccc;");
        
        // Login and Signup buttons
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        
        Button signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        
        Button forgotPassButton = new Button("Password Reset");
        forgotPassButton.setStyle("-fx-background-color:rgb(252, 7, 7); -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");

        // Google Login Button
        // Button googleButton = new Button("Continue with Google");
        // googleButton.setStyle("-fx-background-color: #db4437; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px; -fx-background-radius: 5px;");
        
        // Add an icon for the Google button
        // ImageView googleIcon = new ImageView(new Image("file:src/icons-google.png"));        
        // googleIcon.setFitHeight(20);
        // googleIcon.setFitWidth(20);
        // googleButton.setGraphic(googleIcon);
        // googleButton.setContentDisplay(ContentDisplay.LEFT);
        
        // Add components to formLayout
        formLayout.getChildren().addAll(logo, usernameLabel, usernameField, passwordLabel, passwordField,serverStatusIndicator, loginButton, signupButton,forgotPassButton);
        
        // Add formLayout to the root pane
        root.getChildren().addAll(formLayout, progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);

        // Set up button actions (You can replace this with actual login/signup logic)
        loginButton.setOnAction(e -> {

            login(usernameField.getText(), passwordField.getText(), serverStatusIndicator, stage);


        });
        scene.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER){

                login(usernameField.getText(), passwordField.getText(), serverStatusIndicator, stage);

            }
        });

//         loginButton.setOnKeyPressed(event -> {
//         if (event.getCode() == KeyCode.ENTER) {
//         // Your action when the Enter key is pressed
//         login(usernameField.getText(), passwordField.getText(), error, stage);

//     }
// });

        signupButton.setOnAction(e -> {
           
//            System.out.println("Sign Up clicked: ");
            SendOTPScreen otpScreen = new SendOTPScreen();
            otpScreen.ShowSendOTPScreen(stage);            
            

        });
        forgotPassButton.setOnAction((actionEvent) -> {
            SendOTPReset sendOTPReset = new SendOTPReset();
            sendOTPReset.ShowSendOTPReset(stage);
            
        });

        // googleButton.setOnAction(e -> {
        //     System.out.println("Continue with Google clicked");
        //     // Implement Google sign-in integration here
        // });
   
        
        stage.setTitle("Login / Sign Up");
        stage.setScene(scene);
        stage.show();
        passStage = stage;


    }

    private void login(String usrname, String p, Label error, Stage stage) {
        if(WebSocket.websocketio.INSTANCE.socketIoInstance != null)
            if (!WebSocket.websocketio.INSTANCE.socketIoInstance.connected()) {
                WebSocket.websocketio.INSTANCE.socketIoInstance.connect(); // This turns the engine back on
            }
        String username = usrname;
        String password = p;
//        System.out.println("Login clicked: " + username + " / " + password);

        LoginClass usrpass = new LoginClass(username, password);
        String json = "";
        try {
            json = om.writeValueAsString(usrpass);
//            System.out.println("JSON to send: " + json);
        } catch (JsonProcessingException e1) {
            System.out.println("JSON serialization error: " + e1.getLocalizedMessage());
            e1.printStackTrace();
        }

        System.out.println("DEBUG: BEFORE PostLogin call"); // NEW

        //TASK START

        AtomicReference<String> res = new AtomicReference<>();

        String finalJson = json;
        Task<String> loginTask = new Task<>() {
            @Override
            protected String call() throws Exception {

                // If AuthRequests.PostLogin(json) throws an exception,
                // the Task will catch it and trigger 'onFailed'.
                return AuthRequests.PostLogin(finalJson);
            }
        };
        loginTask.setOnScheduled(e->{

                progressIndicator.setVisible(true);

        });
// This replaces your 'catch' block
        loginTask.setOnFailed(event -> {
            Throwable e = loginTask.getException(); // Get the actual error if needed
            e.printStackTrace();

            // This runs on the JavaFX Application Thread automatically
                progressIndicator.setVisible(false);
                error.setTextFill(Color.RED);
                error.setText("Login request failed");

        });

        // This replaces the code after the 'try' block
        loginTask.setOnSucceeded(event -> {

            String result = loginTask.getValue();
            // Handle successful login here (e.g., check if res is empty)
            res.set(result);
            String token = "";
            try {
                Map<String, Object> jsonUser = om.readValue(res.get(), new TypeReference<Map<String,Object>>(){});
                token = (String) jsonUser.get("token");
                App.id = (String) jsonUser.get("_id");
//            System.out.println(token);
//            System.out.println("DEBUG: Parsed - Token: " + token + ", ID: " + _id);
            } catch (JsonProcessingException exception) {
                System.err.println("DEBUG: Failed to parse response:");
                exception.printStackTrace();
                error.setTextFill(Color.RED);
                error.setText("Invalid server response");
                return;
            }
            if(token != null && !token.isEmpty()) {
                WebSocket.websocketio.INSTANCE.connectToServer();
                App.kicked();

//            System.out.println("DEBUG: Token valid, proceeding to WebSocket connection");

//            System.out.println("DEBUG: About to connect WebSocket");

                try {
//                System.out.println("DEBUG: connectToServer() completed");
                    String finalToken = token;
                    WebSocket.websocketio.INSTANCE.socketIoInstance.emit("Login", App.id, (Ack) args -> {

                        // Now we are back on the UI Thread!

                        System.out.println("Login acknowledgment received");
                        JSONObject response = (JSONObject) args[0];
//                        System.out.println("Response: " + response);
                        try {
                            SocketLogin(response.getString("status"), stage, finalToken);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });



                } catch (Exception e) {
                    System.err.println("WebSocket connection error:");
                    e.printStackTrace();
                }
            } else {
                System.out.println("DEBUG: Token is invalid");
                error.setTextFill(Color.RED);
                error.setText("Invalid user or password");

                progressIndicator.setVisible(false);
            }
        });
        //TASK END

        new Thread(loginTask).start();

//        if (res.get() == null || res.get().isEmpty()) {
//            System.err.println("DEBUG: PostLogin returned null/empty response");
//            error.setTextFill(Color.RED);
//            error.setText("No response from server");
//            return;
//        }

//        System.out.println("DEBUG: Parsing response"); // NEW







//        System.out.println("DEBUG: Token null? " + (token == null));
//        System.out.println("DEBUG: Token empty? " + (token != null && token.isEmpty()));


    }
    public static void SocketLogin(String status, Stage stage, String token) {
        if (status.equals("Allowed")) {
            // Use a separate thread to avoid blocking the Socket listener
            new Thread(() -> {
                try {
                    if (manageToken() == null)
                        createTokenManger();
//                    System.out.println("Saving token for: " + token);

                    manageToken().saveToken(App.id, token);

                    Platform.runLater(() -> {
                        // Check if the stage is still showing or valid
                        serverStatusIndicator.setText(null);
                        progressIndicator.setVisible(false);
                        if (stage != null) {
                            MainPage mp = new MainPage();
                            mp.showMainPage(stage);
//                            System.out.println("UI Switched for: " + token);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


}