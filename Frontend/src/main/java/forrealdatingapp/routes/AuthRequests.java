package forrealdatingapp.routes;

import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
public class AuthRequests {

    public static boolean postSignup(String json) {
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
                    .url(getHost() + "auth")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Status Code: " + response.code());
                System.out.println("Response Body: " + response.body().string());
                return response.code() == 201;
            }

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return false;

    }


    public static String PostLogin(String json) {
        try {
            System.out.println("DEBUG: Using OkHttp for login request");

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
                    .url("http://localhost:4000/login")
                    .post(body)
                    .build();

//            System.out.println("DEBUG: Sending request to: " + getHost() + "auth/login");

            try (Response response = client.newCall(request).execute()) {  // ← FIXED HERE
                System.out.println("Response code: " + response.code());

                if (response.body() != null) {
                    String responseBody = response.body().string();
//                    System.out.println("Response body: " + responseBody);
                    return responseBody;
                } else {
                    System.err.println("Response body is null");
                    return null;
                }
            }

        } catch (IOException e) {
            System.err.println("Login request failed:");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Unexpected error:");
            e.printStackTrace();
            return null;
        }
    }
    public static boolean verifyOtpRequest(String email, String otp) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            String jsonInputString = "{\"email\": \"" + email + "\", \"otp\": \"" + otp + "\"}";

            RequestBody body = RequestBody.create(
                    jsonInputString,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth/verify-otp")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                int code = response.code();
                if (code == 200) {
                    System.out.println("OTP verified successfully");
                    return true;
                } else {
                    System.out.println("Invalid OTP");
                    return false;
                }
            }

        } catch (IOException e) {
            return false;
        }
    }

    public static String sendOtpRequest(String email,String type) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .readTimeout(java.time.Duration.ofSeconds(10))
                    .writeTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            // Define the request body
            Map<String, String> emailmap = new HashMap<>(Map.of("email", email,"type",type));
            String requestBody = manageJSON().writeValueAsString(emailmap);

            RequestBody body = RequestBody.create(
                    requestBody,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth/send-otp")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
    public static String Resetusrpass(String passwordString, String email) {
        try {
            Map<String, Object> jsonMap = new HashMap<>(Map.of("email", email,"password",passwordString));
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
                    .url(getHost() + "auth/resetpassword")
                    .put(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response code: " + response.code());
                System.out.println("Response body: " + response.body().string());
                if(response.code() == 201) return "201|password reset successfuly";
                if (response.code() == 404) return "404|user not exist in the database";
                if (response.code() == 403) return "403|you tried to change to the same password";
                return null;
            }

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }
    
}
