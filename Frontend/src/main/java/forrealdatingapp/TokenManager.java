package forrealdatingapp;
 
 import java.util.prefs.Preferences;

 public class TokenManager {
    //for production
    //  private static final String TOKEN_KEY = "x-api-key"; // Key for the token
     private final Preferences prefs;
 
     public TokenManager() {
         // Get the Preferences instance for the current class
         this.prefs = Preferences.userNodeForPackage(TokenManager.class);
     }
 
     // Save the token
     public void saveToken(String _id, String token) {
         prefs.put("token_" + _id, token);
     }
 
     // Retrieve the token
     public String getToken(String _id) {
         return prefs.get("token_" + _id, null); // Default to null if no token is found
     }
 
     // Clear the token
     public void clearToken(String _id) {
         prefs.remove("token_" + _id);
         System.out.println("token cleared successfuly");
     }
    }
 
