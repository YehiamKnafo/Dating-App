package forrealdatingapp;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Queue;

import static forrealdatingapp.App.progressIndicator;
import static forrealdatingapp.App.serverStatusIndicator;

public class LoadingScreen {
    public void showLoadingScreen(Stage stage){
        progressIndicator = new ProgressIndicator();
        progressIndicator.setMinWidth(40);
        progressIndicator.setStyle("-fx-progress-color: black;");
        Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/datingapplogo.png")));
        ImageView logoView = new ImageView(logo);
        logoView.setFitHeight(300);
        logoView.setFitWidth(300);
        VBox loadingItems = new VBox(40, logoView, progressIndicator, serverStatusIndicator);
        loadingItems.setAlignment(Pos.CENTER);
        Scene root = new Scene(loadingItems,600,800);
        stage.setScene(root);
        stage.show();
    }

}
