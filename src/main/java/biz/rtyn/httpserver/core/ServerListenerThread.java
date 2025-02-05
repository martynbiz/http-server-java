package biz.rtyn.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    private int port;
    private String webroot;
    ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) {
        this.port = port;
        this.webroot = webroot;
    }

    @Override
    public void run() {

        try {

            this.serverSocket = new ServerSocket(port);

            while (serverSocket.isBound() && !serverSocket.isClosed()) {

                System.out.println("Listening on port: " + port);
                Socket socket = serverSocket.accept();

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket, webroot);
                workerThread.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
