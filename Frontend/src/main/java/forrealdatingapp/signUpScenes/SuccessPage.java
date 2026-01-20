package forrealdatingapp.signUpScenes;
import forrealdatingapp.Scenes.LoginWindow;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SuccessPage {

    public void showSuccessPage(Stage stage) {
        // Create the layout
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // Success message
        Label successLabel = new Label("Your account has been created successfully!");
        successLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Back to Login button
        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setStyle("-fx-font-size: 14px;");
        backToLoginButton.setOnAction(e -> {
            // Logic to navigate back to the login screen
            // Replace with your actual navigation logic
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.showLoginWindow(stage,null);
        });

        // Add elements to the layout
        root.getChildren().addAll(successLabel, backToLoginButton);

        // Set the scene and stage
        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Success");
        stage.show();
    }
}
