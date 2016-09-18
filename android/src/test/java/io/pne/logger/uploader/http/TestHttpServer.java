package io.pne.logger.uploader.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class TestHttpServer implements HttpHandler {

    private HttpServer server;

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(9095), 0);
        server.setExecutor(null);
        server.createContext("/log/upload", this);
        server.start();
    }

    public void stop() {
//        server.stop(100);
    }

    @Override
    public void handle(HttpExchange aExchange) throws IOException {
        System.out.println("Headers:");
        for (Map.Entry<String, List<String>> entry : aExchange.getRequestHeaders().entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        InputStream in = aExchange.getRequestBody();
        byte[] buf = new byte[1024];
        int size;
        while ( ( size = in.read(buf, 0, buf.length)) >= 0 ) {

        }

        String response = "File saved";
        aExchange.sendResponseHeaders(200, response.length());
        OutputStream os = aExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    public static void main(String[] args) throws IOException {
        new TestHttpServer().start();

        System.out.println("Press ENTER key to exit");
        System.in.read();
        System.exit(0);
    }
}
