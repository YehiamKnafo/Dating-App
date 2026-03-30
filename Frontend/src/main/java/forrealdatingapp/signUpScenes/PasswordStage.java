package forrealdatingapp.signUpScenes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.routes.AuthRequests;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Map;

public class PasswordStage  {
    public void showPasswordStage(Stage stage)  {
        // Create the main layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        // Password label
        Label passwordLabel = new Label("Enter your password:");

        // Password field and visibility toggle
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("enter your password here");
        TextField visiblePasswordField = new TextField(); // For showing the password in plain text
        visiblePasswordField.setManaged(false); // Not visible by default
        visiblePasswordField.setVisible(false);

        // Sync the text between PasswordField and TextField
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        // Eye button for toggling visibility
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

        // Add password field and button to a horizontal layout
        HBox passwordBox = new HBox(10, passwordField, visiblePasswordField, toggleVisibilityButton);
        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String password = passwordField.getText();

            if (password.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Password cannot be empty.", ButtonType.OK);
                alert.show();
            } else {
                // Set the password in the user object (you can handle hashing later)
                UnloggedUserManager.getUser().setPassword(password);
                try {
                    ObjectMapper om = new ObjectMapper();
                    String json = om.writeValueAsString(UnloggedUserManager.getUser());
                    Map<String,Object> returnMap = AuthRequests.postSignup(json);

                    if(!(Boolean) returnMap.get("bool")){
                        Alert alert = new Alert(Alert.AlertType.ERROR, (String) returnMap.get("body"), ButtonType.CLOSE);
                        alert.show();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password saved successfully!", ButtonType.OK);
                        alert.show();
                        UserDetails.uploadedPictures = null;
                        if (UnloggedUserManager.getUser() != null && UnloggedUserManager.getUser().getEmail() != null){
                            boolean ok = AuthRequests.dropOtp(UnloggedUserManager.getUser().getEmail());
                            if (ok) System.out.println("otp dropped");
                        }
                        UnloggedUserManager.setUser(null);
                        // Optionally move to the next stage
                        SuccessPage sp = new SuccessPage();
                        sp.showSuccessPage(stage);

                    }

                    
                    
                } catch (JsonProcessingException exp) {
                    System.err.println(exp.getLocalizedMessage());
                }
                

                // Success message
      
            }
        });

        // Add components to the layout
        root.getChildren().addAll(passwordLabel, passwordBox, submitButton);
        App.BackToLoginBtn(root, stage);

        // Set up the scene and stage
        Scene scene = new Scene(root, 600, 800);
        stage.setScene(scene);
        stage.setTitle("Set Password");
        stage.show();
    }
}
