package info.rori.lunchbox;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import info.rori.lunchbox.api.v1.LunchboxApi;

import java.io.IOException;
import java.net.URI;

public class App {
    public static final String BASE_URI = "http://localhost:8080/api/v1";

    public static HttpServer startServer() {
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new LunchboxApi());
        server.getServerConfiguration().addHttpHandler(new StaticHttpHandler("src/main/webapp/swagger-ui"));
        return server;
    }

    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Lunchbox API started with address %s\nHit enter to stop it...", BASE_URI));
        System.in.read();
        server.shutdownNow();
    }
}
