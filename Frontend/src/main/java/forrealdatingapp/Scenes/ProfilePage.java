package forrealdatingapp.Scenes;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.FileRequests;
import forrealdatingapp.routes.UserProfileRequests;
import forrealdatingapp.utilities.CloudinaryUtils;
import forrealdatingapp.utilities.ImageUtils;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProfilePage {
    private static User user;
    private static final ObjectMapper om = new ObjectMapper();
    private static final String PRIMARY_COLOR = "#2196F3";
    private static final String SECONDARY_COLOR = "#FFF";
    private static final String BACKGROUND_COLOR = "#F5F5F5";
    private static final String RED_COLOR = "#c40a0a";
    
    private final Text name = new Text();
    private final Text age = new Text();
    private final Text bio = new Text();
    
    private final List<ImageView> pictureViews = new ArrayList<>();
    private final GridPane pictureGrid = new GridPane();
    private final VBox detailsSection = new VBox(15);
    
    private final Label notificationLabel = new Label();
    private final PauseTransition notificationTimeout = new PauseTransition(Duration.seconds(3));
    private String _id;
    public void showProfilePage(Stage stage, String _id, User currentUser) throws URISyntaxException {
        user = currentUser;
        this._id = _id;
        // Initialize example data
        name.setText(user.getFirstName() + " " + user.getLastName());
        age.setText(Integer.toString(user.getAge()));
        bio.setText(user.getBio());
        List<String> userPictures = user.getPictures(); // Assumes pictures are stored as Strings (e.g., URLs or file paths)
        System.out.println(userPictures);
        initializePictureGrid(userPictures);

        VBox root = createStyledRoot();
        setupNotificationLabel();
        
        VBox header = createProfileHeader();
        VBox pictureSection = createPictureSection();
        VBox details = createDetailsSection(stage,_id);
        
        root.getChildren().addAll(notificationLabel, header, pictureSection, details);
        ScrollPane scrollPane = new ScrollPane(root);  // Make the VBox scrollable
        scrollPane.setFitToWidth(true);  // Make content fit to ScrollPane width
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        Scene scene = new Scene(scrollPane, 800, 900);
        stage.setTitle("Profile");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createStyledRoot() {
        VBox root = new VBox(30);
        root.setStyle(String.format("-fx-background-color: %s;", BACKGROUND_COLOR));
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private VBox createProfileHeader() throws URISyntaxException {
        VBox header = new VBox(15);
        header.setAlignment(Pos.CENTER);
        
        // Create a default avatar placeholder using shapes
  
        
        StackPane avatarPlaceholder = createAvatarPlaceholder();
        
        // Edit profile picture button
        Button editProfilePic = createStyledButton("Change Profile Picture", "photo");
        Button removeProfilePic = createStyledButton("Remove Profile Picture", "photo");
        removeProfilePic.setOnAction((e) -> {
         avatarPlaceholder.getChildren().clear();
         avatarPlaceholder.getChildren().add(createAvatarPlaceholder());
            


        });
        editProfilePic.setOnAction(e -> handleProfilePictureChange(avatarPlaceholder));
        if (user.getProfilePicture() != null) {
            user.getProfilePicture();
            Image image;
            try {
                image = ImageUtils.loadCorrectedImage(user.getProfilePicture());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                
                // Create circular clip for the image
                Circle clip = new Circle(60,60,60);
                imageView.setClip(clip);
                // imageView.setPreserveRatio(true);
                // Replace placeholder with new image
                avatarPlaceholder.getChildren().clear();
                avatarPlaceholder.getChildren().add(imageView);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        }
        header.getChildren().addAll(avatarPlaceholder, editProfilePic, removeProfilePic);
        return header;
    }

    private StackPane createAvatarPlaceholder() {
        StackPane avatarPlaceholder = new StackPane();
        
        // Create circular background
        Circle circle = new Circle(60);
        circle.setFill(Color.web(PRIMARY_COLOR));
        
        // Create initials label
        Label initials = new Label(getInitials(name.getText()));
        initials.setStyle("""
            -fx-text-fill: white;
            -fx-font-size: 36px;
            -fx-font-weight: bold;
            """);
        
        avatarPlaceholder.getChildren().addAll(circle, initials);
        avatarPlaceholder.setEffect(new DropShadow(10, Color.gray(0.5)));
        return avatarPlaceholder;
    }

    private String getInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) return "?";
        
        String[] names = fullName.split(" ");
        StringBuilder initials = new StringBuilder();
        
        for (String name : names) {
            if (!name.isEmpty()) {
                initials.append(name.charAt(0));
            }
        }
        
        return initials.toString().toUpperCase();
    }

    private void handleProfilePictureChange(StackPane avatarContainer) {
        FileChooser fileChooser = createImageFileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.toString());
            try {
                String urlToDB = CloudinaryUtils.Upload(file);
                Image image = ImageUtils.loadCorrectedImage(urlToDB);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(120);
                imageView.setFitWidth(120);
                
                // Create circular clip for the image
                Circle clip = new Circle(60,60,60);
                imageView.setClip(clip);
                imageView.setPreserveRatio(true);
                // Replace placeholder with new image
                Map<String, String> jsonMap = new HashMap<>();
                
                jsonMap.put("url", urlToDB);
                boolean success = UserProfileRequests.updateProfilePicture(_id, jsonMap);
                if(success){

                    avatarContainer.getChildren().clear();
                    avatarContainer.getChildren().add(imageView);
                    showSuccess("Profile picture updated successfully");
                }
                else{

                    showError("Failed to update image");
                }
           
            } catch (Exception e) {
                showError("Failed to load image: " + e.getMessage());
            }
        }
    }

    private VBox createPictureSection() {
        VBox pictureSection = new VBox(15);
        pictureSection.setAlignment(Pos.CENTER);
        
        pictureGrid.setHgap(15);
        pictureGrid.setVgap(15);
        pictureGrid.setAlignment(Pos.CENTER);
        
        Button addPictureButton = createStyledButton("Add Picture", "add");
        addPictureButton.setOnAction(e -> handleAddPicture());
        
        Label sectionTitle = new Label("My Pictures");
        sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        pictureSection.getChildren().addAll(sectionTitle, pictureGrid, addPictureButton);
        return pictureSection;
    }

    private VBox createDetailsSection(Stage stage, String _id) {
    // Clear existing children (if any)
    detailsSection.getChildren().clear();

    // Create non-editable name label
    Label nameLabel = new Label(name.getText());
    nameLabel.setStyle(getTextFieldStyle());

    // Create non-editable age label
    Label ageLabel = new Label(age.getText());
    ageLabel.setStyle(getTextFieldStyle());

    // Create non-editable bio text (with wrapping)
    Text bioText = new Text(bio.getText());
    bioText.setWrappingWidth(400); // Adjust width as needed
    VBox bioContainer = new VBox(bioText);
    bioContainer.setStyle(getTextFieldStyle());
    bioContainer.setPadding(new Insets(10));

    // Back button (no edit/save buttons)
    Button removeBio = createStyledButton("Remove Bio", "delete");
    removeBio.setOnAction((e)->{
        User bioChange = new User();
        bioChange.setBio("");
        UserProfileRequests.UpdateBio(bioChange, _id);
        showSuccess("Bio removed successfuly");

    });
    Button backToProfileButton = createStyledButton("Back to Profile", "back");
    backToProfileButton.setOnAction((actionEvent) -> {
        MainPage mp = new MainPage();
        mp.showMainPage(stage, _id);
    });
    HBox options = new HBox(15);
    options.getChildren().addAll(removeBio, backToProfileButton);

    // Section title
    Label sectionTitle = new Label("Profile Details");
    sectionTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    // Add all components to the details section
    detailsSection.getChildren().addAll(
        sectionTitle,
        new Label("Name"),
        nameLabel,
        new Label("Age"),
        ageLabel,
        new Label("Bio"),
        bioContainer,
        options
    );

    return detailsSection;
}

    // ... (rest of the methods remain the same)

    private void setupNotificationLabel() {
        notificationLabel.setVisible(false);
        notificationLabel.setStyle("""
            -fx-background-color: #333333;
            -fx-text-fill: white;
            -fx-padding: 10;
            -fx-background-radius: 5;
            """);
        notificationLabel.setMaxWidth(Double.MAX_VALUE);
        notificationLabel.setAlignment(Pos.CENTER);
        
        notificationTimeout.setOnFinished(e -> notificationLabel.setVisible(false));
    }

    private void showNotification(String message, String type) {
        notificationLabel.setText(message);
        
        String backgroundColor = switch(type) {
            case "error" -> "#ff4444";
            case "success" -> "#4CAF50";
            default -> "#333333";
        };
        
        notificationLabel.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: white;
            -fx-padding: 10;
            -fx-background-radius: 5;
            """, backgroundColor));
            
        notificationLabel.setVisible(true);
        notificationTimeout.playFromStart();
    }

    private void showSuccess(String message) {
        showNotification(message, "success");
    }

    private void showError(String message) {
        showNotification(message, "error");
    }

    private TextField createStyledTextField(String prompt, String initialValue) {
        TextField field = new TextField(initialValue);
        field.setPromptText(prompt);
        field.setStyle(getTextFieldStyle());
        return field;
    }

    public Button createStyledButton(String text, String icon) {
        Button button = new Button(text);
        if (icon.equals("delete")) {
              button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: %s;
            -fx-padding: 10 20;
            -fx-background-radius: 5;
            -fx-cursor: hand;
            """, RED_COLOR, SECONDARY_COLOR));
        }
        else
            button.setStyle(String.format("""
            -fx-background-color: %s;
            -fx-text-fill: %s;
            -fx-padding: 10 20;
            -fx-background-radius: 5;
            -fx-cursor: hand;
            """, PRIMARY_COLOR, SECONDARY_COLOR));
        
        button.setOnMouseEntered(e -> 
            button.setEffect(new DropShadow(10, Color.web(PRIMARY_COLOR))));
        button.setOnMouseExited(e -> 
            button.setEffect(null));
            
        return button;
    }

    private String getTextFieldStyle() {
        return """
            -fx-background-color: white;
            -fx-padding: 10;
            -fx-background-radius: 5;
            -fx-border-radius: 5;
            -fx-border-color: #E0E0E0;
            -fx-border-width: 1;
            """;
    }

    private FileChooser createImageFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        return fileChooser;
    }

    private void handleAddPicture() {
        FileChooser fileChooser = createImageFileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                addPictureToGrid(file);
                //add also to db here
                Map<String, String> jsonMap = new HashMap<>();
                String urlToDB = CloudinaryUtils.Upload(file);
                jsonMap.put("url",urlToDB);
                // handle a cloud entry and create a url here for now file for system path
                String json = om.writeValueAsString(jsonMap);
                FileRequests.addPicture(json, user.get_id());
                showSuccess("Picture added successfully");
            } catch (Exception e) {
                showError("Failed to add picture: " + e.getMessage());
            }
        }
    }
    
    private void addPictureToGrid(File file) {
        // Create image view
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setFitWidth(150);
        
        // Create container for the image and buttons
        VBox pictureContainer = new VBox(10);
        pictureContainer.setAlignment(Pos.CENTER);
        pictureContainer.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5;");
        
        // Add hover effect
        pictureContainer.setOnMouseEntered(e -> 
            pictureContainer.setEffect(new DropShadow(10, Color.web(PRIMARY_COLOR))));
        pictureContainer.setOnMouseExited(e -> 
            pictureContainer.setEffect(null));
        
        // Create buttons
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        Button editButton = createStyledButton("Edit", "edit");
        editButton.setOnAction(e -> handleEditPicture(imageView));
        
        Button removeButton = createStyledButton("Remove", "remove");
        removeButton.setOnAction(e -> handleRemovePicture(pictureContainer));
        
        buttonContainer.getChildren().addAll(editButton, removeButton);
        pictureContainer.getChildren().addAll(imageView, buttonContainer);
        
        // Add to grid
        int column = pictureViews.size() % 3;
        int row = pictureViews.size() / 3;
        pictureGrid.add(pictureContainer, column, row);
        pictureViews.add(imageView);
    }
    
    private void handleEditPicture(ImageView imageView) {
        FileChooser fileChooser = createImageFileChooser();
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Image newImage = new Image(file.toURI().toString());
                imageView.setImage(newImage);
                showSuccess("Picture updated successfully");
            } catch (Exception e) {
                showError("Failed to update picture: " + e.getMessage());
            }
        }
    }
    
    private void handleRemovePicture(VBox pictureContainer) {
        pictureGrid.getChildren().remove(pictureContainer);
        
        // Find and remove the ImageView from our list
        pictureContainer.getChildren().stream()
            .filter(node -> node instanceof ImageView)
            .map(node -> (ImageView) node)
            .findFirst()
            .ifPresent(pictureViews::remove);
        
        // Reorganize the grid
        reorganizePictureGrid();
        showSuccess("Picture removed successfully");
        //TODO: REMOVE PICTURE FROM DATABASE
    }
    
    private void reorganizePictureGrid() {
        // Clear the grid
        pictureGrid.getChildren().clear();
        
        // Re-add all pictures in order
        for (int i = 0; i < pictureViews.size(); i++) {
            VBox container = (VBox) pictureViews.get(i).getParent();
            pictureGrid.add(container, i % 3, i / 3);
        }
    }
    
   
    private void initializePictureGrid(List<String> picturePaths) {
    pictureGrid.getChildren().clear();
    pictureViews.clear(); // Clear existing views
    
    if (picturePaths != null && !picturePaths.isEmpty()) {
        int col = 0, row = 0;
        for (String picturePath : picturePaths) {
            try {
                Image img = ImageUtils.loadCorrectedImage(picturePath);
                ImageView imageView = new ImageView(img);
                imageView.setFitHeight(150);
                imageView.setFitWidth(150);
                imageView.setPreserveRatio(true);
                
                // Create the same container structure as addPictureToGrid()
                VBox pictureContainer = new VBox(10);
                pictureContainer.setAlignment(Pos.CENTER);
                pictureContainer.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-background-radius: 5;");
                
                // Add hover effect
                pictureContainer.setOnMouseEntered(e -> 
                    pictureContainer.setEffect(new DropShadow(10, Color.web(PRIMARY_COLOR))));
                pictureContainer.setOnMouseExited(e -> 
                    pictureContainer.setEffect(null));
                
                // Create buttons (same as addPictureToGrid)
                HBox buttonContainer = new HBox(10);
                buttonContainer.setAlignment(Pos.CENTER);
                
                Button editButton = createStyledButton("Edit", "edit");
                editButton.setOnAction(e -> handleEditPicture(imageView));
                
                Button removeButton = createStyledButton("Remove", "remove");
                removeButton.setOnAction(e -> handleRemovePicture(pictureContainer));
                
                buttonContainer.getChildren().addAll(editButton, removeButton);
                pictureContainer.getChildren().addAll(imageView, buttonContainer);
                
                pictureGrid.add(pictureContainer, col, row);
                pictureViews.add(imageView);
                
                // Grid positioning
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } catch (Exception e) {
                showError("Failed to load picture: " + e.getMessage());
            }
        }
    }
}
    public void handleRealtimeBioDelete(){
        //TODO: SOCKET BIO DELETE

    }

    
}