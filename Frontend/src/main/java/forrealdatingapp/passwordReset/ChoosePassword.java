package forrealdatingapp.passwordReset;



import forrealdatingapp.App;
import forrealdatingapp.Scenes.LoginWindow;
import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.AuthRequests;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ChoosePassword {
    public void showChoosePassword(Stage stage, User user){
        
        VBox root = new VBox(15);
        Label passLabel = new Label("enter your new password");
        TextField password = new TextField();
        password.setPromptText("enter your password here");
        Button reset = new Button("reset password");
        reset.setOnAction((actionEvent) -> {
                String passwordString = password.getText();
                String status = "";
                if(user != null)
                    status = AuthRequests.Resetusrpass(passwordString, user.getEmail());
                if(status.contains("403")){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText(status.split("\\|")[1]);
                    alert.showAndWait();
                    status = "";

                }
                else{
                    App.loginWindow.showLoginWindow(stage, null);
                    if (status.contains("201")){
                        LoginWindow.error.setText(status.split("\\|")[1]);
                        LoginWindow.error.setTextFill(Color.GREEN);
                     }
                     else if(status.contains("404")){
                        LoginWindow.error.setText(status.split("\\|")[1]);
                        LoginWindow.error.setTextFill(Color.RED);
    
                     }
                }
        });
        // reset.setOnAction(()=>{
           
            //     String passwordString = password.getText();
            //     if(user != null)
            //     String status = UsersRouteRequests.Resetusrpass(passwordString, user.getEmail());

            // });




        
        
        
        
        root.getChildren().addAll(passLabel, password, reset);
        Scene scene = new Scene(root, 500,600);
        stage.setScene(scene);

        
    }
    public boolean isValid(String password, Stage stage, User user){
        return true;
    }
    
}
