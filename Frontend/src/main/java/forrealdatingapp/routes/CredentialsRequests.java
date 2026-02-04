package forrealdatingapp.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import forrealdatingapp.credentials.Credentials;
import forrealdatingapp.dtos.User;
import javafx.print.Collation;
import okhttp3.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;

import static forrealdatingapp.routes.RouterUtils.*;

public class CredentialsRequests {
    public static void credentialsRetrieve(String token) throws JsonProcessingException {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(java.time.Duration.ofSeconds(10))
                .readTimeout(java.time.Duration.ofSeconds(10))
                .writeTimeout(java.time.Duration.ofSeconds(10))
                .build();

        Request request = new Request.Builder()
                .url("http://localhost:4000")
                .addHeader("x-api-key", token)
                .build();
        String resBody ="";
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            resBody = response.body().string();

        } catch (IOException e) {

        }
//        System.out.println(resBody);
        Credentials c = manageJSON().readValue(resBody, new TypeReference<Credentials>() {});
        RouterUtils.getCredentials().setCloudinaryUrl(c.getCloudinaryUrl());
        RouterUtils.getCredentials().setExpressUrl(c.getExpressUrl());
    }
}
