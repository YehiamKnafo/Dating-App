package forrealdatingapp.signUpScenes;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.utilities.CloudinaryUtils;
import forrealdatingapp.utilities.ImageUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import static forrealdatingapp.App.progressIndicator;
import static forrealdatingapp.App.serverStatusIndicator;


public class UserDetails {
    public static List<String> uploadedPictures;
    public void showUserDetails(Stage stage) {
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
        uploadedPictures = new ArrayList<>();
        HBox pictureHbox = new HBox(3);

        uploadButton.setOnAction(e -> {
            if (uploadedPictures.size() < 6) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Picture");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    Label fileLabel = new Label(selectedFile.getName());
                    /*
                     * selectedFile.getAbsolutePath()) -> this file is what cloudinary gets
                     * and instead of add it to the list of strings ill add the cloudinary url to the list of strings
                     */
                    //TASK TEST
                    Task<String> uploadPictureTask = new Task<String>() {
                        @Override
                        protected String call() throws Exception {
                            return CloudinaryUtils.Upload(selectedFile);
                        }
                    };
                    uploadPictureTask.setOnScheduled(e1->{

                            progressIndicator.setVisible(true);

                    });
                    uploadPictureTask.setOnSucceeded(e2->{

                            progressIndicator.setVisible(false);

                        try {
                            String res = uploadPictureTask.get();
                            uploadedPictures.add(res);
                            Image newimg;
                            try {
                                newimg = ImageUtils.loadCorrectedImage(res);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (URISyntaxException ex) {
                                throw new RuntimeException(ex);
                            }

                            ImageView newImageView = new ImageView(newimg);
                            newImageView.setFitWidth(100);
                            newImageView.setFitHeight(100);
                            // hbox filelabel, delete icon
                            Button btn = new Button();
                            btn.setBackground(Background.EMPTY);
                            btn.setGraphic(new FontIcon("fas-trash"));

                            HBox crudOps = new HBox(10,fileLabel,btn);
                            VBox border = new VBox(newImageView, crudOps);
                            border.setStyle("-fx-border-color:  #7FB3FF; -fx-border-width: 3px;");

                            pictureHbox.getChildren().add(border);
                            btn.setOnAction(actionEvent -> {
                                boolean ok = CloudinaryUtils.deleteFromCloudinaryByUrl(res);
                                if (ok){
//                                    System.out.println("success");
                                    pictureHbox.getChildren().remove(border);
                                }
//                                System.out.println("clicked" + btn);
                            });
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        } catch (ExecutionException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                    new Thread(uploadPictureTask).start();
                }
                    //TASK TEST END

//                    String urlToDB = CloudinaryUtils.Upload(selectedFile);
//                    if(urlToDB == null) return;
//                    uploadedPictures.add(urlToDB);
////                    picturesBox.getChildren().add(fileLabel);
//                    Image newimg;
//                    try {
//                        newimg = ImageUtils.loadCorrectedImage(urlToDB);
//                    } catch (IOException ex) {
//                        throw new RuntimeException(ex);
//                    } catch (URISyntaxException ex) {
//                        throw new RuntimeException(ex);
//                    }
//
//                    ImageView newImageView = new ImageView(newimg);
//                    newImageView.setFitWidth(100);
//                    newImageView.setFitHeight(100);
//                    // hbox filelabel, delete icon
//                    Button btn = new Button();
//                    btn.setBackground(Background.EMPTY);
//                    btn.setGraphic(new FontIcon("fas-trash"));
//
//                    HBox crudOps = new HBox(10,fileLabel,btn);
//                    VBox border = new VBox(newImageView, crudOps);
//                    border.setStyle("-fx-border-color:  #7FB3FF; -fx-border-width: 3px;");
//
//                    pictureHbox.getChildren().add(border);
//                    btn.setOnAction(actionEvent -> {
//                        boolean ok = CloudinaryUtils.deleteFromCloudinaryByUrl(urlToDB);
//                        if (ok){
//                            System.out.println("success");
//                            pictureHbox.getChildren().remove(border);
//                        }
//                        System.out.println("clicked" + btn);
//                    });


            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You can only upload up to 6 pictures.", ButtonType.OK);
                alert.show();
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
                alert.show();
            } else {
                // Save user details
                UnloggedUserManager.getUser().setFirstName(firstName);
                UnloggedUserManager.getUser().setLastName(lastName);
                UnloggedUserManager.getUser().setUsername(username);
                UnloggedUserManager.getUser().setPictures(uploadedPictures);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User details saved successfully!", ButtonType.OK);
                alert.show();
                // Optionally transition to another stage
                PrefrencesWindow pWindow = new PrefrencesWindow();
                pWindow.showPrefrencesWindow(stage);
            }
        });

        // Add all elements to the layout
        root.getChildren().addAll(firstNameLabel, firstNameField, lastNameLabel, lastNameField, usernameLabel, usernameField, picturesLabel, uploadButton, pictureHbox, submitButton);
        StackPane stackPane = new StackPane(root, progressIndicator);
        StackPane.setAlignment(progressIndicator, Pos.CENTER);
        App.BackToLoginBtn(root, stage);
        sp.setContent(stackPane);

        // Set the scene and show the stage
        Scene scene = new Scene(sp, 600, 800);
        stage.setScene(scene);
        stage.setTitle("User Details");
        stage.show();
    }
}
