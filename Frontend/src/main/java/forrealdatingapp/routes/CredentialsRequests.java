package forrealdatingapp.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import forrealdatingapp.credentials.Credentials;
import forrealdatingapp.utilities.Config;
import forrealdatingapp.utilities.RouterUtils;
import forrealdatingapp.utilities.TimeoutInterceptor;
import okhttp3.*;
import org.json.JSONObject;

import java.beans.Encoder;
import java.io.IOException;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.TimeUnit;


import static forrealdatingapp.utilities.RouterUtils.*;

public class CredentialsRequests {
    public static boolean credentialsRetrieve() throws JsonProcessingException {


        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .connectionPool(new okhttp3.ConnectionPool(5, 5, TimeUnit.MINUTES)) // Standard pooling
                .build();

        Request request = new Request.Builder()
                //PUBLIC BACKEND NOT THE MAIN ONE
                .url(Config.get("api.base_url") +"/api/config")
                .addHeader("X-App-Signature", "JavaFX-Client-v1")
                .build();


        try (Response response = client.newCall(request).execute()) {
            String resBody = null;

            ResponseBody body = response.body();

            if (body != null) {
                resBody = body.string();
            }
            if (resBody == null || resBody.isBlank()) {
//            System.out.println("Server returned empty body (probably sleeping)");
                return false;
            }

//        System.out.println("resbody: " + resBody);

            Credentials c = manageJSON().readValue(resBody, new TypeReference<Credentials>() {});

            RouterUtils.getCredentials().setCloudinaryUrl(c.getCloudinaryUrl());
            RouterUtils.getCredentials().setExpressUrl(c.getExpressUrl());
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }

    public static JSONObject CheckForUpdate(String version) {
        String baseUrl = Config.get("api.base_url");
        String finalUrl = baseUrl + "/api/checkForUpdates?v=" +
                version;
        System.out.println(finalUrl);
// Result: https://your-dating-app.onrender.com/checkForUpdates?v=1.0.0

        try {

        Request request = new Request.Builder()
                .url(finalUrl)
                .build();
        try (Response response = BASE_CLIENT.newCall(request).execute()) {
//            System.out.println(response.body().string());
            return new JSONObject(response.body().string());

        }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
