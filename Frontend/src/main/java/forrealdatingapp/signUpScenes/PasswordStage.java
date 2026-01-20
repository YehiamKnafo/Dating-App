package forrealdatingapp.signUpScenes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
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

public class PasswordStage  {
    public void showPasswordStage(Stage stage, User user)  {
        // Create the main layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        // Password label
        Label passwordLabel = new Label("Enter your password:");

        // Password field and visibility toggle
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
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

            if (password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Password cannot be empty.", ButtonType.OK);
                alert.showAndWait();
            } else {
                // Set the password in the user object (you can handle hashing later)
                user.setPassword(password);
                try {
                    ObjectMapper om = new ObjectMapper();
                    String json = om.writeValueAsString(user);
                    boolean ok = AuthRequests.postSignup(json);
                    if(!ok){
                        Alert alert = new Alert(Alert.AlertType.ERROR, "cannot create account!", ButtonType.CLOSE);
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password saved successfully!", ButtonType.OK);
                        alert.showAndWait();
        
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
        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Set Password");
        stage.show();
    }
}
