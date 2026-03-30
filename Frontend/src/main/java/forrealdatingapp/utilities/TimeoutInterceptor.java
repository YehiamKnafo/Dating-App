package forrealdatingapp.utilities;

import forrealdatingapp.App;
import forrealdatingapp.mangers.NavigationManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TimeoutInterceptor implements Interceptor {
    private final int maxRetries;
    private static final int INITIAL_MS = 1000; // Start with 1 second

    public TimeoutInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request =  chain.request();
        IOException lastException = null;

        for (int i = 0; i < maxRetries; i++) {
            // --- STEP 1: UI Feedback BEFORE the attempt ---
            final int attempt = i;
            Platform.runLater(() -> {
                App.serverStatusIndicator.setTextFill(Color.BLACK);
                App.serverStatusIndicator.setText("Connecting" + ".".repeat(attempt % 4));
            });

            try {
                // This is the blocking call.
                // If the server is asleep, it hangs here until it times out.
                Response response = chain.proceed(request);

                if (response.isSuccessful()) {
                    Platform.runLater(() -> App.serverStatusIndicator.setText(null));
                    return response;
                }

                if (response.code() >= 400 && response.code() < 500) {
                    handleClientError(response, request);
                    return response;
                }
                response.close();
            } catch (IOException e) {
                lastException = e;
            }

            // --- STEP 2: Smooth Waiting (Only runs if we need to retry) ---
            if (i < maxRetries - 1) {
                long totalWaitTime = INITIAL_MS * (long) Math.pow(2, i);
                long elapsed = 0;
                long interval = 500;
                System.out.println("Waiting " + totalWaitTime + "ms before next retry...");

                while (elapsed < totalWaitTime) {
                    int dotCount = (int) ((elapsed / interval) % 4);
                    // Use a simpler string format to avoid .indent() issues in UI labels
                    String baseMessage = (totalWaitTime >= 8000) ? "Server waking up" : "Retrying";
                    String finalMessage = baseMessage + ".".repeat(dotCount);

                    Platform.runLater(() -> App.serverStatusIndicator.setText(finalMessage));

                    try {
                        Thread.sleep(interval);
                        elapsed += interval;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", ie);
                    }
                }
            }
        }

        // Clear text if we totally give up
        Platform.runLater(() -> App.serverStatusIndicator.setText("Connection failed"));
        if (lastException != null) throw lastException;
        throw new IOException("Execution failed after " + maxRetries + " attempts.");
    }
    private void handleClientError(Response response, Request request) {
        if (response.code() == 401 && !request.url().toString().contains("/login")) {
            Platform.runLater(() -> {
                ButtonType backToLoginType = new ButtonType("Back to Login");
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "Session expired. Redirecting to login screen.",
                        backToLoginType
                );
                alert.setOnHidden(event -> NavigationManager.switchToLogin());
                alert.show();
            });
        }
    }
}