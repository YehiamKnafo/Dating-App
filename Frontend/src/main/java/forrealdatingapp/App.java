package forrealdatingapp;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import forrealdatingapp.Scenes.LoginWindow;
import forrealdatingapp.Scenes.MatchesPage;
import forrealdatingapp.dtos.User;
import forrealdatingapp.mangers.NavigationManager;
import forrealdatingapp.mangers.TokenManager;
import forrealdatingapp.mangers.UnloggedUserManager;
import forrealdatingapp.routes.AuthRequests;
import forrealdatingapp.routes.CredentialsRequests;
import forrealdatingapp.signUpScenes.UserDetails;
import forrealdatingapp.utilities.CloudinaryUtils;
import forrealdatingapp.utilities.RouterUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static forrealdatingapp.utilities.RouterUtils.manageToken;

public class App extends Application{
    private static final Logger log = LoggerFactory.getLogger(App.class);
    public static LoginWindow loginWindow;
    public static MatchesPage matchesPage;
    public static boolean isTokenOnline;
    public static String id;
    public static ProgressIndicator progressIndicator;
    public static Label serverStatusIndicator;

    @Override
    public void start(Stage primaryStage)  throws IOException {
        //TEST
        loginWindow = new LoginWindow();
        matchesPage = new MatchesPage();
        isTokenOnline = false;
        serverStatusIndicator = new Label();
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.showLoadingScreen(primaryStage);
        Task<Boolean> loginTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {

                return runCredentialsRetrieveOnce();
            }
        };
        loginTask.setOnSucceeded(e->{
            boolean res = loginTask.getValue();
            if (res){
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/datingappicon.png")));
                NavigationManager.setStage(primaryStage);
//
                primaryStage.getIcons().add(icon);
                try {
                    loginWindow.showLoginWindow(primaryStage);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.setOnCloseRequest(event->{
                    Platform.exit();
                });

            }
        });

        new Thread(loginTask).start();
        //END TEST

        //PROD
//        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/datingappicon.png")));
//        NavigationManager.setStage(primaryStage);
//
//        primaryStage.getIcons().add(icon);
//        loginWindow.showLoginWindow(primaryStage);
//        primaryStage.setOnCloseRequest(event->{
//            Platform.exit();
//
//        });
        //END PROD



    }
    @Override
    public void stop() {
        clear();
        System.exit(0);


    }

    public static void clear() {
        if (WebSocket.websocketio.INSTANCE.socketIoInstance != null){
            WebSocket.websocketio.INSTANCE.socketIoInstance.disconnect();

        }
        if (UnloggedUserManager.getUser() != null && UnloggedUserManager.getUser().getEmail() != null){

            boolean ok = AuthRequests.dropOtp(UnloggedUserManager.getUser().getEmail());
            if (ok) System.out.println("otp dropped");
        }
        UnloggedUserManager.setUser(null);
        deleteAllSignUpPics();
        System.out.println("Window closing, disconnecting socket...");
        try {
            manageToken().clearToken(App.id);
        } catch (Exception e) {
            System.out.println("No token was found");

        }
//        TokenManager tokenManager = new TokenManager();
//        if (tokenManager.getToken(id) != null){
//            tokenManager.clearToken(id);  // Clear the token when the app stops
//
//        }
//        else {
//            System.out.println("No token was found");
//        }

        App.id = null;
    }

    public static <T extends Pane> void BackToLoginBtn(T div, Stage stage){
        Button backButton = new Button("back to login/sign up screen");
        backButton.setOnAction((actionEvent) -> {
            try {
                clear();
                loginWindow.showLoginWindow(stage);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        });
        div.getChildren().add(backButton);

    }
    public static <T extends Pane> void BackToLoginBtn(T div, Stage stage, User user){
        BackToLoginBtn(div, stage);
        if (stage.getTitle().equals("Reset Password")){
            user.setEmail(null);
        }
        div.getChildren().stream()
                .filter(node -> node instanceof Button)
                .map(node -> (Button) node)
                .filter(btn -> btn.getText().toLowerCase().contains("back to login"))
                .findFirst().ifPresent(backBtn -> backBtn.setOnAction(e -> {
                    System.out.println(backBtn);
                    clear();
                    try {
                        loginWindow.showLoginWindow(stage);
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }

                }));


    }
    public static VBox skipEmailScene(Stage stage){
        VBox layout = new VBox(14);
        Text txt = new Text("Skipping this step will not allow you to reset your password in the future.");
        Text txt2 = new Text("Account deletion will always be available in settings");
        Text txt3 = new Text("Im using free api for email so its not guarntee to work everytime");
        Text txt4 = new Text("Tested only on google domains. might not work when using other mail services");
        txt.setFill(Color.YELLOW);
        txt2.setFill(Color.YELLOW);
        txt3.setFill(Color.YELLOW);
        txt4.setFill(Color.YELLOW);

        Button skipBtn;
        skipBtn = new Button("Skip");
        skipBtn.setOnAction((actionEvent)->{
            UserDetails userDetails = new UserDetails();
            User newUser = new User();
            UnloggedUserManager.setUser(newUser);
            userDetails.showUserDetails(stage);
        });


        layout.setStyle("-fx-background-color: #333333; -fx-padding: 10px;");
        layout.getChildren().addAll(txt,txt2,txt3,txt4, skipBtn);
        return layout;
    }
    public static void main(String[] args) throws Exception {

        launch(args);

    }
    public static void  deleteAllSignUpPics(){
        if(UserDetails.uploadedPictures != null && !UserDetails.uploadedPictures.isEmpty()){
            UserDetails.uploadedPictures.forEach(CloudinaryUtils::deleteFromCloudinaryByUrl);
            System.out.println("Sign up pics cleared successfully");
        }

    }
    private static Supplier<Boolean> oneTimeLogin = () -> {
        System.out.println("Executing sensitive login...");
        boolean ok = false;
        try {
            ok = CredentialsRequests.credentialsRetrieve();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return ok;

    };

    public static boolean runCredentialsRetrieveOnce() {
        if (oneTimeLogin != null) {
            boolean res = oneTimeLogin.get();
            // KILL THE METHOD RIGHT AWAY
            oneTimeLogin = null;
//            System.out.println("Method has been destroyed.");
            return res;
        }
        return false;
    }
    public static void kicked(){
        WebSocket.websocketio.INSTANCE.socketIoInstance.off("kicked");
        WebSocket.websocketio.INSTANCE.socketIoInstance.on("kicked", (args)->{

            clear();
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.WARNING,"Someone else was trying to login to your account");
                alert.show();
                try {
                    loginWindow.showLoginWindow(NavigationManager.getMainStage());

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

        });

    }
}
        // // delete later
        // TokenManager tokenManager = new TokenManager();
        // tokenManager.clearToken();
//    public static String getEnv(String key) {
//        // Check system environment first
//        String value = System.getenv(key);
//        if (value != null) {
//            return value;
//        }
//
//        // Fallback to .env file only if needed
//        try {
//            Dotenv dotenv = Dotenv.load();
//            return dotenv.get(key);
//        } catch (DotenvException e) {
//            throw new RuntimeException("Missing environment variable: " + key +
//                                " (not found in system env or .env file)", e);
//        }
//    }
//        primaryStage.setOnCloseRequest(event->{
//            System.out.println("Window closing, disconnecting socket...");
//            TokenManager tokenManager = new TokenManager();
//            if (token_id != null && !isTokenOnline){
//                tokenManager.clearToken(token_id);  // Clear the token when the app stops
//
//            }
//
//            if (WebSocket.websocketio.INSTANCE.socketIoInstance != null)
//                WebSocket.websocketio.INSTANCE.socketIoInstance.disconnect();
//        });
