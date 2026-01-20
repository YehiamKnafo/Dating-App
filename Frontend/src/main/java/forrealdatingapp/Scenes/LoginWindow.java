package forrealdatingapp.Scenes;
import static forrealdatingapp.routes.RouterUtils.manageToken;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.App;
import forrealdatingapp.TokenManager;
import forrealdatingapp.chatScenes.ChatZone;
import forrealdatingapp.dtos.LoginClass;
import forrealdatingapp.otps.SendOTPReset;
import forrealdatingapp.otps.SendOTPScreen;
import forrealdatingapp.routes.AuthRequests;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginWindow {
    static ObjectMapper om = new ObjectMapper();
    public static  Label error;
    public static Stage passStage;
    public static String passID;


    public void showLoginWindow(Stage stage, String _id) {
        App.isTokenOnline = false;
        // Main layout pane
        passID = _id;
        
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 500, 600);
        
        // Set background color
        scene.setFill(Color.web("#f4f4f4"));

        // Create a VBox for the form layout
        VBox formLayout = new VBox(15);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 10;");
        
        // Title
        Label titleLabel = new Label("Welcome Back!");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");
        
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
        error = new Label();
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
        formLayout.getChildren().addAll(titleLabel, usernameLabel, usernameField, passwordLabel, error, passwordField, loginButton, signupButton,forgotPassButton);
        
        // Add formLayout to the root pane
        root.getChildren().add(formLayout);

        // Set up button actions (You can replace this with actual login/signup logic)
        loginButton.setOnAction(e -> {
       
            login(usernameField.getText(), passwordField.getText(), error, stage,_id);
            
        });
        scene.setOnKeyPressed((keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                login(usernameField.getText(), passwordField.getText(), error, stage,_id);

            }
        });
        
//         loginButton.setOnKeyPressed(event -> {
//         if (event.getCode() == KeyCode.ENTER) {
//         // Your action when the Enter key is pressed
//         login(usernameField.getText(), passwordField.getText(), error, stage);

//     }
// });

        signupButton.setOnAction(e -> {
           
            System.out.println("Sign Up clicked: ");
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
private void login(String usrname, String p, Label error, Stage stage,String _id){
    String username = usrname;
    String password = p;
    System.out.println("Login clicked: " + username + " / " + password);
    LoginClass usrpass = new LoginClass(username, password);
    String json = "";
    try {
        json = om.writeValueAsString(usrpass);
    } catch (JsonProcessingException e1) {
        // TODO Auto-generated catch block
        System.out.println(e1.getLocalizedMessage());
        
    }
    // System.out.println(json);
    String res = AuthRequests.PostLogin(json);
    // System.out.println(res == null);
    String token = "";
    
    try {
        Map<String, Object> jsonUser = om.readValue(res, new TypeReference<Map<String,Object>>(){});
        token = (String) jsonUser.get("token");
        _id = (String) jsonUser.get("_id");
        
    } catch (JsonProcessingException exception) {
        System.out.println(exception.getLocalizedMessage());
    }
    if(token != null && !token.isEmpty()){
        // System.out.println("dailyFeedScreen");
        //token logic -- create token class
        // res var contains token now
        passID = _id;
        manageToken().saveToken(_id, token);
        try {
            ChatZone.connectToServer();
            ChatZone.writer.println("userInteract|" + _id);
            
 

        } catch (Exception e) {
            System.out.println(e.getCause());
        }
      
    } 
    else{
        error.setTextFill(Color.RED);
        error.setText("invalid user or password");
    }
    
    
}
public static void SocketLogin(String status){
    if(status.equals("Unallowed")){
        App.isTokenOnline = true;
        ChatZone.closeConnection();
        Platform.runLater(()->{
            LoginWindow lw = new LoginWindow();
            lw.showLoginWindow(passStage, null);
            error.setTextFill(Color.RED);
            error.setText("user logged in at another session");
            

        });
        
        
        
        
    }
    else{
        {
            Platform.runLater(()->{
                MainPage mp = new MainPage();
                mp.showMainPage(passStage, passID);

            });
    }
    }

}

}