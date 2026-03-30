package forrealdatingapp.routes;

import forrealdatingapp.utilities.TimeoutInterceptor;
import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.dtos.User;

import static forrealdatingapp.utilities.RouterUtils.*;

public class UserProfileRequests {
    public static User getMyProfile(String _id) {

        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
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
         OkHttpClient client = BASE_CLIENT.newBuilder()
                 .addInterceptor(new TimeoutInterceptor(30))
                 .build();
        try {


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
//                System.out.println("Response body: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

//    public static void addPicture(String json, String _id) {
//        OkHttpClient client = BASE_CLIENT.newBuilder()
//                .addInterceptor(new TimeoutInterceptor(30))
//                .build();
//        try {
//
//
//            RequestBody body = RequestBody.create(
//                    json,
//                    MediaType.parse("application/json; charset=utf-8")
//            );
//
//            Request request = new Request.Builder()
//                    .url(getHost() + "profile/addpicture/" + _id)
//                    .addHeader("x-api-key", manageToken().getToken(_id))
//                    .put(body)
//                    .build();
//
//            try (Response response = client.newCall(request).execute()) {
//                System.out.println("Response code: " + response.code());
////                System.out.println("Response body: " + response.body().string());
//            }
//        } catch (IOException e) {
//            System.out.println(e.getLocalizedMessage());
//        }
//    }
    public static boolean updateProfilePicture(String _id, String url) {
        String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8);
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {



            RequestBody body = RequestBody.create(
                    "",
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "profile/changeprofilepic?url=" + encodedUrl)
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
    public static String UpdatePreferrences(User user,String id) {
        try {
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
            String json = manageJSON().writeValueAsString(user);
            System.out.println(json);



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
            int code = response.code();
                System.out.println("Response Code: " + response.code());
//                System.out.println("Response Body: " + response.body().string());

                if (code == 400){
                    return "Enter a valid date";
                }
                return "";
            }


        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
    public static void UpdateBio(User bioChange, String _id) {
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {
            String json = manageJSON().writeValueAsString(bioChange);



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
    public static boolean deletePicture(String token, Map<String,Object> reqbody){
        String encodedUrl = URLEncoder.encode(reqbody.get("url").toString(), StandardCharsets.UTF_8);
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {


            Request request = new Request.Builder()
                    .url(getHost() + "profile/deletepicture?url=" + encodedUrl)
                    .delete()
                    .addHeader("x-api-key", manageToken().getToken(token))
                    .build();
            try (Response response = client.newCall(request).execute()) {
                return response.code() == 200;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    public static boolean deleteAccount(String _id){
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {

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
