package forrealdatingapp.Scenes;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.MatchingRequests;
import forrealdatingapp.utilities.ImageUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MatchedProfilePage {
    private int count = 0;

    public void showMatchedProfilePage(Stage stage, String _id, String matchID) throws URISyntaxException{
        File file;
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("matchID", matchID);
        User matchedUserDetails = MatchingRequests.getMatchedProfile(_id, jsonMap);
        List<String> pics = matchedUserDetails.getPictures();
        VBox layout = new VBox();
        layout.setPadding(new Insets(15));
        ImageView bigProfileImageView = new ImageView();
        
        Image image;
        try {
            image = ImageUtils.loadCorrectedImage(pics.get(0));
            bigProfileImageView.setImage(image);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bigProfileImageView.setFitHeight(600);
        bigProfileImageView.setFitWidth(500);
        HBox imageControl = new HBox(15);
        Button prev = new Button("<");
        int pictureListSize = pics.size();
        prev.setOnAction((actionEvent) -> {
            String lastFile = pics.get(pictureListSize - 1);
            if(count != 0){
                
                try {
                    bigProfileImageView.setImage(ImageUtils.loadCorrectedImage(pics.get(--count%pictureListSize)));
                } catch (IOException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
                
                
            } else
                try {
                    bigProfileImageView.setImage(ImageUtils.loadCorrectedImage(lastFile));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException ex) {
            }
        });
        Button next = new Button(">");
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String modFile = pics.get(++count% pictureListSize);
                try {
                    bigProfileImageView.setImage(ImageUtils.loadCorrectedImage(modFile));
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (URISyntaxException ex) {
                }
            }
        });
        prev.setAlignment(Pos.BOTTOM_CENTER);
        next.setAlignment(Pos.BOTTOM_CENTER);
        imageControl.getChildren().addAll(prev ,bigProfileImageView ,next);
        imageControl.setAlignment(Pos.TOP_CENTER);
        VBox contentDetails = new VBox();
        Label name = new Label(String.format("Name:   %s", matchedUserDetails.getFirstName()));
        Label age = new Label(String.format("Age:   %d", matchedUserDetails.getAge()));

        contentDetails.getChildren().addAll(name, age);
        contentDetails.setAlignment(Pos.CENTER);
        
        layout.getChildren().addAll(imageControl,contentDetails);
        Scene scene = new Scene(layout, 700, 700);
        stage.setScene(scene);
        
    }
    


}
