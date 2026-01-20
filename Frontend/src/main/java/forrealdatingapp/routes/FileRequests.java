package forrealdatingapp.routes;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static forrealdatingapp.routes.RouterUtils.getHost;
import static forrealdatingapp.routes.RouterUtils.manageJSON;
import static forrealdatingapp.routes.RouterUtils.manageToken;
public class FileRequests {
    public static void addPicture(String json, String _id) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder()
            .uri(URI.create(getHost() + "file/addpicture/" + _id))
            .header("Content-Type", "application/json")
            .header("x-api-key",manageToken().getToken(_id))
            .PUT(HttpRequest.BodyPublishers.ofString(json))
            .build(); //PUT request
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
    public static boolean updateProfilePicture(String _id, Map<String,String> jsonMap) {
        try {
                    String json = manageJSON().writeValueAsString(jsonMap);
                    System.out.println(json);
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest req = HttpRequest.newBuilder()
                    // other route take this route as a param
                    .uri(URI.create(getHost() + "file/changeprofilepic"))
                    .header("Content-Type", "application/json")
                    .header("x-api-key",manageToken().getToken(_id))
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build(); //PUT request
                    HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Response code: " + response.statusCode());
                    System.out.println("Response body: " + response.body());
                    return response.statusCode() == 200;
                } catch (IOException | InterruptedException e) {
                    System.out.println(e.getLocalizedMessage());
                    return false;
                }
    }


}
