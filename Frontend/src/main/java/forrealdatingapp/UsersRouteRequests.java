// package forrealdatingapp;

// import java.io.IOException;
// import java.io.OutputStream;
// import java.net.HttpURLConnection;
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.net.URL;
// import java.net.URLEncoder;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.nio.charset.StandardCharsets;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Queue;

// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import forrealdatingapp.chatScenes.ChatZone;
// import forrealdatingapp.dtos.Message;
// import forrealdatingapp.dtos.UnreadCounter;
// import forrealdatingapp.dtos.User;


// public class UsersRouteRequests {

//     /*i need to study completeable future
//      * and then make the request method as post and asking the backend to give me a json of the users
//      * so i can then put it in a user class or deserialize on the spot to whatever i want to
//      * 
//      * Serialize a class object to JSON
//        String json = objectMapper.writeValueAsString(user);
//      * 
//      */
    
//     private static final String HOST = App.getEnv("EXPRESS_HOST");
//     private static final ObjectMapper om = new ObjectMapper();
//     public static final TokenManager tm = new TokenManager();

//     public static User getMyProfile(String _id) {
//         HttpClient client = HttpClient.newHttpClient();

//         // Create a HttpRequest (GET request)
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(HOST + "users/myProfile"))
//                 .header("x-api-key", tm.getToken(_id)) // Example URL
//                 .build();

//         try {
//             // Send the GET request and receive the response
//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//             User user = om.readValue(response.body(), new TypeReference<User>() {
//             });
//             return user;
//             //response status code and body
//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//             return null;
//         }

//     }

//     public static Queue<User> getUsers() {
//         // Create a HttpClient
//         HttpClient client = HttpClient.newHttpClient();

//         // Create a HttpRequest (GET request)
//         HttpRequest request = HttpRequest.newBuilder()
//                 .uri(URI.create(HOST + "users"))
//                 .header("x-api-key", "example")
//                 .build();

//         try {
//             // Send the GET request and receive the response
//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             //response status code and body
//             // System.out.println(response.body());
//             Queue<User> users = om.readValue(response.body(), new TypeReference<Queue<User>>() {
//             });
//             System.out.println(users);
//             return users;
//         } catch (IOException | InterruptedException e) {
//             return null;
//         }

//     }

//     public static Queue<User> getUsers(String _id,String page) {
//     // Start building the query string
//     StringBuilder queryParams = new StringBuilder("?");
    
//     if (page != null) queryParams.append("page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8));
    

//     // Remove trailing "&" if exists
//     if (queryParams.length() < 1) {
//         queryParams.setLength(0); // No params provided, avoid sending "?"
//     }
//     System.out.println(queryParams);
//     // Create the full URL
//     String url = HOST + "users/queryget" + queryParams;

//     // Create HttpClient
//     HttpClient client = HttpClient.newHttpClient();

//     // Create HttpRequest (GET request)
//     HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(url))
//             .header("x-api-key", tm.getToken(_id))
//             .build();

//     try {
//         // Send the GET request and receive the response
//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         // Debugging output
//         System.out.println(response.body());

//         // Parse JSON response into Queue<User>
//         return om.readValue(response.body(), new TypeReference<Queue<User>>() {});
//     } catch (IOException | InterruptedException e) {
//         return null;
//     }
// }


//     public static boolean postSignup(String json) {
//         try {
//             // Create an HttpClient
//             HttpClient client = HttpClient.newHttpClient();
//             // Build the HttpRequest
//             HttpRequest request = HttpRequest.newBuilder()
//                     .uri(URI.create(HOST + "users/signup"))
//                     .POST(HttpRequest.BodyPublishers.ofString(json)) // Send JSON as body
//                     .header("Content-Type", "application/json") // Set header
//                     .build();

//             // Send the request and get the response
//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             // Print the status code and response body
//             System.out.println("Status Code: " + response.statusCode());
//             System.out.println("Response Body: " + response.body());
//             return response.statusCode() == 201;

//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//         }
//         return false;

//     }

//     public static String PostLogin(String json) {
      
//         try {
//             HttpClient client = HttpClient.newHttpClient();

//             // System.out.println(jsonPath.toAbsolutePath());
//             HttpRequest req = HttpRequest.newBuilder().uri(URI.create(HOST + "users/login"))
//             .header("Content-Type", "application/json")
//             .POST(HttpRequest.BodyPublishers.ofString(json)).build();
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             // System.out.println("response code: " + response.statusCode());
//             // System.out.println("response Body: " + response.body());
//             return response.body();

//         } catch (IOException | InterruptedException e) {
//             return e.getLocalizedMessage();
//         }

//     }

//     public static boolean verifyOtpRequest(String email, String otp) {
//         try {
//             URI uri = new URI(HOST + "otp/verify-otp");
//             URL url = uri.toURL();
//             HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//             connection.setRequestMethod("POST");
//             connection.setRequestProperty("Content-Type", "application/json");
//             connection.setDoOutput(true);

//             String jsonInputString = "{\"email\": \"" + email + "\", \"otp\": \"" + otp + "\"}";

//             try (OutputStream os = connection.getOutputStream()) {
//                 byte[] input = jsonInputString.getBytes("utf-8");
//                 os.write(input, 0, input.length);
//             }

//             int code = connection.getResponseCode();
//             if (code == 200) {
//                 System.out.println("OTP verified successfully");
//                 // Move to next part of your flow (e.g., user dashboard or profile creation)
//                 return true;
//             } else {
//                 System.out.println("Invalid OTP");
//                 return false;
//             }

//         } catch (IOException | URISyntaxException e) {

//             return false;
//         }
//     }

//     public static String sendOtpRequest(String email,String type) {
//         try {
//             // Initialize ObjectMapper
            
//             // Create the HTTP client
//             HttpClient client = HttpClient.newHttpClient();

//             // Define the request body
//             Map<String, String> emailmap = new HashMap<>(Map.of("email", email,"type",type));
//             String requestBody = om.writeValueAsString(emailmap);
//             System.out.println(requestBody);

//             // Create the POST request
//             HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "otp/send-otp"))
//             .header("Content-Type", "application/json") // Set headers
//             .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Set the request body
//             .build();

//             // Send the request and get the response
//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             // Return the response
//             return response.body();
//         } catch (IOException | InterruptedException e) {
//             e.printStackTrace(); // Better debugging
//             return "error"; // Return a non-null value
//         }
//     }
//     // public static void main(String[] args) {
//     //     System.out.println(HOST);
//     // }

//     public static void updateProfile(String json, String _id) {

//         try {
//             HttpClient client = HttpClient.newHttpClient();
//             HttpRequest req = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/updateprofile/" + _id))
//             .header("Content-Type", "application/json")
//             .header("x-api-key",tm.getToken(_id))
//             .PUT(HttpRequest.BodyPublishers.ofString(json)).build(); //PUT request
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             System.out.println("Response code: " + response.statusCode());
//             System.out.println("Response body: " + response.body());
//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//         }

//     }

//     public static void addPicture(String json, String _id) {
//         try {
//             HttpClient client = HttpClient.newHttpClient();
//             HttpRequest req = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/addpicture/" + _id))
//             .header("Content-Type", "application/json")
//             .header("x-api-key",tm.getToken(_id))
//             .PUT(HttpRequest.BodyPublishers.ofString(json))
//             .build(); //PUT request
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             System.out.println("Response code: " + response.statusCode());
//             System.out.println("Response body: " + response.body());
//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//         }
//     }

//     public static void Dislike(String json, String _id) {
     
//         try {
//         HttpClient client = HttpClient.newHttpClient();
//         // System.out.println(jsonPath.toAbsolutePath());
//         HttpRequest req = HttpRequest.newBuilder()
//         .uri(URI.create(HOST + "users/dislike"))
//         .header("Content-Type", "application/json")
//         .header("x-api-key",tm.getToken(_id))
//         .POST(HttpRequest.BodyPublishers.ofString(json))
//         .build();
//         HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//         System.out.println("response code: "+ response.statusCode());
//         System.out.println("response Body: " + response.body());
//     } catch (IOException | InterruptedException e) {
//         System.out.println(e.getLocalizedMessage());
//     }

//     }

//     public static Map<String, Boolean> CheckMatch(String json,String _id) {
//         try {
//             HttpClient client = HttpClient.newHttpClient();
//             // System.out.println(jsonPath.toAbsolutePath());
//             HttpRequest req = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/checkmatch"))
//             .header("Content-Type", "application/json")
//             .header("x-api-key",tm.getToken(_id))
//             .POST(HttpRequest.BodyPublishers.ofString(json))
//             .build();
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             System.out.println("response code: "+ response.statusCode());
//             System.out.println("response Body: " + response.body());
//             return om.readValue(response.body(), new TypeReference<Map<String, Boolean>>() {});
//         } catch (IOException | InterruptedException e) {
//             return null;
//         }
//     }

//     public static void like(String json,String _id) {
//         try {
//             HttpClient client = HttpClient.newHttpClient();
//             // System.out.println(jsonPath.toAbsolutePath());
//             HttpRequest req = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/like"))
//             .header("Content-Type", "application/json")
//             .header("x-api-key",tm.getToken(_id))
//             .POST(HttpRequest.BodyPublishers.ofString(json))
//             .build();
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             System.out.println("response code: "+ response.statusCode());
//             System.out.println("response Body: " + response.body());
//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//         }
//     }

//     public static List<User> getMatches(String page, String _id) {
//         StringBuilder queryParams = new StringBuilder("?");
    
//         if (page != null) queryParams.append("page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8)).append("&");
    
//         // Remove trailing "&" if exists
//         if (queryParams.length() > 1) {
//             queryParams.setLength(queryParams.length() - 1);
//         } else {
//             queryParams.setLength(0); // No params provided, avoid sending "?"
//         }
    
//         try {
//                     // Create HttpClient
//                     HttpClient client = HttpClient.newHttpClient();
//                     // Build the GET request
//                     HttpRequest req = HttpRequest.newBuilder()
//                     .uri(URI.create(HOST + "users/getmatches" + queryParams))
//                     .header("x-api-key", tm.getToken(_id)) // Target URL
//                     .GET() // GET method
//                     .build();
//                     // Send the request and handle the response
//                     HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//                     // Print response details
//                     // System.out.println("Response code: " + response.statusCode());
//                     // System.out.println("Response body: " + response.body());
//                     return om.readValue(response.body(), new TypeReference<List<User>>(){});
//                     } catch (IOException | InterruptedException e) {
//                         System.out.println(e.getLocalizedMessage());
//                         return null;
//                     }
//     }

  

//     public static boolean Unmatch(String _id, String _matchId) {
//         Map<String, String> reqBodyMap = new HashMap<>(Map.of("_matchId", _matchId));
//         try {
//                 String reqBody = om.writeValueAsString(reqBodyMap);
//                 HttpClient client = HttpClient.newHttpClient();
//                 // System.out.println(jsonPath.toAbsolutePath());
//                 HttpRequest req = HttpRequest.newBuilder().uri(URI.create(HOST + "users/unmatch"))
//                 .header("Content-Type", "application/json")
//                 .header("x-api-key",tm.getToken(_id))
//                 .POST(HttpRequest.BodyPublishers.ofString(reqBody)).build();
//                 HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//                 System.out.println("response code: "+ response.statusCode());
//                 System.out.println("response Body: " + response.body());
//                 Map<String, Object> resbody = om.readValue(response.body(), new TypeReference<Map<String, Object>>(){});
//                 if (resbody.containsKey("approved"))
//                     return (boolean)resbody.get("approved");    
//                 return false;    
//             } catch (IOException | InterruptedException e) {
//                     System.out.println(e.getLocalizedMessage());
//                     return false;
//                 }
//     }

//     public static boolean updateProfilePicture(String _id, Map<String,String> jsonMap) {
//         try {
//                     String json = om.writeValueAsString(jsonMap);
//                     System.out.println(json);
//                     HttpClient client = HttpClient.newHttpClient();
//                     HttpRequest req = HttpRequest.newBuilder()
//                     // other route take this route as a param
//                     .uri(URI.create(HOST + "users/changeprofilepic"))
//                     .header("Content-Type", "application/json")
//                     .header("x-api-key",tm.getToken(_id))
//                     .PUT(HttpRequest.BodyPublishers.ofString(json))
//                     .build(); //PUT request
//                     HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//                     System.out.println("Response code: " + response.statusCode());
//                     System.out.println("Response body: " + response.body());
//                     return response.statusCode() == 200;
//                 } catch (IOException | InterruptedException e) {
//                     System.out.println(e.getLocalizedMessage());
//                     return false;
//                 }
//     }
//     public static User getMatchedProfile(String _id, Map<String, Object> jsonMap){
//         try {
//                     String json = om.writeValueAsString(jsonMap);
//                     HttpClient client = HttpClient.newHttpClient();
//                     // System.out.println(jsonPath.toAbsolutePath());
//                     HttpRequest req = HttpRequest.newBuilder()
//                     .uri(URI.create(HOST + "users/getMatchedProfile"))
//                     .header("Content-Type", "application/json")
//                     .header("x-api-key", tm.getToken(_id))
//                     .POST(HttpRequest.BodyPublishers.ofString(json))
//                     .build();
//                     HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//                     System.out.println("response code: "+ response.statusCode());
//                     System.out.println("response Body: " + response.body());
//                     return om.readValue(response.body(), User.class);
//                 } catch (IOException | InterruptedException e) {
//                     System.out.println(e.getLocalizedMessage());
//                     return null;
//                 }
        
        
//     }

// public static List<Map<String, Object>> FetchMessages(String userId, String matchId) {
//     try {
//                 Map<String, String> jsonMap = new HashMap<>(Map.of("matchID", matchId));
//                 String json = om.writeValueAsString(jsonMap);
//                 HttpClient client = HttpClient.newHttpClient();
//                 // System.out.println(jsonPath.toAbsolutePath());
//                 HttpRequest req = HttpRequest.newBuilder()
//                 .uri(URI.create(HOST + "users/fetchmessages"))
//                 .header("Content-Type", "application/json")
//                 .header("x-api-key", tm.getToken(userId))
//                 .POST(HttpRequest.BodyPublishers.ofString(json))
//                 .build();
//                 HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//                 // System.out.println("response code: "+ response.statusCode());
//                 // System.out.println("response Body: " + response.body());
//                 return om.readValue(response.body(), new TypeReference<List<Map<String, Object>>>(){});
//             } catch (IOException | InterruptedException e) {
//                 System.out.println(e.getLocalizedMessage());
//                 return null;
//             }
//     }

// public static String Resetusrpass(String passwordString, String email) {
//     try {
//         Map<String, Object>  jsonMap = new HashMap<>(Map.of("email", email,"password",passwordString));
//         String json = om.writeValueAsString(jsonMap);
//         HttpClient client = HttpClient.newHttpClient();
//         HttpRequest req = HttpRequest.newBuilder()
//         .uri(URI.create(HOST + "users/resetpassword" ))
//         .header("Content-Type", "application/json")
//         .PUT(HttpRequest.BodyPublishers.ofString(json))
//         .build(); //PUT request
//         HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//         System.out.println("Response code: " + response.statusCode());
//         System.out.println("Response body: " + response.body());
//         if(response.statusCode() == 201) return "201|password reset successfuly";
//         if (response.statusCode() == 404) return "404|user not exist in the database";
//         if (response.statusCode() == 403) return "403|you tried to change to the same password";
//         return null;
        


//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//             return null;
//         }

    
// }

//     public static List<Message> getLastMessages(String userId) {
//         try {
//                     // Create HttpClient
//                     HttpClient client = HttpClient.newHttpClient();
//                     // Build the GET request
//                     HttpRequest req = HttpRequest.newBuilder()
//                     .uri(URI.create(HOST + "users/latest-messages")) // Target URL
//                     .header("x-api-key", tm.getToken(userId))
//                     .GET() // GET method
//                     .build();
//                     // Send the request and handle the response
//                     HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//                     // Print response details
//                     // System.out.println("Response code: " + response.statusCode());
//                     // System.out.println("Response body getLastMessages: " + response.body());
//                     return om.readValue(response.body(), new TypeReference<List<Message>>(){});
//                     } catch (IOException | InterruptedException e) {
//                         System.out.println(e.getLocalizedMessage());
//                         return null;
//                     }

//     }
//     public static void UpdateCounter(String id){
//         try {
//             String json = om.writeValueAsString(ChatZone.messageCounters);
//             HttpClient client = HttpClient.newHttpClient();
//             HttpRequest req = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/update-counter"))
//             .header("Content-Type", "application/json")
//             .header("x-api-key", tm.getToken(id))
//             .PUT(HttpRequest.BodyPublishers.ofString(json))
//             .build(); //PUT request
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             System.out.println("Response code: " + response.statusCode());
//             System.out.println("Response body: " + response.body());
//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//         }
        
//     }

//     public static List<UnreadCounter> ShowUnreadMessages(String id) {
//         try {
//             // Create HttpClient
//             HttpClient client = HttpClient.newHttpClient();
//             // Build the GET request
//             HttpRequest req = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/unreadmessages")) // Target URL
//             .header("x-api-key", tm.getToken(id))
//             .GET() // GET method
//             .build();
//             // Send the request and handle the response
//             HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
//             // Print response details
//             // System.out.println("Response code: " + response.statusCode());
//             // System.out.println("Response body: " + response.body());
//             return om.readValue(response.body(), new TypeReference<List<UnreadCounter>>() {});
//         } catch (IOException | InterruptedException e) {
//             System.out.println(e.getLocalizedMessage());
//             return null; 
//         }
        
//     }

//     public static void ResetMessageCounter(String id, String matchId) {
//         try {
//             HttpClient client = HttpClient.newHttpClient();

//             // Add query parameters to the URL
           

//             HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/resetCounter/" + matchId)) // URL with query params
//             .method("PATCH", HttpRequest.BodyPublishers.noBody()) // No request body
//             .header("x-api-key", tm.getToken(id))
//             .build();

//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             System.out.println("Response Code: " + response.statusCode());
//             System.out.println("Response Body: " + response.body());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
        
//     }

//     public static void UpdatePreferrences(User user,String id) {
//         try {
//             String json = om.writeValueAsString(user);
//             System.out.println(json);
//             HttpClient client = HttpClient.newHttpClient();

//             // Add query parameters to the URL
           

//             HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(HOST + "users/updatePreferrences")) // URL with query params
//             .method("PATCH", HttpRequest.BodyPublishers.ofString(json)) 
//             .header("Content-Type", "application/json")
//             .header("x-api-key", tm.getToken(id))
//             .build();

//             HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//             System.out.println("Response Code: " + response.statusCode());
//             System.out.println("Response Body: " + response.body());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
        
//     }
    



// }
// // public static String getJson(){
// //     String jsonContent;
// //     try{
// //         Path jsonPath = Paths.get("users.json");
// //         jsonContent = Files.readString(jsonPath);
// //     }
// //     catch (IOException e)
// //     {
// //         return e.getMessage();
// //     }
// //     return jsonContent;
// // }
// // PATCH example
// // try {
// //     HttpClient client = HttpClient.newHttpClient();

// //     // Add query parameters to the URL
   

// //     HttpRequest request = HttpRequest.newBuilder()
// //     .uri(URI.create(HOST + "/users/updatePreferrences")) // URL with query params
// //     .method("PATCH", HttpRequest.BodyPublishers.ofString(json)) 
// //     .header("x-api-key", tm.getToken(id))
// //     .header("Content-Type", "application/json")
// //     .build();

// //     HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

// //     System.out.println("Response Code: " + response.statusCode());
// //     System.out.println("Response Body: " + response.body());
// // } catch (Exception e) {
// //     e.printStackTrace();
// // }

// // PUT example
// // public static void Put(String _id) {
// //     try {
// //         HttpClient client = HttpClient.newHttpClient();
// //         HttpRequest req = HttpRequest.newBuilder()
// //         .uri(URI.create(HOST + "/users/" + _id)).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString("json-example")).build(); //PUT request
// //         HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
// //         System.out.println("Response code: " + response.statusCode());
// //         System.out.println("Response body: " + response.body());
// //     } catch (IOException | InterruptedException e) {
// //         System.out.println(e.getLocalizedMessage());
// //     }
// // }
// // POST example
// // public static void Post(String json)  {
// //     /*write value as string of objectmapper in jackson dependency will help me make a class in java and turn it into a sendable json to a post request */
// //     // CompletableFuture<String> completableFuture = new  CompletableFuture<>();
// //     // System.out.println(completableFuture.get());
// //     // completableFuture.complete("dddd");
// //     // System.out.println(completableFuture.get());
// //     try {
// //         HttpClient client = HttpClient.newHttpClient();
// //         // System.out.println(jsonPath.toAbsolutePath());
// //         HttpRequest req = HttpRequest.newBuilder().uri(URI.create(LOCAL_HOST + "/users")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
// //         HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
// //         System.out.println("response code: "+ response.statusCode());
// //         System.out.println("response Body: " + response.body());
// //     } catch (IOException | InterruptedException e) {
// //         System.out.println(e.getLocalizedMessage());
// //     }
// // }
// //DELETE example
// // public static void Delete(String _id) {
// //     try {
// //         HttpClient client = HttpClient.newHttpClient();
// //         HttpRequest req = HttpRequest.newBuilder()
// //         .uri(URI.create(LOCAL_HOST + "/users/" + _id)) // Delete user by ID
// //         .DELETE() // DELETE method
// //         .build();
// //         HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
// //         System.out.println("Response code: " + response.statusCode());
// //         System.out.println("Response body: " + response.body());
// //     } catch (IOException | InterruptedException e) {
// //         System.out.println(e.getLocalizedMessage());
// //     }
// // }
// // GET example
// // public static void Get(){
// //     try {
// //         // Create HttpClient
// //         HttpClient client = HttpClient.newHttpClient();
// //         // Build the GET request
// //         HttpRequest req = HttpRequest.newBuilder()
// //         .uri(URI.create(LOCAL_HOST + "/users")) // Target URL
// //         .GET() // GET method
// //         .build();
// //         // Send the request and handle the response
// //         HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
// //         // Print response details
// //         System.out.println("Response code: " + response.statusCode());


// //         System.out.println("Response body: " + response.body());
// //         } catch (IOException | InterruptedException e) {
// //             System.out.println(e.getLocalizedMessage());
// //         }
            
            
            
            
//     //     }
