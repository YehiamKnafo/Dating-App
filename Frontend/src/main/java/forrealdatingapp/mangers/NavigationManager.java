package forrealdatingapp.mangers;

import forrealdatingapp.App;
import forrealdatingapp.Scenes.LoginWindow;
import javafx.application.Platform;
import javafx.stage.Stage;

public class NavigationManager {
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    // Call this once in your start() method
    public static void setStage(Stage stage) {
        mainStage = stage;
    }

    public static void switchToLogin() {
        // Since the Interceptor runs on a background thread,
        // we MUST use Platform.runLater to touch the UI.
        Platform.runLater(() -> {
            try {
                // Load your login window
                App.clear();
                LoginWindow login = new LoginWindow();
                login.showLoginWindow(mainStage);
                System.out.println("Global Redirect: Sent user to Login Screen.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
