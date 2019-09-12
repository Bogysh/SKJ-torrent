package files;

import java.io.*;
import java.net.ServerSocket;

public class Info_Server {
    private final static int SERVER_PORT = 10001;
    private final static String NAME = "INFO";
    private static Info_handler info_handler;
    private static Thread thread;

    private static void log(String message) {
        System.out.println(NAME + ": " + message);
        System.out.flush();
    }

    public static void main(String[] args) throws IOException {
        log("URUCHOMIONY");
        ServerSocket welcomeSocket = new ServerSocket(SERVER_PORT);
        log("CZEKAM NA INFORMACJE");

        try {
            while (true) {
                info_handler = new Info_handler(welcomeSocket.accept());
                thread = new Thread(info_handler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





