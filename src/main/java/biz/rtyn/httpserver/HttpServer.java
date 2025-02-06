package biz.rtyn.httpserver;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import biz.rtyn.http.HttpParsingException;
import biz.rtyn.http.HttpRequest;
import biz.rtyn.http.HttpResponse;
import biz.rtyn.http.token.HttpStatusCode;
import biz.rtyn.httpserver.core.io.ReadFileException;
import biz.rtyn.httpserver.core.io.WebRootHandler;
import biz.rtyn.httpserver.core.io.WebRootNotFoundException;

public class HttpServer {

    public static void main(String[] args) {
        int httpPort = 8080;
        int httpsPort = 8443;

        String webroot = "C:/Users/yqb18196/Sites/HTTP Server/WebRoot";

        // Start HTTP server thread
        new Thread(() -> startHttpServer(httpPort, webroot)).start();

//        // Start HTTPS server thread
//        new Thread(() -> {
//            try {
//                startHttpsServer(httpsPort, String webroot);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    // Basic HTTP server using ServerSocket
    private static void startHttpServer(int port, String webroot) {
        try (ExecutorService executor = Executors.newFixedThreadPool(10);
             ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("HTTP Server running on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(() -> handleClientRequest(socket, webroot));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    // Basic HTTPS server using SSLServerSocket
//    private static void startHttpsServer(int port) throws Exception {
//        SSLServerSocketFactory sslServerSocketFactory = createSSLContext().getServerSocketFactory();
//        try (SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port)) {
//            System.out.println("HTTPS Server running on port " + port);
//
//            while (true) {
//                Socket socket = sslServerSocket.accept();
//                handleClientRequest(socket, webroot);
//            }
//        }
//    }

    // Handle client request for both HTTP and HTTPS
    private static void handleClientRequest(Socket socket, String webroot) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                OutputStream outputStream = socket.getOutputStream()
        ) {

            try {

                // Read request line
                // e.g. "GET /index.html HTTP/1.1"
                String requestLine = reader.readLine();
                System.out.println(requestLine);

                // Initiate new request instance
                HttpRequest request = new HttpRequest(requestLine);

                String relativePath = request.getRequestTarget();

                // Read headers until CRLF
                String header;
                while ((header = reader.readLine()) != null && !header.isEmpty()) {
                    request.addHeaderLine(header);
                }

                // Read the body until the end of the input stream (not always ideal)
                StringBuilder body = new StringBuilder();
                while (reader.ready()) {  // Check if there's more data
                    body.append((char) reader.read());
                }
                request.addBody(String.valueOf(body));

                WebRootHandler webRootHandler = new WebRootHandler(webroot);

                // check if ends in slash, append index.html
                if (webRootHandler.checkIfEndsWithSlash(relativePath)) {
                    relativePath += "index.html";
                }

                // ensure that only files within webroot
                if (!webRootHandler.checkIfProvidedRelativePathExists(relativePath)) {
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_404_NOT_FOUND);
                }

                File file = new File(webroot, relativePath);

                HttpResponse response = new HttpResponse();

                String mimeType = webRootHandler.getFileMimeType(relativePath);

                // Send OK response
                response.setHttpVersion("HTTP/1.1");
                response.setStatusCode(HttpStatusCode.SUCCESS_200_OK);
                response.addHeader("Content-Type", mimeType);

                switch(mimeType) {
                    case "text/plain", "text/html":

                        byte[] fileBytes = webRootHandler.getFileByteArrayData(relativePath);
                        response.addBody(new String(fileBytes, StandardCharsets.UTF_8));

                        // output headers + body
                        writer.write(response.toString());
                        writer.flush();

                        break;

                    case "image/jpeg", "image/png":

                        // output headers
                        writer.write(response.toString());
                        writer.flush();

                        // Write image binary data directly to the output stream
                        byte[] imageBytes = webRootHandler.getFileByteArrayData(relativePath);
                        outputStream.write(imageBytes);
                        outputStream.flush();

                        break;

                    default:
                        // code block
                }

            } catch (HttpParsingException e) {

                HttpStatusCode statusCode = e.getErrorCode();

                // Send error response
                String response = "HTTP/1.1 " + statusCode.STATUS_CODE + " " + statusCode.MESSAGE + "\r\nContent-Type: text/plain\r\n\r\n" + statusCode.STATUS_CODE + " " + statusCode.MESSAGE;
                writer.write(response);
                writer.flush();

            } catch (ReadFileException | FileNotFoundException e) {

                HttpStatusCode statusCode = HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR;

                // Send error response
                String response = "HTTP/1.1 " + statusCode.STATUS_CODE + " " + statusCode.MESSAGE + "\r\nContent-Type: text/plain\r\n\r\n" + statusCode.STATUS_CODE + " " + statusCode.MESSAGE;
                writer.write(response);
                writer.flush();

            }

        } catch (IOException e) {
            e.printStackTrace(); // TODO
        }

    }

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
}
