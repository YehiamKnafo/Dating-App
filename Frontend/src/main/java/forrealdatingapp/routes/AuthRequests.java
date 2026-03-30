package forrealdatingapp.routes;

import com.fasterxml.jackson.core.JsonProcessingException;
import forrealdatingapp.utilities.TimeoutInterceptor;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static forrealdatingapp.utilities.RouterUtils.*;

public class AuthRequests {

    public static Map<String, Object> postSignup(String json) {
        Map<String, Object> returnMap = new HashMap<>();
        try {

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth")
                    .post(body)
                    .build();

            try (Response response = BASE_CLIENT.newCall(request).execute()) {
//                System.out.println("Status Code: " + response.code());
                returnMap.put("body", response.body().string());
                returnMap.put("bool", response.code() == 201);
                return returnMap;
            }

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;

    }


    public static String PostLogin(String json) {
        try {
            System.out.println("DEBUG: Using OkHttp for login request");

            OkHttpClient client = BASE_CLIENT.newBuilder()
                    .addInterceptor(new TimeoutInterceptor(20))
                    .build();


            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth/login")
                    .post(body)
                    .build();

//            System.out.println("DEBUG: Sending request to: " + getHost() + "auth/login");

            try (Response response = client.newCall(request).execute()) {

//                System.out.println("Response code: " + response.code());
                if (response.body() != null) {
                    //                    System.out.println("Response body: " + responseBody);
                    return response.body().string();

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


            String jsonInputString = "{\"email\": \"" + email + "\", \"otp\": \"" + otp + "\"}";

            RequestBody body = RequestBody.create(
                    jsonInputString,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth/verify-otp")
                    .post(body)
                    .build();

            try (Response response = BASE_CLIENT.newCall(request).execute()) {
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

    public static JSONObject sendOtpRequest(String email, String type) {
        try {
            OkHttpClient client = BASE_CLIENT.newBuilder()
                    .addInterceptor(new TimeoutInterceptor(10))
                    .build();
            // Define the request body
            Map<String, String> emailmap = new HashMap<>(Map.of("email", email, "type", type));
            String requestBody = manageJSON().writeValueAsString(emailmap);

            RequestBody body = RequestBody.create(
                    requestBody,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth/send-otp")
//                    .url("http://localhost:3000/auth/send-otp")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                ResponseBody b = response.body();

                int c = response.code();
                String resbody = response.body().string();
                return new JSONObject(Map.of("code", c, "body", resbody));

            }

        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject(Map.of("msg", "error"));
        }
    }

    public static String Resetusrpass(String passwordString, String email) {
        try {
            Map<String, Object> jsonMap = new HashMap<>(Map.of("email", email,"password",passwordString));
            String json = manageJSON().writeValueAsString(jsonMap);



            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(getHost() + "auth/resetpassword")
                    .put(body)
                    .build();

            try (Response response = BASE_CLIENT.newCall(request).execute()) {
//                System.out.println("Response code: " + response.code());
//                System.out.println("Response body: " + response.body().string());
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

    public static boolean dropOtp(String email) {

        String reqbody;
        try {
            reqbody = manageJSON().writeValueAsString(Map.of("email", email));

            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(reqbody, JSON);
            Request request = new Request.Builder()
                    .url(getHost() + "auth/dropOtp")
                    .post(body)
                    .build();
            try (Response response = BASE_CLIENT.newCall(request).execute()) {
                return response.code() == 200;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
