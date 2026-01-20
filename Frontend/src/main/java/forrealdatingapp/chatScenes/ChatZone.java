package forrealdatingapp.chatScenes;

import static forrealdatingapp.routes.RouterUtils.getTCPHost;
import static forrealdatingapp.routes.RouterUtils.getTCPPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forrealdatingapp.Scenes.LoginWindow;
import forrealdatingapp.Scenes.MainPage;
import forrealdatingapp.Scenes.MatchesPage;
import forrealdatingapp.dtos.User;
import forrealdatingapp.routes.MessageRequests;
import forrealdatingapp.routes.UserProfileRequests;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ChatZone {

    public static TextArea chatArea; // Displays messages
    private static TextField messageInput; // User input
    private HBox inputBox;
    private static Text alertMessage;
        private Button sendButton; // Send button
        public static Socket socket;
        public static BufferedReader reader;
        public static PrintWriter writer;
        private static String userId;
        public static String matchId;
        public static boolean inChatZoneScreen = false;
        private static  String finalMessage;
        private static  String finalstr;
        public static Thread listenerThread;
        public static Map<String, Integer> messageCounters = new HashMap<>();
        public static Map <String, Boolean> isMessagesFetched = new HashMap<>();
        public static Map <String, TextArea> MessagesMap = new HashMap<>();
        // public static Map <String, Text> alertMessagesMap = new HashMap<>();
        private static PauseTransition typingPause;
        private static boolean isTyping = false;
        private final static Duration TYPING_DELAY = Duration.seconds(1); // 1 second pause
                                
                                    
        public void showChatZone(Stage stage, MatchesPage.Match match, String _userId) {
            
            messageCounters.put(matchId, 0);
            matchId = match.getId();
            userId = _userId;
            // messageCounters.get(matchId) 
            
            // UI Components
            
            chatArea = new TextArea();
            chatArea.setEditable(false);
            chatArea.setWrapText(true);
            
            if(isMessagesFetched.get(matchId) == null){
                // System.out.println(userId);
                // System.out.println(matchId);
                List<Map<String, Object>> messages = MessageRequests.FetchMessages(userId, matchId);
                if(messages != null){
                    User currentUser = UserProfileRequests.getMyProfile(_userId);
                    StringBuilder chatContent = new StringBuilder();
                    String sender;
                    for (Map<String, Object> message : messages) {
                        String content = (String)message.get("senderUsername");
                        if(content.equals(currentUser.getUsername())){
                            sender = "Me";
                        }
                        else{
                            sender = content;
                        }
        
                        chatContent.append(sender).append("|").append((String)message.get("message")).append("\n");
                    }
                    chatArea.setText(chatContent.toString());
                    isMessagesFetched.put(matchId, true);
        
                }
            
    
    
            }
            else{
                chatArea = MessagesMap.get(matchId);
    
            }
    
    
            messageInput = new TextField();
            setupTypingDetection();
    
            sendButton = new Button("Send");
            // messageInput.setOnKeyPressed((event) ->{
            //     System.out.println("textfield");
            //     writer.println("Typing|" + matchId);
            // } );
            // Handle sending messages
            sendButton.setOnAction(e -> {
                try {
                    sendMessage();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            });
    
       
            alertMessage = new Text();
            // alertMessagesMap.put(match.getId(), alertMessage);//map know how to handle initializition of different object and help manage them
            
            alertMessage.setText(null);
            // Layout
            inputBox = new HBox(10, messageInput, sendButton, alertMessage);
            inputBox.setPadding(new Insets(10));
    
            Button backToMainScreen = new Button("back to main screen");
            backToMainScreen.setOnAction((actionEvent) -> {
                MessageRequests.ResetMessageCounter(_userId, match.getId());
                MessagesMap.put(matchId, chatArea);
                inChatZoneScreen = false;
                messageCounters.put(matchId, 0);
                MatchesPage.MessageCounterMap.get(match.getId()).setText(null);
                MainPage mp = new MainPage();
                mp.showMainPage(stage, _userId);
    
                
            });
            Label whoamichattingwith = new Label("you are now chatting with " + match.getName());
            HBox Top = new HBox(10);
            Top.getChildren().addAll(backToMainScreen,whoamichattingwith, MatchesPage.statusMap.get(matchId));
            BorderPane layout = new BorderPane();
            layout.setCenter(chatArea);
            layout.setBottom(inputBox);
            layout.setTop(Top);
            Scene scene = new Scene(layout, 400, 500);
            scene.setOnKeyPressed ((keyEvent) -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    try {
                        sendMessage();
                    } catch (InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
            }
            } );
            stage.setScene(scene);
            stage.setTitle("Chat");
            stage.show();
    
            // Connect to server
            
        }
    
        public static void connectToServer() {
            String socket_host = getTCPHost();
            int socket_port = getTCPPort();
            try {
                if (socket == null ||socket.isClosed()) {
                    
                    socket = new Socket(socket_host, socket_port); // Replace with actual server details
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    
                }
                // Start a thread to listen for incoming messages
                    
                    if (listenerThread == null || !listenerThread.isAlive()) {
                    listenerThread = new Thread(ChatZone::listenForMessages);
                    listenerThread.setDaemon(true); // Optional: ensures it stops when the app closes
                    listenerThread.start();
                }
                // Send initial message to identify user
    
                System.out.println("thread test: " + listenerThread);
            } catch (IOException e) {
                e.printStackTrace();
                chatArea.appendText("Failed to connect to chat server.\n");
            }
        }
    
        private static void listenForMessages() {
            System.out.println("new thread");
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    finalMessage = message; // Effectively final
                    System.out.println(finalMessage);
                    handleIncomingTypingNotification(message);
                    if (finalMessage.contains("MatchesPageStatus|")){
                        String id = finalMessage.split("\\|")[1].trim();
                        String type = finalMessage.split("\\|")[2].trim();
                        // System.out.println(id + "\n" + type);
                        MatchesPage.setUserStatus(id, type);
                    }
                    if (finalMessage.equals("Unallowed")) {
                        System.out.println("test-unallowed");
                        LoginWindow.SocketLogin("Unallowed");
                        
                        
                    }
                    else if(finalMessage.contains("Allowed|")){
                        LoginWindow.SocketLogin("allowed");
    
                    }
                    if (finalMessage.contains("Unmatched|")){
                        String userid = finalMessage.split("userid:")[1].trim();
                        MatchesPage.cleanCurrentUnmatch(userid);
                    }
                    if (finalMessage.contains("MessageRecieved|")){
                        String username = finalMessage.split("\\|")[1];
                        String content = finalMessage.split("\\|")[2];
                        String usernameID = finalMessage.split("\\|")[3];
                        Platform.runLater(()->{
                            chatArea.appendText(username + "|" + content + "\n");
    
                        });
                        System.out.println("inChatZoneScreen:"+inChatZoneScreen);
                        if(!inChatZoneScreen){
                            messageCounters.put(usernameID, messageCounters.getOrDefault(usernameID, 0) + 1);
                            MatchesPage.pushUserMsgToTop(usernameID);
                        }
                        else messageCounters.remove(usernameID);
                        MatchesPage.showLastMessage(usernameID, content,username);
                        // if(messageCounters.get(usernameID) == null){
                        //     messageCounters.put(usernameID, 0);
    
                        // }
                        // else{
                        //     messageCounters.put(usernameID, messageCounters.get(usernameID) + 1);
                        // }
                    }
    
            }
            System.out.println("end-while-test");
        }
        catch (IOException e) {
            closeConnection();
        }
    }
    
        private void sendMessage() throws InterruptedException {
            String message = messageInput.getText().trim();
            chatArea.appendText("Me|" + message + "\n");
            if(MatchesPage.lastMessageMap.get(matchId) != null){
                MatchesPage.lastMessageMap.get(matchId).setText("me: " + message);
            }
            messageInput.clear();
            if (!message.isEmpty()) {
                
                writer.println("MESSAGE| input: "+ message + "| matchid:" + matchId);
                
    
            }
        }
        
        
                                
        private static void setupTypingDetection() {
                typingPause = new PauseTransition(TYPING_DELAY);
                typingPause.setOnFinished(e -> {
                if (isTyping) {
                    isTyping = false;
                    writer.println("StoppedTyping|" + matchId + "|" + userId);
                }
                });
            
                
                messageInput.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (!isTyping && !newVal.isEmpty()) {
                        // User started typing
                        isTyping = true;
                        writer.println("Typing|" + matchId + "|" + userId);
                    }
            
                    // Reset the timer on each keystroke
                    typingPause.playFromStart();
            
                    if (newVal.isEmpty()) {
                    // Field cleared - stopped typing
                        isTyping = false;
                        writer.println("StoppedTyping|" + matchId + "|" + userId);
                        typingPause.stop();
                    }
                });
           
        }
        public static void handleIncomingTypingNotification(String message) {
                if (message.contains("Typing") && !message.contains("MessageRecieved|")) {

                    
                    String[] parts = message.split("\\|");
                    String type = parts[0];
                    String senderName = parts[1];
                    String senderID = parts[2];
                    if (matchId != null && matchId.equals(senderID)) {
                        
                        Platform.runLater(() -> {
                            if (type.equals("Typing")) {
                                alertMessage.setText(senderName + " is typing...");
                                alertMessage.setFill(Color.GRAY);
                              
                            } else if (type.equals("StoppedTyping")) {
                                alertMessage.setText(""); // Clear the indicator
                            
                            }
                        });
                    }
                    else{
                        Platform.runLater(()->{
                            if (alertMessage !=null) {
                                alertMessage.setText(""); // Clear the indicator
                                
                            }
                        });
                    }
                    if (!inChatZoneScreen && type.equals("Typing")) {
                        Platform.runLater(()-> MatchesPage.lastMessageMap.get(senderID).setText(senderName + " is typing..."));
                        
                        
                    }
                    else{
                        Platform.runLater(()-> MatchesPage.lastMessageMap.get(senderID).setText(""));
                        
                    }

                }
        
    }

    public static void closeConnection() {
        try {
            if (writer != null) writer.println("DISCONNECT");
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
