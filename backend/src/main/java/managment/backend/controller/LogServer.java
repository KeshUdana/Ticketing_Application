package managment.backend.controller;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public class LogServer {

    public static void startServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/logs", new LogHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Log server started at http://localhost:8080/logs");
    }

    static class LogHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File logFile = new File("thread_activity.log");
            byte[] logBytes = new FileInputStream(logFile).readAllBytes();

            exchange.sendResponseHeaders(200, logBytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(logBytes);
            os.close();
        }
    }
}
