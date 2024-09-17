package de.tnttastisch.yuija;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ApiHandler {

    private final String API_URL;
    private final String AUTH_TOKEN;

    /**
     * Constructs a new {@code YuijaHandler} with the specified API URL and authorization token.
     *
     * @param API_URL    the base URL of the API
     * @param AUTH_TOKEN the authorization token for API authentication
     */
    public ApiHandler(String API_URL, String AUTH_TOKEN) {
        this.API_URL = API_URL;
        this.AUTH_TOKEN = AUTH_TOKEN;
    }

    /**
     * Sends a POST request to the API using the provided content with a default timeout of 15,000 milliseconds.
     *
     * @param content the content to be sent in the request body
     * @return the API response as a {@code String}
     */
    public String callApiRequest(String content) {
        return request(content, 15000);
    }

    /**
     * Sends a POST request to the API using the provided content and a custom timeout.
     *
     * @param content the content to be sent in the request body
     * @param timeout the timeout duration in milliseconds
     * @return the API response as a {@code String}
     */
    public String callApiRequest(String content, long timeout) {
        return request(content, timeout);
    }

    /**
     * Helper method to build and send the HTTP request.
     *
     * @param content the content to be sent in the request body
     * @param timeout the timeout duration in milliseconds
     * @return the API response as a {@code String}
     */
    private String request(String content, long timeout) {
        AtomicReference<String> response = new AtomicReference<>("");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(URI.create(API_URL));
        builder.header("x-goog-api-key", AUTH_TOKEN);
        builder.header("Content-Type", "application/json");
        builder.timeout(Duration.ofMillis(timeout));

        HashMap<String, Object> body = new HashMap<>();
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> paramParts = new HashMap<>();

        params.put("text", content);
        paramParts.put("parts", params);
        body.put("contents", paramParts);

        builder.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(body)));
        client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).thenAccept(response::set).join();
        return response.toString();
    }
}
