package forrealdatingapp.signUpScenes;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.utilities.CloudinaryUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UserDetails {

    public void showUserDetails(Stage stage, User user) {
        // Create a VBox for the layout
        ScrollPane sp = new ScrollPane();
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // First Name field
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter your first name");

        // Last Name field
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last name");

        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        // Pictures section
        Label picturesLabel = new Label("Upload Pictures (Up to 6):");
        Button uploadButton = new Button("Upload Picture");
        HBox picturesBox = new HBox(10); // Holds the picture file names
        picturesBox.setPadding(new Insets(10));
        List<String> uploadedPictures = new ArrayList<>();

        uploadButton.setOnAction(e -> {
            if (uploadedPictures.size() < 6) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Picture");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    /*
                     * selectedFile.getAbsolutePath()) -> this file is what cloudinary gets
                     * and instead of add it to the list of strings ill add the cloudinary url to the list of strings
                     */
                    String urlToDB = CloudinaryUtils.Upload(selectedFile);
                    if(urlToDB == null) return;
                    uploadedPictures.add(urlToDB);
                    Label fileLabel = new Label(selectedFile.getName());
                    picturesBox.getChildren().add(fileLabel);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You can only upload up to 6 pictures.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || uploadedPictures.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields and upload at least one picture.", ButtonType.OK);
                alert.showAndWait();
            } else {
                // Save user details
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setUsername(username);
                user.setPictures(uploadedPictures);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User details saved successfully!", ButtonType.OK);
                alert.showAndWait();
                // Optionally transition to another stage
                PrefrencesWindow pWindow = new PrefrencesWindow();
                pWindow.showPrefrencesWindow(stage, user);
            }
        });

        // Add all elements to the layout
        root.getChildren().addAll(firstNameLabel, firstNameField, lastNameLabel, lastNameField, usernameLabel, usernameField, picturesLabel, uploadButton, picturesBox, submitButton);
        App.BackToLoginBtn(root, stage);
        sp.setContent(root);

        // Set the scene and show the stage
        Scene scene = new Scene(sp, 400, 300);
        stage.setScene(scene);
        stage.setTitle("User Details");
        stage.show();
    }
}
