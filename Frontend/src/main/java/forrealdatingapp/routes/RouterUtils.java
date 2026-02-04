package forrealdatingapp.routes;

import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.App;
import forrealdatingapp.TokenManager;
import forrealdatingapp.credentials.Credentials;

public class RouterUtils {
    private static final Credentials credentials = new Credentials();
    private static ObjectMapper om = new ObjectMapper();
    private static TokenManager tm = new TokenManager();

    public static Credentials getCredentials() {
        return credentials;
    }

    public static String getCludinaryUrl(){

        return credentials.getCloudinaryUrl();
    }
    public static String getHost(){ //wouldve got deleted but 30 usages...

        return credentials.getExpressUrl();
    }

    public static TokenManager manageToken(){
        return tm;
    }
    public static  ObjectMapper manageJSON(){
        return om;
    }




    
    
    
}
