package forrealdatingapp.Scenes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forrealdatingapp.WebSocket;
import forrealdatingapp.chatScenes.ChatZone;
import forrealdatingapp.dtos.Message;
import forrealdatingapp.dtos.UnreadCounter;
import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.MatchingRequests;
import forrealdatingapp.routes.MessageRequests;
import forrealdatingapp.utilities.ImageUtils;
import io.socket.client.Ack;
import io.socket.emitter.Emitter;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import static forrealdatingapp.chatScenes.ChatZone.*;

public class MatchesPage {
    private static ChatZone chatZone;
    private static ListView<HBox> matchesListView;
    private static Map<String, HBox> matchBoxMap = new HashMap<>();
    public static Map<String, Label> statusMap = new HashMap<>();
    public static Map<String, Label> lastMessageMap = new HashMap<>();
    public static Map<String, String> lastMessageTxtMap = new HashMap<>();
    public static Map<String, Label> MessageCounterMap = new HashMap<>();
    private static Label statusLabel;
    
    


    public void showMatchesPage(Stage stage,String _id) throws IOException {

        ChatZone.inChatZoneScreen = false;
        setupTypingListeners();
        cleanCurrentUnmatch();
        MessageRecieve();
        // Create a back button to return to the main page
        Button backToProfileButton = new Button("Back to Main Screen");
        backToProfileButton.setOnAction((actionEvent) -> {
            MainPage mainPage = new MainPage();
            mainPage.showMainPage(stage,_id);
        });
        
        // Create a list of matches (for demonstration purposes)


        // Create a ListView to display matches
        Pagination pagination = new Pagination();
        pagination.setStyle("-fx-page-information-visible: false;");
        pagination.setMaxPageIndicatorCount(1);
        pagination.setPageFactory((pageIndex)->{
            System.out.println(pageIndex);
            List<Match> matches = getMatches(_id, pageIndex + 1);
            
            if (!matches.isEmpty()){
                matchesListView = new ListView<>();
                for (Match match : matches) {
                    System.out.println(match);
                    HBox matchBox;
                    try {
                        matchBox = createMatchBox(match, stage,_id);
                        matchesListView.getItems().add(matchBox);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }
            
                    
                }
                MatchesPage.setUserStatus();
//                ChatZone.writer.println("Broadcast|" + _id);
                List<Message> lastMessages =  MessageRequests.getLastMessages(_id);
                List<Message> filteredMessages = lastMessages.stream()
                .filter(e -> matches.stream()
                        .anyMatch(match -> e.getRecieverID().equals(match.getId()) || e.getSenderID().equals(match.getId())))
                .toList();
                System.out.println(filteredMessages);
            
        
                for(Message msg : filteredMessages){
                    System.out.println(msg.getMessage());
                    String SenderID = msg.getSenderID();
                    String recieverID = msg.getRecieverID();
//                    System.out.println("sender - id v");
//                    System.out.println(SenderID);
                    
                    if (SenderID.equals(_id)) {
                        if(lastMessageMap.get(recieverID) != null){
                            lastMessageTxtMap.clear();
                            lastMessageTxtMap.put(_id, msg.getMessage());
                            lastMessageMap.get(recieverID).setText("me: " + msg.getMessage());
                        }
                        matchesListView.getItems().remove(matchBoxMap.get(recieverID));
                        matchesListView.getItems().add(0, matchBoxMap.get(recieverID));
                     }
                    else{
                        if(lastMessageMap.get(SenderID) != null){
                            lastMessageTxtMap.clear();
                            lastMessageTxtMap.put(SenderID, msg.getMessage());
                            lastMessageMap.get(SenderID).setText(msg.getSenderUsername() + ": " + msg.getMessage());
                        }    
                        matchesListView.getItems().remove(matchBoxMap.get(SenderID));
                        matchesListView.getItems().add(0, matchBoxMap.get(SenderID));


        
                    }
                } 
                List<UnreadCounter> unreadCounters = MessageRequests.ShowUnreadMessages(_id);
                List<UnreadCounter> filteredCounters = unreadCounters.stream()
                .filter(e -> matches.stream()
                .anyMatch(match -> e.getMatched_user_id().equals(match.getId()))).toList();
                for(UnreadCounter e : filteredCounters){
                    if (e.getMessageCounter() > 0) 
                    {
                        MessageCounterMap.get(e.getMatched_user_id()).setText("you have " + e.getMessageCounter() + " messages unread");
                        ChatZone.messageCounters.put(e.getMatched_user_id(), e.getMessageCounter());
                        matchesListView.getItems().remove(matchBoxMap.get(e.getMatched_user_id()));
                        matchesListView.getItems().add(0, matchBoxMap.get(e.getMatched_user_id()));
                        
                        
                    }
                }

            }
            else{
                pagination.setCurrentPageIndex(pageIndex);
                return new Label("no more matches!!!");
            }
  
            
            return matchesListView;

        });
        if (ChatZone.messageCounters != null) {
            System.out.println("test");
            System.out.println(ChatZone.messageCounters);
        }
        


        ScrollPane sp = new ScrollPane();
        sp.setContent(matchesListView);
        // Main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.getChildren().addAll(backToProfileButton, pagination);

        // Scene
        Scene scene = new Scene(mainLayout, 900, 800);
        stage.setScene(scene);
        stage.setTitle("Matches");
        stage.show();
        WebSocket.websocketio.INSTANCE.socketIoInstance.emit("RequestMatchStatus", _id);

//        ChatZone.writer.println("Broadcast|" + _id);
        // List<Message> lastMessages =  UsersRouteRequests.getLastMessages(_id);
        // for(Message msg : lastMessages){
        //     String SenderID = msg.getSenderID();
        //     String recieverID = msg.getRecieverID();
        //     System.out.println("sender - id v");
        //     System.out.println(SenderID);
            
        //     if (SenderID.equals(_id)) {
        //         if(lastMessageMap.get(recieverID) != null)
        //             lastMessageMap.get(recieverID).setText("me: " + msg.getMessage());
        //      }
        //     else{
        //         if(lastMessageMap.get(SenderID) != null)    
        //             lastMessageMap.get(SenderID).setText(msg.getSenderUsername() + ": " + msg.getMessage());

        //     }
        // } 
      
        
   

    }

    // Helper method to create a match box (HBox) for each match
private HBox createMatchBox(Match match,Stage stage,String _id) throws IOException, URISyntaxException {
    // System.out.println("match box entry");
    // Create an ImageView for the profile picture
    if(!statusMap.containsKey(match.getId())){
        statusLabel = new Label();
        statusMap.put(match.getId(),statusLabel);
    }
    Node imageOrLabel;
    ImageView profilePicture = new ImageView();
    profilePicture.setFitWidth(50);  // Set the width of the image
    profilePicture.setFitHeight(50); // Set the height of the image
    profilePicture.setPreserveRatio(true); // Preserve the aspect ratio
    profilePicture.setStyle("-fx-border-radius: 25px; -fx-border-color: #cccccc; -fx-border-width: 2px;"); // Optional: Add a circular border
    // Load the profile picture (replace with the actual image URL or path)
    String imageUrl = match.getProfilePictureUrl(); // Assuming Match class has a method to get the image URL
    System.out.println(imageUrl);
    System.out.println("why??");
    if (imageUrl != null && !imageUrl.isEmpty()) {
        // File file = new File(imageUrl);
        Image image = ImageUtils.loadCorrectedImage(imageUrl);
        profilePicture.setImage(image);
        imageOrLabel = profilePicture;
    } else {
        // Set a default image if no URL is provided
        imageOrLabel = new Label("no profile pic");
    }

    // Match name
    Label nameLabel = new Label(match.getName());
    nameLabel.setFont(Font.font(18));
    nameLabel.setTextFill(Color.DARKBLUE);

    // Last message
    Label lastMessageLabel = new Label();
    // System.out.println("matchboxtest:"+lastMessageMap);
    // System.out.println("check1:"+lastMessageMap.get(match.getId()) == null);
    if (lastMessageMap.get(match.getId()) == null) {

        lastMessageMap.put(match.getId(), lastMessageLabel);
        // System.out.println("check2:"+lastMessageMap);
    }
    lastMessageLabel.setFont(Font.font(14));
    lastMessageLabel.setTextFill(Color.GRAY);
    // Layout for match details
    HBox matchBox = new HBox(10);
    
    VBox matchDetails = new VBox(5);
    matchDetails.getChildren().addAll(nameLabel, lastMessageMap.get(match.getId()));
    Button unmatchButton = new Button("Unmatch");
    unmatchButton.setTextFill(Color.RED);
    unmatchButton.setStyle("-fx-background-color: transparent; -fx-border-color: red;");
    unmatchButton.setOnAction(e -> {
        System.out.println("test-button-unmatch");

        boolean unmatched = MatchingRequests.Unmatch( _id, match._id);
        if(unmatched){
            //progress: socket logic done, database logic missing
            matchesListView.getItems().remove(matchBox);
            WebSocket.websocketio.INSTANCE.socketIoInstance.emit("unmatchSocket", match._id, (Ack) args->{
                JSONObject data = (JSONObject) args[0];
                try {
                    System.out.println(data.getBoolean("acknowledged"));
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
            });
//            ChatZone.writer.println("UnmatchSocket| match:" + match._id);
            ChatZone.chatArea = null;
        }


        
});

// Spacer to push the button to the right
Button showProfile = new Button("Show Profile");
showProfile.setTextFill(Color.BLUEVIOLET);
showProfile.setStyle("-fx-background-color: transparent; -fx-border-color: blue;");
showProfile.setOnAction(e -> {
    MatchedProfilePage mpp = new MatchedProfilePage();
    try {
        mpp.showMatchedProfilePage(stage, _id, match._id);
    } catch (URISyntaxException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

});


Label messageCounter = new Label();
MessageCounterMap.put(match.getId(), messageCounter);
Region spacer = new Region();
HBox.setHgrow(spacer, Priority.ALWAYS);
if(ChatZone.messageCounters.get(match.getId()) != null){

    int msgCnt = ChatZone.messageCounters.get(match.getId());
    if(msgCnt > 0){
        messageCounter.setText("you have " + msgCnt + " messages unread");
    
    }
}
messageCounter.setTextFill(Color.BLUE);
    // Container for the match
    matchBox.setAlignment(Pos.CENTER_LEFT);
    matchBox.setPadding(new Insets(10));
    matchBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1px; -fx-border-radius: 5px;");
    matchBox.getChildren().addAll(imageOrLabel, matchDetails,messageCounter,spacer,statusMap.get(match.getId()), showProfile,unmatchButton); // Add the profile picture and match details
    matchBox.setOnMouseClicked((mouseEvent) -> {
        System.out.println(match.getId());
        if (chatZone == null)
            chatZone = new ChatZone();
        chatZone.showChatZone(stage, match,_id);
        ChatZone.inChatZoneScreen = true;
        
        
    });
    matchBoxMap.put(match.getId(), matchBox);
    return matchBox;
}

    // Mock data for matches (replace with real data from your app)
    private List<Match> getMatches(String _id, int index) {
       
            List<Match> matches = new ArrayList<>();
            
            List<User> matchesList = MatchingRequests.getMatches(Integer.toString(index) , _id);
            if (matchesList != null ){
                for (User elem : matchesList) {
                    matches.add(new Match(elem.get_id(),elem.getUsername(), elem.getProfilePicture()));
                    
                }
                
            }
          
        
      
   
        return matches;
    }

    // Inner class to represent a match
    public static class Match {
        private final String _id;
        private final String name;
        private final String profilePictureUrl; // Add this field
        // private final Label status = new Label();

        @Override
        public String toString() {
            return "Match{" +
                    "_id='" + _id + '\'' +
                    ", name='" + name + '\'' +
                    ", profilePictureUrl='" + profilePictureUrl + '\'' +
                    '}';
        }

        public Match(String _id, String name, String profilePictureUrl) {
            this._id = _id;
            this.name = name;
            this.profilePictureUrl = profilePictureUrl;
            
        }
    
        // public Label getStatus() {
        //     return status;
        // }

        public String getId() {
            return _id;
        }
        public String getName() {
            return name;
        }
    
    
    
        public String getProfilePictureUrl() {
            return profilePictureUrl;
        }
    }
    public static void cleanCurrentUnmatch(){

        WebSocket.websocketio.INSTANCE.socketIoInstance.off("Unmatched");
        WebSocket.websocketio.INSTANCE.socketIoInstance.on("Unmatched", args ->{
            JSONObject data = (JSONObject) args[0];
            String matchID = "";
            try {
                matchID = data.getString("socketMatchId");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            String finalMatchID = matchID;
            System.out.println("final match id");
            System.out.println(finalMatchID);
            Platform.runLater(()->{
                matchesListView.getItems().remove(matchBoxMap.get(finalMatchID));
            });
        });

    }
    public static void setUserStatus() {
        WebSocket.websocketio.INSTANCE.socketIoInstance.off("MatchesPageStatus");
        WebSocket.websocketio.INSTANCE.socketIoInstance.on("MatchesPageStatus", args -> {
            JSONObject data = (JSONObject) args[0];
            try {
                String matchId = data.getString("matchId");
                String status = data.getString("status");

                Platform.runLater(() -> {
                    // Now we are safe on the JavaFX Application Thread
                    if (statusMap != null && statusMap.containsKey(matchId)) {
                        Label label = statusMap.get(matchId);
                        label.setText(status);
                        label.setTextFill(status.equals("online") ? Color.GREEN : Color.RED);
                    } else {
                        // Create the label on the UI thread to avoid crashes
                        Label newLabel = new Label(status);
                        newLabel.setTextFill(Color.RED);
                        if (statusMap != null) {
                            statusMap.put(matchId, newLabel);
                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
    public static void showLastMessage(String usernameID, String content,String username){
        if (lastMessageMap != null && !lastMessageMap.isEmpty()) {   
              
            Platform.runLater(()->{
                lastMessageMap.get(usernameID).setText(username + ": " + content);
            });
        }
        if(ChatZone.messageCounters.get(usernameID) != null){

            int msgCnt = ChatZone.messageCounters.get(usernameID);
            Platform.runLater(()->{
                MessageCounterMap.get(usernameID).setText("you have " + msgCnt + " messages unread");
            });
            
        }

    }
    public static void pushUserMsgToTop(String id){
        Platform.runLater(()->{
            if (matchBoxMap.get(id) != null){

                matchesListView.getItems().remove(matchBoxMap.get(id));
                matchesListView.getItems().add(0, matchBoxMap.get(id));
            }
        });

    }
}
