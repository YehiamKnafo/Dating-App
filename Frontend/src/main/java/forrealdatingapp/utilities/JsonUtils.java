package forrealdatingapp.utilities;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static String Serialize(Object obj){
        // deliver to req.body method 
        /* 
         * i need to take a user  class turn it to json
         * so basically return a string
         */

        try{
            return objectMapper.writeValueAsString(obj);
        }
        catch(IOException e){
            return e.getLocalizedMessage();
        }
        
    }
    public static <T> Map<String, Object> deserializeToMap(String body) {
        try {
            // Deserialize JSON string to Map
            return objectMapper.readValue(body, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON", e);
        }
    }
    
}
