package forrealdatingapp.Scenes;


import java.time.LocalDate;
import java.time.Period;

import org.controlsfx.control.RangeSlider;

import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.UserProfileRequests;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrefrencesWindow {
    public void showPrefrencesWindow(Stage stage,String id) {
        // Create a VBox for layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        Button backtomainpage = new Button("back to main page");
        backtomainpage.setOnAction((actionEvent) -> {
            MainPage mp = new MainPage();
            mp.showMainPage(stage, id);
        });
        TextField firstname = new TextField();
        firstname.setPromptText("Enter your first name:");
        TextField lastname = new TextField();
        lastname.setPromptText("Enter your last name:");
        // Birth Date
        Label birthDateLabel = new Label("Update Your Birth Date:");
        DatePicker birthDatePicker = new DatePicker();

        // Gender
        Label genderLabel = new Label("Update Your Gender:");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderGroup);
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderGroup);
        RadioButton otherButton = new RadioButton("Other");
        otherButton.setToggleGroup(genderGroup);

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

        HBox preferenceBox = new HBox(10, preferMaleButton, preferFemaleButton, preferOtherButton);
        
        // Age Range Slider 
        RangeSlider ageSlider = new RangeSlider(18, 99, 18, 65);
        
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
        bioTextArea.setPromptText("Tell us about yourself...");
        bioTextArea.setWrapText(true);

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
       
                // Calculate age from birth date
                LocalDate birthDate = birthDatePicker.getValue();
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
                

                
                UserProfileRequests.UpdatePreferrences(userPref, id);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Preferences saved successfully!", ButtonType.OK);
                alert.showAndWait();
                // Optionally transition to another stage
            
        });

        // Add all elements to the layout
        root.getChildren().addAll(
                backtomainpage,
                firstname,lastname,
                birthDateLabel, birthDatePicker,
                genderLabel, genderBox,
                preferenceLabel, preferenceBox,
                slider,
                bioLabel, bioTextArea,
                submitButton
        );
    

        // Set the scene and show the stage
        Scene scene = new Scene(root, 500, 600);
        stage.setScene(scene);
        stage.setTitle("Preferences");
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
