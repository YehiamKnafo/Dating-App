package forrealdatingapp.routes;

import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
import static forrealdatingapp.routes.RouterUtils.manageToken;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.dtos.User;

public class MatchingRequests {
    public static Queue<User> getUsers() {
        // Create a HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // Create a HttpRequest (GET request)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getHost() + "matches"))
                .header("x-api-key", "example")
                .build();

        try {
            // Send the GET request and receive the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //response status code and body
            // System.out.println(response.body());
            Queue<User> users = manageJSON().readValue(response.body(), new TypeReference<Queue<User>>() {
            });
            System.out.println(users);
            return users;
        } catch (IOException | InterruptedException e) {
            return null;
        }

    }

    public static Queue<User> getUsers(String _id,String page) {
    // Start building the query string
    StringBuilder queryParams = new StringBuilder("?");
    
    if (page != null) queryParams.append("page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8));
    

    // Remove trailing "&" if exists
    if (queryParams.length() < 1) {
        queryParams.setLength(0); // No params provided, avoid sending "?"
    }
    System.out.println(queryParams);
    // Create the full URL
    String url = getHost() + "matches/querygetmatches" + queryParams;

    // Create HttpClient
    HttpClient client = HttpClient.newHttpClient();

    // Create HttpRequest (GET request)
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("x-api-key", manageToken().getToken(_id))
            .build();

    try {
        // Send the GET request and receive the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Debugging output
        System.out.println(response.body());

        // Parse JSON response into Queue<User>
        return manageJSON().readValue(response.body(), new TypeReference<Queue<User>>() {});
    } catch (IOException | InterruptedException e) {
        return null;
    }
}
public static void Dislike(String json, String _id) {
     
        try {
        HttpClient client = HttpClient.newHttpClient();
        // System.out.println(jsonPath.toAbsolutePath());
        HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(getHost() + "matches/dislike"))
        .header("Content-Type", "application/json")
        .header("x-api-key",manageToken().getToken(_id))
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("response code: "+ response.statusCode());
        System.out.println("response Body: " + response.body());
    } catch (IOException | InterruptedException e) {
        System.out.println(e.getLocalizedMessage());
    }

    }

    public static Map<String, Boolean> CheckMatch(String json,String _id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            // System.out.println(jsonPath.toAbsolutePath());
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "matches/checkmatch"))
            .header("Content-Type", "application/json")
            .header("x-api-key",manageToken().getToken(_id))
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("response code: "+ response.statusCode());
            System.out.println("response Body: " + response.body());
            return manageJSON().readValue(response.body(), new TypeReference<Map<String, Boolean>>() {});
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }

    public static void like(String json,String _id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            // System.out.println(jsonPath.toAbsolutePath());
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "matches/like"))
            .header("Content-Type", "application/json")
            .header("x-api-key",manageToken().getToken(_id))
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("response code: "+ response.statusCode());
            System.out.println("response Body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static List<User> getMatches(String page, String _id) {
        StringBuilder queryParams = new StringBuilder("?");
    
        if (page != null) queryParams.append("page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8)).append("&");
    
        // Remove trailing "&" if exists
        if (queryParams.length() > 1) {
            queryParams.setLength(queryParams.length() - 1);
        } else {
            queryParams.setLength(0); // No params provided, avoid sending "?"
        }
    
        try {
                    // Create HttpClient
                    HttpClient client = HttpClient.newHttpClient();
                    // Build the GET request
                    HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(getHost() + "matches/getmatches" + queryParams))
                    .header("x-api-key", manageToken().getToken(_id)) // Target URL
                    .GET() // GET method
                    .build();
                    // Send the request and handle the response
                    HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                    // Print response details
                    // System.out.println("Response code: " + response.statusCode());
                    // System.out.println("Response body: " + response.body());
                    return manageJSON().readValue(response.body(), new TypeReference<List<User>>(){});
                    } catch (IOException | InterruptedException e) {
                        System.out.println(e.getLocalizedMessage());
                        return null;
                    }
    }

  

    public static boolean Unmatch(String _id, String _matchId) {
        Map<String, String> reqBodyMap = new HashMap<>(Map.of("_matchId", _matchId));
        try {
                String reqBody = manageJSON().writeValueAsString(reqBodyMap);
                HttpClient client = HttpClient.newHttpClient();
                // System.out.println(jsonPath.toAbsolutePath());
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(getHost() + "matches/unmatch"))
                .header("Content-Type", "application/json")
                .header("x-api-key",manageToken().getToken(_id))
                .POST(HttpRequest.BodyPublishers.ofString(reqBody)).build();
                HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                System.out.println("response code: "+ response.statusCode());
                System.out.println("response Body: " + response.body());
                Map<String, Object> resbody = manageJSON().readValue(response.body(), new TypeReference<Map<String, Object>>(){});
                if (resbody.containsKey("approved"))
                    return (boolean)resbody.get("approved");    
                return false;    
            } catch (IOException | InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                    return false;
                }
    }
public static User getMatchedProfile(String _id, Map<String, Object> jsonMap){
        try {
                    String json = manageJSON().writeValueAsString(jsonMap);
                    HttpClient client = HttpClient.newHttpClient();
                    // System.out.println(jsonPath.toAbsolutePath());
                    HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(getHost() + "matches/getMatchedProfile"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key", manageToken().getToken(_id))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
                    HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                    System.out.println("response code: "+ response.statusCode());
                    System.out.println("response Body: " + response.body());
                    return manageJSON().readValue(response.body(), User.class);
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                    return null;
                }
        
        
    }

    
}
