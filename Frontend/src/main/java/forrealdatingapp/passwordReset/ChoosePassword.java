package forrealdatingapp.passwordReset;



import com.fasterxml.jackson.core.JsonProcessingException;
import forrealdatingapp.App;
import forrealdatingapp.Scenes.LoginWindow;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.routes.AuthRequests;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ChoosePassword {
    public void showChoosePassword(Stage stage,String email){

        VBox root = new VBox(15);
        Label passLabel = new Label("enter your new password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("enter your password here");
        TextField visiblePasswordField = new TextField(); // For showing the password in plain text
        visiblePasswordField.setManaged(false); // Not visible by default
        visiblePasswordField.setVisible(false);
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());
        Button toggleVisibilityButton = new Button("\uD83D\uDC41"); // Eye emoji
        toggleVisibilityButton.setStyle("-fx-background-color: transparent; -fx-font-size: 14px;");
        toggleVisibilityButton.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            visiblePasswordField.setManaged(true);
            visiblePasswordField.setVisible(true);
            passwordField.setManaged(false);
            passwordField.setVisible(false);
        });
        toggleVisibilityButton.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            visiblePasswordField.setManaged(false);
            visiblePasswordField.setVisible(false);
            passwordField.setManaged(true);
            passwordField.setVisible(true);
        });
        HBox passwordBox = new HBox(10, passwordField, visiblePasswordField, toggleVisibilityButton);

        Button reset = new Button("reset password");
        reset.setOnAction((actionEvent) -> {
                String passwordString = passwordField.getText();
                if (passwordString.isBlank()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Password cannot be empty.", ButtonType.OK);
                    alert.show();
                    return;
                }
                String status = "";
                if(!email.isBlank())
                    status = AuthRequests.Resetusrpass(passwordString, email);
                if(status.contains("403")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(status.split("\\|")[1]);
                    alert.show();
                    status = "";

                }
                else{
                    try {
                        App.loginWindow.showLoginWindow(stage);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if (status.contains("201")){
                        App.serverStatusIndicator.setText(status.split("\\|")[1]);
                        App.serverStatusIndicator.setTextFill(Color.GREEN);
                     }
                     else if(status.contains("404")){
                        App.serverStatusIndicator.setText(status.split("\\|")[1]);
                        App.serverStatusIndicator.setTextFill(Color.RED);
    
                     }
                }

        });
        // reset.setOnAction(()=>{
           
            //     String passwordString = password.getText();
            //     if(user != null)
            //     String status = UsersRouteRequests.Resetusrpass(passwordString, user.getEmail());

            // });




        
        
        
        
        root.getChildren().addAll(passLabel, passwordBox, reset);
        Scene scene = new Scene(root, 500,600);
        stage.setTitle("Reset Password");
        stage.setScene(scene);


        
    }
    public boolean isValid(String password, Stage stage, User user){
        return true;
    }
    
}
