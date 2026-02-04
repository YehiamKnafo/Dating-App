package forrealdatingapp.routes;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import forrealdatingapp.chatScenes.ChatZone;
import forrealdatingapp.dtos.Message;
import forrealdatingapp.dtos.UnreadCounter;
import static forrealdatingapp.routes.RouterUtils.*;
public class MessageRequests {
    public static List<Map<String, Object>> FetchMessages(String userId, String matchId) {
        try {
            Map<String, String> jsonMap = new HashMap<>(Map.of("matchID", matchId));
            String json = manageJSON().writeValueAsString(jsonMap);

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
                    .url(getHost() + "messages/fetchmessages")
                    .addHeader("x-api-key", manageToken().getToken(userId))
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return manageJSON().readValue(response.body().string(), new TypeReference<List<Map<String, Object>>>(){});
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
    public static List<Message> getLastMessages(String userId) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            Request request = new Request.Builder()
                    .url(getHost() + "messages/latest-messages")
                    .addHeader("x-api-key", manageToken().getToken(userId))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return manageJSON().readValue(response.body().string(), new TypeReference<List<Message>>(){});
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
    public static void UpdateCounter(String id){
        try {
            String json = manageJSON().writeValueAsString(ChatZone.messageCounters);

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
                    .url(getHost() + "messages/update-counter")
                    .addHeader("x-api-key", manageToken().getToken(id))
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

    public static List<UnreadCounter> ShowUnreadMessages(String id) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            Request request = new Request.Builder()
                    .url(getHost() + "messages/unreadmessages")
                    .addHeader("x-api-key", manageToken().getToken(id))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return manageJSON().readValue(response.body().string(), new TypeReference<List<UnreadCounter>>() {});
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    public static void ResetMessageCounter(String id, String matchId) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            Request request = new Request.Builder()
                    .url(getHost() + "messages/resetCounter/" + matchId)
                    .addHeader("x-api-key", manageToken().getToken(id))
                    .method("PATCH", RequestBody.create("", null))
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response Code: " + response.code());
                System.out.println("Response Body: " + response.body().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
