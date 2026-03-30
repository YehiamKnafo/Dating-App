package forrealdatingapp.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import forrealdatingapp.mangers.TokenManager;
import forrealdatingapp.credentials.Credentials;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class RouterUtils {
    private static final Credentials credentials = new Credentials();
    private static ObjectMapper om = new ObjectMapper();
    private static TokenManager tm = new TokenManager();
    public static final OkHttpClient BASE_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .build();
    public static Credentials getCredentials() {
        return credentials;
    }

    public static String getCloudinaryUrl(){

        return credentials.getCloudinaryUrl();
    }
    public static String getHost(){ //wouldve got deleted but 30 usages...

        return credentials.getExpressUrl();
    }

    public static TokenManager manageToken(){
        return tm;
    }
    public static void createTokenManger(){
        tm = new TokenManager();
    }
    public static  ObjectMapper manageJSON(){
        return om;
    }




    
    
    
}
