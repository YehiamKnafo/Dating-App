package forrealdatingapp.routes;

import forrealdatingapp.utilities.TimeoutInterceptor;
import okhttp3.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.dtos.User;

import static forrealdatingapp.utilities.RouterUtils.*;

public class MatchingRequests {
    public static Queue<User> getUsers() {


        Request request = new Request.Builder()
                .url(getHost() + "matches")
                .addHeader("x-api-key", "example")
                .build();

        try (Response response = BASE_CLIENT.newCall(request).execute()) {
            Queue<User> users = manageJSON().readValue(response.body().string(), new TypeReference<Queue<User>>() {
            });
//            System.out.println(users);
            return users;
        } catch (IOException e) {
            return null;
        }
    }

    public static Queue<User> getUsers(String _id,String page) {
//        System.out.println(manageToken().getToken(_id));
        // Create the query string
        String queryString = (page != null) ? "?page=" + URLEncoder.encode(page, StandardCharsets.UTF_8) : "";
        String url = getHost() + "matches/querygetmatches" + queryString;

        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-api-key", manageToken().getToken(_id))
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            // Debugging output
//            System.out.println(responseBody);

            // Parse JSON response into Queue<User>
            return manageJSON().readValue(responseBody, new TypeReference<Queue<User>>() {});
        } catch (IOException e) {
            return null;
        }
    }
public static void Dislike(String json, String _id) {
        OkHttpClient client = BASE_CLIENT.newBuilder()
            .addInterceptor(new TimeoutInterceptor(30))
            .build();
        try {


            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "matches/dislike")
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("response code: " + response.code());
//                System.out.println("response Body: " + response.body().string());
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    public static Map<String, Boolean> CheckMatch(String json,String _id) {
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {


            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "matches/checkmatch")
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("response code: " + response.code());
                String responseBody = response.body().string();
//                System.out.println("response Body: " + responseBody);
                return manageJSON().readValue(responseBody, new TypeReference<Map<String, Boolean>>() {});
            }
        } catch (IOException e) {
            return null;
        }
    }


    public static List<User> getMatches(String page, String _id) {
        StringBuilder queryParams = new StringBuilder("?");

        if (page != null) queryParams.append("page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8)).append("&");

        // Remove trailing "&" if exists
        if (queryParams.length() > 1) {
            queryParams.setLength(queryParams.length() - 1);
        } else {
            queryParams.setLength(0); // No params provided, avoid sending "?"
        }
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {


            Request request = new Request.Builder()
                    .url(getHost() + "matches/getmatches" + queryParams)
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return manageJSON().readValue(response.body().string(), new TypeReference<List<User>>(){});
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

  

    public static boolean Unmatch(String _id, String _matchId) {
        Map<String, String> reqBodyMap = new HashMap<>(Map.of("_matchId", _matchId));
        OkHttpClient client = BASE_CLIENT.newBuilder()
                .addInterceptor(new TimeoutInterceptor(30))
                .build();
        try {
            String reqBody = manageJSON().writeValueAsString(reqBodyMap);



            RequestBody body = RequestBody.create(
                    reqBody,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "matches/unmatch")
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("response code: " + response.code());
                String responseBody = response.body().string();
//                System.out.println("response Body: " + responseBody);
                Map<String, Object> resbody = manageJSON().readValue(responseBody, new TypeReference<Map<String, Object>>(){});
                if (resbody.containsKey("approved"))
                    return (boolean)resbody.get("approved");
                return false;
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
public static User getMatchedProfile(String _id, Map<String, Object> jsonMap){
        OkHttpClient client = BASE_CLIENT.newBuilder()
            .addInterceptor(new TimeoutInterceptor(30))
            .build();
        try {
            String json = manageJSON().writeValueAsString(jsonMap);



            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "matches/getMatchedProfile")
                    .addHeader("x-api-key", manageToken().getToken(_id))
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("response code: " + response.code());
                String responseBody = response.body().string();
                System.out.println("response Body: " + responseBody);
                return manageJSON().readValue(responseBody, User.class);
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    
}}

