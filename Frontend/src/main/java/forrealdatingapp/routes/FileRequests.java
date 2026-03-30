package forrealdatingapp.routes;

import forrealdatingapp.utilities.TimeoutInterceptor;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static forrealdatingapp.utilities.RouterUtils.*;

public class FileRequests {
    public static void addPicture(String json, String _id) {
        try {

            OkHttpClient client = BASE_CLIENT.newBuilder()
                    .addInterceptor(new TimeoutInterceptor(3))
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
//                System.out.println("Response body: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    public static boolean updateProfilePicture(String _id, Map<String,String> jsonMap) {
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(3))
                .build();
        try {
            String json = manageJSON().writeValueAsString(jsonMap);
//            System.out.println(json);



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
//                System.out.println("Response body: " + response.body().string());
                return response.code() == 200;
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    public static boolean updatePicture(String id, HashMap<String, String> stringStringHashMap) {
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(3))
                .build();
        try {
            String json = manageJSON().writeValueAsString(stringStringHashMap);



            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "file/changepic")
                    .addHeader("x-api-key", manageToken().getToken(id))
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response code: " + response.code());
//                System.out.println("Response body: " + response.body().string());
                return response.code() == 200;
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }


}
