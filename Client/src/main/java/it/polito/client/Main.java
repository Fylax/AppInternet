package it.polito.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.net.CookieManager;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static String baseUri = "http://localhost:8080/";

    public static void main(String[] args) {

        try {

            HttpClient client = HttpClient.
                    newBuilder().
                    version(HttpClient.Version.HTTP_2).
                    cookieHandler(new CookieManager()).
                    followRedirects(HttpClient.Redirect.SECURE).
                    build();

            String json = "{\"user\":\"elena\",\"pwd\":\"cande\"}";
            ObjectMapper objectMapper = new ObjectMapper();

            HttpRequest req = HttpRequest.newBuilder().
                    uri(new URI(baseUri + "login")).
                    header("content-type", "application/json").
                    POST(HttpRequest.BodyPublisher.fromString(json)).
                    build();

            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandler.asString());

            if( resp.statusCode() == 200){

                Position p1 = new Position(54,66, 123456);
                Position p2 = new Position(54, 66, 123666);
                List<Position> positions = new ArrayList<>();
                positions.add(p1);
                positions.add(p2);

                String s = objectMapper.writeValueAsString(positions);
                System.out.println(s);

                HttpRequest req_p = HttpRequest.newBuilder().
                        uri(new URI(baseUri + "position")).
                        header("Cookie", resp.headers().firstValue("set-cookie").get()).
                        header("content-type", "application/json").
                        POST(HttpRequest.BodyPublisher.fromString(s)).
                        build();

                HttpResponse<String> res = client.send(req_p, HttpResponse.BodyHandler.asString(Charset.defaultCharset()));
                System.out.println(res.toString());

                if(res.statusCode() == 200){
                    HttpRequest r1 = HttpRequest.newBuilder().
                            uri(new URI(baseUri + "position")).
                            header("Cookie", resp.headers().firstValue("set-cookie").get()).
                            header("accept", "application/json").
                            GET().
                            build();
                    HttpResponse<String> res1 = client.send(r1, HttpResponse.BodyHandler.asString(Charset.defaultCharset()));
                    System.out.println(res1.toString());

                    HttpRequest r2 = HttpRequest.newBuilder().
                            uri(new URI(baseUri + "position?start=123&end=1234567")).
                            header("Cookie", resp.headers().firstValue("set-cookie").get()).
                            header("accept", "application/json").
                            GET().
                            build();
                    HttpResponse<String> res2 = client.send(r2, HttpResponse.BodyHandler.asString(Charset.defaultCharset()));
                    System.out.println(res2.toString());
                }

                HttpRequest out = HttpRequest.newBuilder().
                        uri(new URI(baseUri + "logout")).
                        header("Cookie", resp.headers().firstValue("set-cookie").get()).
                        header("content-type", "application/json").
                        POST(HttpRequest.BodyPublisher.noBody()).
                        build();

                HttpResponse<String> r = client.send(out, HttpResponse.BodyHandler.asString(Charset.defaultCharset()));
                System.out.println(r.toString());
            }
            System.out.println(resp.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
