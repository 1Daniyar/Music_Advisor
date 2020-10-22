package advisor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MyAuthorization {

    private static final String client_id = "0ab724b9ac964865ad584d03fbc02a08";
    private static final String client_secret = "826a67c5feca451b968cc24efcc7be7f";

    static String server_path = "https://accounts.spotify.com";
    static String api_server_path = "https://api.spotify.com";
    static int quantityOfElem = 5;

    private static final String redirect_uri = "http://localhost:8080";

    private static final String uri = server_path + "/authorize" +
            "?client_id=" + client_id +
            "&redirect_uri=" + redirect_uri +
            "&response_type=code";

    private static String code = "";
    private static String access_token = "";

    private MyModel model;
    private int currentPage;
    private int maxPage;

    public void printFirst(int variant) {
        switch (variant) {
            case 1:
                model.listOfElem1(1, quantityOfElem);
                break;
            case 2:
                model.listOfElem2(1, quantityOfElem);
                break;
            case 3:
                model.listOfElem3(1, quantityOfElem);
                break;
        }
        System.out.println("---PAGE " + currentPage + " OF " + maxPage + "---");
    }

    public void next(int variant, int direction) {
        if(currentPage + 1 > maxPage && direction > 0 || currentPage -1 < 1 && direction < 0) {
            System.out.println("No more pages.");
        } else {

            if(direction > 0) {
                currentPage++;
            } else
                currentPage--;

            switch (variant) {
                case 1:
                    model.listOfElem1(currentPage, quantityOfElem);
                    break;
                case 2:
                    model.listOfElem2(currentPage, quantityOfElem);
                    break;
                case 3:
                    model.listOfElem3(currentPage, quantityOfElem);
                    break;
            }

            System.out.println("---PAGE " + currentPage + " OF " + maxPage + "---");
        }
    }

    void newAlbums() throws Exception {
        model = new MyModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        currentPage = 1;

        String newReleases_path = api_server_path + "/v1/browse/new-releases";
        String json = getRequest(newReleases_path);
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();

        JsonObject albums = jo.getAsJsonObject("albums");

        for(JsonElement element : albums.getAsJsonArray("items")) {
            JsonObject elObj = element.getAsJsonObject();

            String name = elObj.get("name").getAsString();

            List<String> list = new ArrayList<>();
            for(JsonElement element2 : elObj.getAsJsonArray("artists")) {
                JsonObject el2Obj = element2.getAsJsonObject();
                String nameOfArtist = el2Obj.get("name").getAsString();
                list.add(nameOfArtist);
            }

            JsonObject externalObj = elObj.getAsJsonObject("external_urls");
            String spotify = externalObj.get("spotify").getAsString();

            model.addName(name);
            model.addArtists(list);
            model.addReference(spotify);
        }
        maxPage = (int) Math.ceil(model.getSize() / (double) quantityOfElem);
    }

    void featured() throws Exception {
        model = new MyModel(new ArrayList<>(), new ArrayList<>());
        currentPage = 1;

        String featured_path = api_server_path + "/v1/browse/featured-playlists";
        String json = getRequest(featured_path);
        JsonObject playObj = JsonParser.parseString(json).getAsJsonObject();

        JsonObject playlistsObject = playObj.getAsJsonObject("playlists");
        for(JsonElement element : playlistsObject.getAsJsonArray("items")) {
            JsonObject elemObj = element.getAsJsonObject();
            String nameOfPlaylist = elemObj.get("name").getAsString();
            JsonObject externalObj = elemObj.getAsJsonObject("external_urls");
            String spotify = externalObj.get("spotify").getAsString();

            model.addName(nameOfPlaylist);
            model.addReference(spotify);
        }
        maxPage = (int) Math.ceil(model.getSize() / (double) quantityOfElem);
    }

    void getCategoryByName(String name) throws Exception{
        model = new MyModel(new ArrayList<>(), new ArrayList<>());
        currentPage = 1;

        String categories_path = api_server_path + "/v1/browse/categories";
        String json = getRequest(categories_path);
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        JsonObject categoriesObject = jo.getAsJsonObject("categories");

        String category_id = "";

        for(JsonElement element : categoriesObject.getAsJsonArray("items")) {
            JsonObject elemObj = element.getAsJsonObject();
            if(elemObj.get("name").getAsString().equals(name)) {
                category_id = elemObj.get("id").getAsString();
                break;
            }
        }

        String playlists_path = api_server_path + "/v1/browse/categories/" + category_id + "/playlists";

        String jo2 = getRequest(playlists_path);
        JsonObject playObj = JsonParser.parseString(jo2).getAsJsonObject();

        if(category_id.length() == 0) {
            System.out.println("Unknown category name.");
        } else {
            try {
                JsonObject playlistsObject = playObj.getAsJsonObject("playlists");

                for (JsonElement element : playlistsObject.getAsJsonArray("items")) {
                    JsonObject elemObj = element.getAsJsonObject();
                    String nameOfPlaylist = elemObj.get("name").getAsString();
                    JsonObject externalObj = elemObj.getAsJsonObject("external_urls");
                    String spotify = externalObj.get("spotify").getAsString();

                    model.addName(nameOfPlaylist);
                    model.addReference(spotify);
                }
            } catch (Exception e) {
                JsonObject error = playObj.getAsJsonObject("error");
                String message = error.get("message").getAsString();
                System.out.println(message);
            }
        }
        maxPage = (int) Math.ceil(model.getSize() / (double) quantityOfElem);
    }

    void getCategories() throws Exception {
        model = new MyModel(new ArrayList<>());
        currentPage = 1;

        String categories_path = api_server_path + "/v1/browse/categories";
        String json = getRequest(categories_path);

        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        JsonObject categoriesObject = jo.getAsJsonObject("categories");

        for(JsonElement element : categoriesObject.getAsJsonArray("items")) {
            JsonObject elemObj = element.getAsJsonObject();
            model.addName(elemObj.get("name").getAsString());
        }
        maxPage = (int) Math.ceil(model.getSize() / (double) quantityOfElem);
    }

    String getRequest(String uriApi) throws Exception {

        HttpClient client = HttpClient.newBuilder().build();

        // создать Get запрос
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + access_token)
                .uri(URI.create(uriApi))
                .GET()
                .build();

        // отправить запрос и получить ответ
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    // server listen for the incoming queries
    void server() throws Exception {

        System.out.println("use this link to request the access code:");
        System.out.println(uri);

        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 0);

        server.createContext("/",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        String answer;
                        if(query != null && query.substring(0, 4).equals("code")){
                            code = query.substring(5);
                            answer = "Got the code. Return back to your program.";
                        } else {
                            answer = "Authorization code not found. Try again.";
                        }
                        exchange.sendResponseHeaders(200, answer.length());
                        exchange.getResponseBody().write(answer.getBytes());
                        exchange.getResponseBody().close();

                    }
                });

        System.out.println("waiting for code...");

        server.start();

        while (code.length() == 0){
            Thread.sleep(50);
        }

        server.stop(1);

        System.out.println("code received");
    }

    // get token
    void postRequest() throws Exception {

        System.out.println("making http request for access_token...");

        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(server_path + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code=" + code +
                                "&redirect_uri=" + redirect_uri +
                                "&client_id=" + client_id +
                                "&client_secret=" + client_secret
                ))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        access_token = jo.get("access_token").getAsString();
        System.out.println("Success!");
    }
}