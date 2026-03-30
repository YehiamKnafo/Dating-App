package forrealdatingapp.signUpScenes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import forrealdatingapp.mangers.UnloggedUserManager;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import jfxtras.scene.control.ListSpinner;
import org.controlsfx.control.RangeSlider;

import forrealdatingapp.App;
import forrealdatingapp.dtos.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PrefrencesWindow {

    public void showPrefrencesWindow(Stage stage) {
        // Create a VBox for layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Birth Date
        Label birthDateLabel = new Label("Birth Date:");
        TextField year = new TextField();
        ComboBox<Integer> month = new ComboBox<>();
        TextField day = new TextField();

        HBox dateBox = dateBoxSetup(year,month,day);
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
        bioTextArea.setPromptText("Tell us about yourself...(Bio is not required)");
        bioTextArea.setWrapText(true);

        // Submit Button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            LocalDate today = LocalDate.now();
            LocalDate birthDate = LocalDate.of(Integer.parseInt(year.getText()),month.getValue(), Integer.parseInt(day.getText()));
            if (!nullChecks(year.getText(), month.getValue(),day.getText())|| genderGroup.getSelectedToggle() == null ||
                preferenceGroup.getSelectedToggle() == null ) {

                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields and correctly.", ButtonType.OK);
                alert.show();
            } else if (birthDate.isBefore(today.minusYears(99)) ) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maximum age for the app: 99", ButtonType.OK);
                alert.show();

            } else if (birthDate.isAfter(today.minusYears(18))) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Minimum age for the app: 18", ButtonType.OK);
                    alert.show();

            } else {
                // Calculate age from birthdate

                String birthdateString = birthDate.toString();
                int age = calculateAge(birthDate);

                // Save preferences to user
                UnloggedUserManager.getUser().setBirthDate(birthdateString);
                UnloggedUserManager.getUser().setAge(age); // Assuming User has an `age` field
                UnloggedUserManager.getUser().setGender(((RadioButton) genderGroup.getSelectedToggle()).getText());
                UnloggedUserManager.getUser().setPreferredGender(((RadioButton) preferenceGroup.getSelectedToggle()).getText());
                UnloggedUserManager.getUser().setBio(bioTextArea.getText());
                UnloggedUserManager.getUser().setMinPreferredAge((int)ageSlider.getLowValue());
                UnloggedUserManager.getUser().setMaxPreferredAge((int)ageSlider.getHighValue());

                


                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Preferences saved successfully!", ButtonType.OK);
                alert.show();
                // Optionally transition to another stage
                PasswordStage pStage = new PasswordStage();
                pStage.showPasswordStage(stage);
            }
        });

        // Add all elements to the layout
        root.getChildren().addAll(
                birthDateLabel,dateBox,
                genderLabel, genderBox,
                preferenceLabel, preferenceBox,
                slider,
                bioLabel, bioTextArea,
                submitButton
        );
        App.BackToLoginBtn(root, stage);

        // Set the scene and show the stage
        Scene scene = new Scene(root, 600, 800);
        stage.setScene(scene);
        stage.setTitle("Preferences");
        stage.show();
    }

    public static HBox dateBoxSetup(TextField year, ComboBox<Integer> month, TextField day) {
        day.setPromptText("Day");
        month.setPromptText("Month");
        year.setPromptText("Year");
        month.setPrefWidth(100);
        day.prefWidthProperty().bind(month.prefWidthProperty());
        year.prefWidthProperty().bind(month.prefWidthProperty());
        // Force numeric only and limit to 4 digits
        year.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                year.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (year.getText().length() == 4) {
                String yearVal = year.getText().substring(0, 4);
                year.setText(yearVal);
//                System.out.println(yearVal);
            }
        });
        UnaryOperator<TextFormatter.Change> yearFilter = change -> {
            String newText = change.getControlNewText();
            // Only allow numbers AND limit to 4
            if (newText.matches("\\d*") && newText.length() <= 4) {
                return change;
            }
            return null; // This REJECTS the change completely
        };
        year.setTextFormatter(new TextFormatter<>(yearFilter));
        month.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12);
        // Month ComboBox
//        month.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue != null) {
//                System.out.println("Selected Month: " + newValue);
//
//
//            }
//        });

        day.textProperty().addListener((obs, old, val) -> {
            if (!val.matches("\\d*")) day.setText(val.replaceAll("[^\\d]", ""));
            String dayVal;
            if (day.getText().length() == 2){
                dayVal = day.getText().substring(0, 2);
                System.out.println(dayVal);
                day.setText(dayVal);
            } else if (day.getText().length() == 1) {
                dayVal = day.getText().substring(0, 1);
                System.out.println(dayVal);
                day.setText(dayVal);
            }
        });
        UnaryOperator<TextFormatter.Change> dayFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 2) {
                return change;
            }
            return null;
        };
        day.setTextFormatter(new TextFormatter<>(dayFilter));
        Label yearLabel = new Label("Year:");
        Label monthLabel = new Label("Month:");
        Label dayLabel = new Label("Day:");
        VBox yearBox = new VBox(2, yearLabel,year);
        VBox monthBox = new VBox(2, monthLabel,month);
        VBox dayBox = new VBox(2, dayLabel,day);
        HBox dateBox = new HBox(4, dayBox, monthBox, yearBox);
        // 1. Make the container focusable
        dateBox.setFocusTraversable(true);

// 2. Use Platform.runLater to ensure the scene is loaded before grabbing focus
        Platform.runLater(dateBox::requestFocus);
        return dateBox;
    }

    public static boolean nullChecks(String year, Integer month, String day) {
        return year != null && month != null && day != null;
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
