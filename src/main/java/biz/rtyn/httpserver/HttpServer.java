package biz.rtyn.httpserver;

import biz.rtyn.httpserver.core.ServerListenerThread;

public class HttpServer {

    private static int port = 8080;
    private static String webroot = "C:\\Users\\yqb18196\\Sites\\HTTP Server\\WebRoot";

    public static void main(String[] args) {
        System.out.println("Starting server...");

        ServerListenerThread serverListenerThread = new ServerListenerThread(port, webroot);
        serverListenerThread.start();
    }
}

//import javax.net.ssl.*;
//        import java.io.*;
//        import java.net.*;
//        import java.security.KeyStore;
//
//public class HttpServer {
//
//    public static void main(String[] args) {
//        int httpPort = 8080;
//        int httpsPort = 8443;
//
//        // Start HTTP server thread
//        new Thread(() -> startHttpServer(httpPort)).start();
//
//        // Start HTTPS server thread
//        new Thread(() -> {
//            try {
//                startHttpsServer(httpsPort);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    // Basic HTTP server using ServerSocket
//    private static void startHttpServer(int port) {
//        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            System.out.println("HTTP Server running on port " + port);
//
//            while (true) {
//                Socket socket = serverSocket.accept();
//                handleClientRequest(socket, false);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Basic HTTPS server using SSLServerSocket
//    private static void startHttpsServer(int port) throws Exception {
//        SSLServerSocketFactory sslServerSocketFactory = createSSLContext().getServerSocketFactory();
//        try (SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port)) {
//            System.out.println("HTTPS Server running on port " + port);
//
//            while (true) {
//                Socket socket = sslServerSocket.accept();
//                handleClientRequest(socket, true);
//            }
//        }
//    }
//
//    // Handle client request for both HTTP and HTTPS
//    private static void handleClientRequest(Socket socket, boolean secure) {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
//
//            // Read request (basic implementation for demonstration)
//            String requestLine = reader.readLine();
//            System.out.println((secure ? "HTTPS" : "HTTP") + " Request: " + requestLine);
//
//            // Send response
//            String response = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nHello, this is a " + (secure ? "secure" : "plain") + " server!";
//            writer.write(response);
//            writer.flush();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Create SSL context using Java Keystore (JKS)
//    private static SSLContext createSSLContext() throws Exception {
//        char[] password = "password".toCharArray();
//
//        // Load the keystore from a JKS file
//        KeyStore keyStore = KeyStore.getInstance("JKS");
//        try (InputStream keyStoreStream = SimpleHttpServer.class.getResourceAsStream("/keystore.jks")) {
//            if (keyStoreStream == null) {
//                throw new FileNotFoundException("Keystore file not found!");
//            }
//            keyStore.load(keyStoreStream, password);
//        }
//
//        // Initialize KeyManagerFactory with the keystore
//        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//        keyManagerFactory.init(keyStore, password);
//
//        // Create SSL context
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
//        return sslContext;
//    }
//}
