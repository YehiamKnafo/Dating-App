package forrealdatingapp.signUpScenes;

import java.time.LocalDate;
import java.time.Period;

import org.controlsfx.control.RangeSlider;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrefrencesWindow {

    public void showPrefrencesWindow(Stage stage, User user) {
        // Create a VBox for layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Birth Date
        Label birthDateLabel = new Label("Birth Date:");
        DatePicker birthDatePicker = new DatePicker();

        // Gender
        Label genderLabel = new Label("Your Gender:");
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleButton = new RadioButton("Male");
        maleButton.setToggleGroup(genderGroup);
        RadioButton femaleButton = new RadioButton("Female");
        femaleButton.setToggleGroup(genderGroup);
        RadioButton otherButton = new RadioButton("Other");
        otherButton.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(10, maleButton, femaleButton, otherButton);

        // Gender preference
        Label preferenceLabel = new Label("Gender You Want to Date:");
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
            if (birthDatePicker.getValue() == null || genderGroup.getSelectedToggle() == null ||
                preferenceGroup.getSelectedToggle() == null || bioTextArea.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields.", ButtonType.OK);
                alert.showAndWait();
            } else {
                // Calculate age from birth date
                LocalDate birthDate = birthDatePicker.getValue();
                String birthdateString = birthDate.toString();
                int age = calculateAge(birthDate);

                // Save preferences to user
                user.setBirthDate(birthdateString);
                user.setAge(age); // Assuming User has an `age` field
                user.setGender(((RadioButton) genderGroup.getSelectedToggle()).getText());
                user.setPreferredGender(((RadioButton) preferenceGroup.getSelectedToggle()).getText());
                user.setBio(bioTextArea.getText());
                user.setMinPreferredAge((int)ageSlider.getLowValue());
                user.setMaxPreferredAge((int)ageSlider.getHighValue());

                


                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Preferences saved successfully!", ButtonType.OK);
                alert.showAndWait();
                // Optionally transition to another stage
                PasswordStage pStage = new PasswordStage();
                pStage.showPasswordStage(stage, user);
            }
        });

        // Add all elements to the layout
        root.getChildren().addAll(
                birthDateLabel, birthDatePicker,
                genderLabel, genderBox,
                preferenceLabel, preferenceBox,
                slider,
                bioLabel, bioTextArea,
                submitButton
        );
        App.BackToLoginBtn(root, stage);

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
