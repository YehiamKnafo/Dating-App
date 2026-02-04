package forrealdatingapp.routes;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
import static forrealdatingapp.routes.RouterUtils.manageToken;
public class FileRequests {
    public static void addPicture(String json, String _id) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "file/addpicture/" + _id)
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response body: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    public static boolean updateProfilePicture(String _id, Map<String,String> jsonMap) {
        try {
            String json = manageJSON().writeValueAsString(jsonMap);
            System.out.println(json);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "file/changeprofilepic")
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response body: " + response.body().string());
                return response.code() == 200;
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }


}
