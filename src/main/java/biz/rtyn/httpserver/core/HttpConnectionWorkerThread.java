package biz.rtyn.httpserver.core;

import biz.rtyn.httpserver.core.io.ReadFileException;
import biz.rtyn.httpserver.core.io.WebRootHandler;
import biz.rtyn.httpserver.core.io.WebRootNotFoundException;
import biz.rtyn.httpserver.http.HttpParser;
import biz.rtyn.httpserver.http.HttpParsingException;
import biz.rtyn.httpserver.http.HttpRequest;

import java.net.Socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpConnectionWorkerThread extends Thread {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String webroot;

    public HttpConnectionWorkerThread(Socket socket, String webroot) {
        this.socket = socket;
        this.webroot = webroot;
    }

    @Override
    public void run() {

        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpParser httpParser = new HttpParser();
            HttpRequest request = httpParser.parseHttpRequest(inputStream);

//            // Debug get entire request
//            int _byte;
//            while ((_byte = inputStream.read()) >= 0) {
//                System.out.print((char) _byte);
//            }

//            String relativePath = request.getRequestTarget();
//            System.out.println(relativePath);
//
//            WebRootHandler webRootHandler = new WebRootHandler(webroot);
//            String mimeType = webRootHandler.getFileMimeType(relativePath);
//            System.out.println(mimeType);
//
//            byte[] fileBytes = webRootHandler.getFileByteArrayData(relativePath);
//            System.out.println(fileBytes.length);

            String html = "<html><head><title>Simples!</title></head><body><h1>Hello HTTP Server</h1></body></html>";

            final String CRLF = "\r\n"; // 10, 13?

            String response =
                    "HTTP/1.1 200 OK" + CRLF + // status line: HTTP_VERSION RESP_CODE RESP_MESS
                            "Content-Type: text/html; charset=UTF-8" + CRLF +
                            "Content-Length: " + html.getBytes().length + CRLF + // Header
                            CRLF +
                            html;

            System.out.println("Sending response: " + CRLF + response);
            outputStream.write(response.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
            // TODO return 500? response
//        } catch (WebRootNotFoundException e) {
//            throw new RuntimeException(e);
//            // TODO return 500 response
        } catch (HttpParsingException e) {
            throw new RuntimeException(e);
            // TODO return 400 response
//        } catch (ReadFileException e) {
//            throw new RuntimeException(e);
//            // TODO return 400/500 response
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
