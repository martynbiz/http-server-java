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