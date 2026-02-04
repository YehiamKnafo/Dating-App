package forrealdatingapp.routes;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.dtos.User;
import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
import static forrealdatingapp.routes.RouterUtils.manageToken;
public class UserProfileRequests {
    public static User getMyProfile(String _id) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(java.time.Duration.ofSeconds(10))
                .readTimeout(java.time.Duration.ofSeconds(10))
                .writeTimeout(java.time.Duration.ofSeconds(10))
                .build();

        Request request = new Request.Builder()
                .url(getHost() + "profile")
                .addHeader("x-api-key", manageToken().getToken(_id))
                .build();

        try (Response response = client.newCall(request).execute()) {
            User user = manageJSON().readValue(response.body().string(), new TypeReference<User>() {
            });
            return user;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
     public static void updateProfile(String json, String _id) {
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
                    .url(getHost() + "profile/updateprofile/" + _id)
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
                    .url(getHost() + "profile/addpicture/" + _id)
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
                    .url(getHost() + "profile/changeprofilepic")
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
    public static void UpdatePreferrences(User user,String id) {
        try {
            String json = manageJSON().writeValueAsString(user);
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
                    .url(getHost() + "profile/updatePreferrences")
                    .addHeader("x-api-key", manageToken().getToken(id))
                    .method("PATCH", body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Body: " + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void UpdateBio(User bioChange, String _id) {
        try {
            String json = manageJSON().writeValueAsString(bioChange);

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
                    .url(getHost() + "profile/updateBio")
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .method("PATCH", body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Body: " + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean deleteAccount(String _id){
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(getHost() + "profile/deleteUser")
                    .method("DELETE", body)
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.code() == 200;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
