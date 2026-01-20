package forrealdatingapp.routes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.chatScenes.ChatZone;
import forrealdatingapp.dtos.Message;
import forrealdatingapp.dtos.UnreadCounter;
import static forrealdatingapp.routes.RouterUtils.*;
public class MessageRequests {
    public static List<Map<String, Object>> FetchMessages(String userId, String matchId) {
    try {
                Map<String, String> jsonMap = new HashMap<>(Map.of("matchID", matchId));
                String json = manageJSON().writeValueAsString(jsonMap);
                HttpClient client = HttpClient.newHttpClient();
                // System.out.println(jsonPath.toAbsolutePath());
                HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(getHost() + "messages/fetchmessages"))
                .header("Content-Type", "application/json")
                .header("x-api-key", manageToken().getToken(userId))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                // System.out.println("response code: "+ response.statusCode());
                // System.out.println("response Body: " + response.body());
                return manageJSON().readValue(response.body(), new TypeReference<List<Map<String, Object>>>(){});
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getLocalizedMessage());
                return null;
            }
    }
    public static List<Message> getLastMessages(String userId) {
        try {
                    // Create HttpClient
                    HttpClient client = HttpClient.newHttpClient();
                    // Build the GET request
                    HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(getHost() + "messages/latest-messages")) // Target URL
                    .header("x-api-key", manageToken().getToken(userId))
                    .GET() // GET method
                    .build();
                    // Send the request and handle the response
                    HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                    // Print response details
                    // System.out.println("Response code: " + response.statusCode());
                    // System.out.println("Response body getLastMessages: " + response.body());
                    return manageJSON().readValue(response.body(), new TypeReference<List<Message>>(){});
                    } catch (IOException | InterruptedException e) {
                        System.out.println(e.getLocalizedMessage());
                        return null;
                    }

    }
    public static void UpdateCounter(String id){
        try {
            String json = manageJSON().writeValueAsString(ChatZone.messageCounters);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "messages/update-counter"))
            .header("Content-Type", "application/json")
            .header("x-api-key", manageToken().getToken(id))
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build(); //PUT request
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
        
    }

    public static List<UnreadCounter> ShowUnreadMessages(String id) {
        try {
            // Create HttpClient
            HttpClient client = HttpClient.newHttpClient();
            // Build the GET request
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "messages/unreadmessages")) // Target URL
            .header("x-api-key", manageToken().getToken(id))
            .GET() // GET method
            .build();
            // Send the request and handle the response
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            // Print response details
            // System.out.println("Response code: " + response.statusCode());
            // System.out.println("Response body: " + response.body());
            return manageJSON().readValue(response.body(), new TypeReference<List<UnreadCounter>>() {});
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
            return null; 
        }
        
    }

    public static void ResetMessageCounter(String id, String matchId) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Add query parameters to the URL
           

            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "messages/resetCounter/" + matchId)) // URL with query params
            .method("PATCH", HttpRequest.BodyPublishers.noBody()) // No request body
            .header("x-api-key", manageToken().getToken(id))
            .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
}
