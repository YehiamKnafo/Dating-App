package forrealdatingapp.routes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.dtos.User;
import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
import static forrealdatingapp.routes.RouterUtils.manageToken;
public class UserProfileRequests {
    public static User getMyProfile(String _id) {
        HttpClient client = HttpClient.newHttpClient();

        // Create a HttpRequest (GET request)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getHost() + "profile"))
                .header("x-api-key", manageToken().getToken(_id)) // Example URL
                .build();

        try {
            // Send the GET request and receive the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            User user = manageJSON().readValue(response.body(), new TypeReference<User>() {
            });
            return user;
            //response status code and body
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }

    }
     public static void updateProfile(String json, String _id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "profile/updateprofile/" + _id))
            .header("Content-Type", "application/json")
            .header("x-api-key",manageToken().getToken(_id))
            .PUT(HttpRequest.BodyPublishers.ofString(json)).build(); //PUT request
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    public static void addPicture(String json, String _id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "profile/addpicture/" + _id))
            .header("Content-Type", "application/json")
            .header("x-api-key",manageToken().getToken(_id))
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build(); //PUT request
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    public static boolean updateProfilePicture(String _id, Map<String,String> jsonMap) {
        try {
                    String json = manageJSON().writeValueAsString(jsonMap);
                    System.out.println(json);
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest req = HttpRequest.newBuilder()
                    // other route take this route as a param
                    .uri(URI.create(getHost() + "profile/changeprofilepic"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key",manageToken().getToken(_id))
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build(); //PUT request
                    HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Response code: " + response.statusCode());
                    System.out.println("Response body: " + response.body());
                    return response.statusCode() == 200;
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                    return false;
                }
    }
    public static void UpdatePreferrences(User user,String id) {
        try {
            String json = manageJSON().writeValueAsString(user);
            System.out.println(json);
            HttpClient client = HttpClient.newHttpClient();

            // Add query parameters to the URL
           

            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "profile/updatePreferrences")) // URL with query params
            .method("PATCH", HttpRequest.BodyPublishers.ofString(json)) 
            .header("Content-Type", "application/json")
            .header("x-api-key", manageToken().getToken(id))
            .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    public static void UpdateBio(User bioChange, String _id) {
         try {
            String json = manageJSON().writeValueAsString(bioChange);
            // System.out.println(json);
            HttpClient client = HttpClient.newHttpClient();

            // Add query parameters to the URL
           

            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "profile/updateBio")) // URL with query params
            .method("PATCH", HttpRequest.BodyPublishers.ofString(json)) 
            .header("Content-Type", "application/json")
            .header("x-api-key", manageToken().getToken(_id))
            .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        


        }


}
