package forrealdatingapp.routes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
public class AuthRequests {

    public static boolean postSignup(String json) {
        try {
            // Create an HttpClient
            HttpClient client = HttpClient.newHttpClient();
            // Build the HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(getHost() + "auth"))
                    .POST(HttpRequest.BodyPublishers.ofString(json)) // Send JSON as body
                    .header("Content-Type", "application/json") // Set header
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the status code and response body
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            return response.statusCode() == 201;

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return false;

    }
public static String PostLogin(String json) {
      
        try {
            HttpClient client = HttpClient.newHttpClient();

            // System.out.println(jsonPath.toAbsolutePath());
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(getHost() + "auth/login"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json)).build();
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            // System.out.println("response code: " + response.statusCode());
            // System.out.println("response Body: " + response.body());
            return response.body();

        } catch (IOException | InterruptedException e) {
            return e.getLocalizedMessage();
        }

    }
    public static boolean verifyOtpRequest(String email, String otp) {
        try {
            URI uri = new URI(getHost() + "auth/verify-otp");
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = "{\"email\": \"" + email + "\", \"otp\": \"" + otp + "\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = connection.getResponseCode();
            if (code == 200) {
                System.out.println("OTP verified successfully");
                // Move to next part of your flow (e.g., user dashboard or profile creation)
                return true;
            } else {
                System.out.println("Invalid OTP");
                return false;
            }

        } catch (IOException | URISyntaxException e) {

            return false;
        }
    }

    public static String sendOtpRequest(String email,String type) {
        try {
            // Initialize ObjectMapper
            
            // Create the HTTP client
            HttpClient client = HttpClient.newHttpClient();

            // Define the request body
            Map<String, String> emailmap = new HashMap<>(Map.of("email", email,"type",type));
            String requestBody = manageJSON().writeValueAsString(emailmap);
            System.out.println(requestBody);

            // Create the POST request
            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "auth/send-otp"))
            .header("Content-Type", "application/json") // Set headers
            .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Set the request body
            .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the response
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace(); // Better debugging
            return "error"; // Return a non-null value
        }
    }
    public static String Resetusrpass(String passwordString, String email) {
    try {
        Map<String, Object>  jsonMap = new HashMap<>(Map.of("email", email,"password",passwordString));
        String json = manageJSON().writeValueAsString(jsonMap);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
        .uri(URI.create(getHost() + "auth/resetpassword" ))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(json))
        .build(); //PUT request
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
        if(response.statusCode() == 201) return "201|password reset successfuly";
        if (response.statusCode() == 404) return "404|user not exist in the database";
        if (response.statusCode() == 403) return "403|you tried to change to the same password";
        return null;
        


        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }

    
}
    
}
