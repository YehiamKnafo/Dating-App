package forrealdatingapp.routes;

import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.App;
import forrealdatingapp.TokenManager;
public class RouterUtils {
    private static String HOST;
    private static String TCP_HOST;
    private static int TCP_PORT;
    private static String CLOUDINARY_URL;
    private static ObjectMapper om = new ObjectMapper();
    private static TokenManager tm = new TokenManager();

    public static String getCludinaryUrl(){
        CLOUDINARY_URL = App.getEnv("CLOUDINARY_URL");
        return CLOUDINARY_URL;
    }
    public static String getHost(){
        HOST = App.getEnv("EXPRESS");
        return HOST;
    }
    public static String getTCPHost(){
        TCP_HOST = App.getEnv("TCP").split(":")[0];
        return TCP_HOST;
    }
    public static int getTCPPort(){
        TCP_PORT = Integer.parseInt(App.getEnv("TCP").split(":")[1]);
        return TCP_PORT;
    }
    public static TokenManager manageToken(){
        return tm;
    }
    public static  ObjectMapper manageJSON(){
        return om;
    }




    
    
    
}
