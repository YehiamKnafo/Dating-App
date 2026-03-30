package forrealdatingapp.Scenes;


import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;

import com.fasterxml.jackson.core.JsonProcessingException;
import forrealdatingapp.App;
import forrealdatingapp.mangers.TokenManager;
import forrealdatingapp.WebSocket;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.controlsfx.control.RangeSlider;

import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.UserProfileRequests;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static forrealdatingapp.signUpScenes.PrefrencesWindow.dateBoxSetup;
import static forrealdatingapp.signUpScenes.PrefrencesWindow.nullChecks;

public class PrefrencesWindow {
    public void showPrefrencesWindow(Stage stage, User user) {
        // Create a VBox for layout
        VBox root = new VBox(5);
        root.setPadding(new Insets(20));
        Button backtomainpage = new Button("back to main page");
        backtomainpage.setOnAction((actionEvent) -> {
            MainPage mp = new MainPage();
            mp.showMainPage(stage);
        });

        LocalDate date = OffsetDateTime.parse(user.getBirthDate()).toLocalDate();
//        System.out.println(date);
        Label firstName = new Label("First Name:");
        Label lastName = new Label("Last Name:");
        TextField firstname = new TextField();
        firstname.setText(user.getFirstName());
        firstname.setPromptText("Enter your first name:");
        TextField lastname = new TextField();
        lastname.setPromptText("Enter your last name:");
        lastname.setText(user.getLastName());
        // Birth Date
        Label birthDateLabel = new Label("Update Your Birth Date:");
        TextField year = new TextField();
        TextField day = new TextField();
        ComboBox<Integer> month = new ComboBox<>();
        year.setText(Integer.toString(date.getYear()));
        month.setValue(date.getMonthValue());
        day.setText(Integer.toString(date.getDayOfMonth()));
        HBox dateBox = dateBoxSetup(year,month,day);
        // Create a list of years from 1950 to 2026

        // 1. Create a specific ValueFactory

        //Then add dateBox to root.getChildren() instead of adding them separately
        // Gender
        Label genderLabel = new Label("Update Your Gender:");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderGroup);
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderGroup);
        RadioButton otherButton = new RadioButton("Other");
        otherButton.setToggleGroup(genderGroup);
        if (user.getGender().equals("Male")){
            genderGroup.selectToggle(maleButton);
        } else if (user.getGender().equals("Female")) {
            genderGroup.selectToggle(femaleButton);

        }
        else {
            genderGroup.selectToggle(otherButton);

        }

        HBox genderBox = new HBox(10, maleButton, femaleButton, otherButton);

        // Gender preference
        Label preferenceLabel = new Label("Update The Gender You Would Like To Date:");
        ToggleGroup preferenceGroup = new ToggleGroup();
        RadioButton preferMaleButton = new RadioButton("Male");
        preferMaleButton.setToggleGroup(preferenceGroup);
        RadioButton preferFemaleButton = new RadioButton("Female");
        preferFemaleButton.setToggleGroup(preferenceGroup);
        RadioButton preferOtherButton = new RadioButton("Other");
        preferOtherButton.setToggleGroup(preferenceGroup);
        if (user.getPreferredGender().equals("Male")){
            preferenceGroup.selectToggle(preferMaleButton);
        } else if (user.getPreferredGender().equals("Female")) {
            preferenceGroup.selectToggle(preferFemaleButton);

        }
        else {
            preferenceGroup.selectToggle(preferOtherButton);

        }
        HBox preferenceBox = new HBox(10, preferMaleButton, preferFemaleButton, preferOtherButton);
        
        // Age Range Slider 
        RangeSlider ageSlider = new RangeSlider(18, 99, user.getMinPreferredAge(), user.getMaxPreferredAge());
        
        // Fine-tuned settings for precise age selection
        ageSlider.setShowTickMarks(true);
        ageSlider.setShowTickLabels(true);
        ageSlider.setMajorTickUnit(10);  // Label every 10 years
        ageSlider.setMinorTickCount(9);  // 9 minor ticks between majors (shows every year)
        ageSlider.setSnapToTicks(true);  // Snap to exact integer values
        ageSlider.setBlockIncrement(1);  // Move by 1 year with keyboard
        
        // Display current values with exact numbers
        Label rangeLabel = new Label();
        updateLabel(rangeLabel, ageSlider);
        
        // Update label when values change
        ageSlider.lowValueProperty().addListener((obs, oldVal, newVal) -> {
            updateLabel(rangeLabel, ageSlider);
        });
        
        ageSlider.highValueProperty().addListener((obs, oldVal, newVal) -> {
            updateLabel(rangeLabel, ageSlider);
        });
        
        VBox slider = new VBox(10, 
            new Label("Select Exact Age Range:"),
            rangeLabel,
            ageSlider
        );



        // Bio
        Label bioLabel = new Label("Bio:");
        TextArea bioTextArea = new TextArea();
        bioTextArea.setPromptText("Tell us about yourself...(Bio not required)");
//        System.out.println(user.getBio());
        if (!user.getBio().isBlank())
            bioTextArea.setText(user.getBio());
        bioTextArea.setWrapText(true);
        bioTextArea.setPrefRowCount(3);

        // Submit Button
        Button submitButton = new Button("Update Preferences");
        submitButton.setOnAction(e -> {
                LocalDate birthDate = null;
                // Calculate age from birth date
                if (nullChecks(year.getText(), month.getValue(),day.getText())){

                    birthDate = LocalDate.of(Integer.parseInt(year.getText()),month.getValue(), Integer.parseInt(day.getText()));
                }
                String birthdateString = null;
                String gender = null;
                String PreferredGender = null;
                String firstnameString = null;
                String lastnameString = null;
                if (firstname.getText() != null) {
                    firstnameString = firstname.getText();
                }
                if (lastname.getText() != null) {
                    lastnameString = lastname.getText();
                }

                
                if (birthDate != null) {
                    birthdateString = birthDate.toString();
                }
                int age = calculateAge(birthDate);
                if(((RadioButton) genderGroup.getSelectedToggle()) != null){

                    gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
                }
                if(((RadioButton) preferenceGroup.getSelectedToggle()) != null){

                    PreferredGender = ((RadioButton) preferenceGroup.getSelectedToggle()).getText();
                }
                int min = (int)ageSlider.getLowValue();
                int max = (int)ageSlider.getHighValue();
                User userPref = new User();
                userPref.setBio(bioTextArea.getText());
                userPref.setBirthDate(birthdateString);
                userPref.setAge(age); // Assuming User has an `age` field
                userPref.setGender(gender);
                userPref.setPreferredGender(PreferredGender);
                userPref.setMinPreferredAge(min);
                userPref.setMaxPreferredAge(max);
                userPref.setFirstName(firstnameString);
                userPref.setLastName(lastnameString);
                

                
                String ok = UserProfileRequests.UpdatePreferrences(userPref, App.id);
                if (ok.isBlank()){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Preferences saved successfully!", ButtonType.OK);
                    alert.show();
                }else{

                    Platform.runLater(()-> {
                        year.setText(Integer.toString(date.getYear()));
                        month.setValue(date.getMonthValue());
                        day.setText(Integer.toString(date.getDayOfMonth()));
                    });
                    Alert alert = new Alert(Alert.AlertType.ERROR, ok, ButtonType.CLOSE);
                    alert.show();
                }
                // Optionally transition to another stage
            
        });
        Button deleteAccountBtn = ProfilePage.createStyledButton("Delete Account", "delete");
        deleteAccountBtn.setOnAction(actionEvent -> {

            boolean isDeleted = UserProfileRequests.deleteAccount(App.id);
            if (isDeleted){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "account deleted Successfully", ButtonType.OK);
                alert.show();
                App.clear();
                LoginWindow loginWindow = new LoginWindow();

                try {
                    loginWindow.showLoginWindow(stage);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }

        });
        // Add all elements to the layout
        root.getChildren().addAll(
                backtomainpage,firstName,
                firstname,lastName,
                lastname,
                birthDateLabel, dateBox,
                genderLabel, genderBox,
                preferenceLabel, preferenceBox,
                slider,
                bioLabel, bioTextArea,
                submitButton,
                deleteAccountBtn

        );
    

        // Set the scene and show the stage
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 900, 800);
        stage.setScene(scene);
        stage.setTitle("Preferences & Settings");
        stage.show();
    }



    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0; // Return 0 if birth date is null
        }
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }
        
    private void updateLabel(Label label, RangeSlider slider) {
        label.setText(String.format("Age Range: %d to %d years", 
            (int)slider.getLowValue(), 
            (int)slider.getHighValue()));
    }


    
}
