package files;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Client_Server {

    private final static String NAME = "CLIENT";
    private static ClientHandler client_handler;
    private static ServerHandler server_handler;
    private static Thread thread;
    static HashMap<String, Integer> filemap;
    static Split s = new Split();

    private static void log(String message) {
        System.out.println(NAME + ": " + message);
        System.out.flush();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        int number = Integer.parseInt(args[0]);
        int port = Integer.parseInt(args[1]);
        String send = args[2];
        String wantget = args[3];
        /*int number = 1;
        int port = 10002;
        String send = "Toronto.txt";
        String wantget = "Mieczyslaw.txt";*/

        final int SERVER_PORT = port;


        log("URUCHOMIONY CLIENT " + number);
        ServerSocket welcomeSocket = new ServerSocket(SERVER_PORT);
        log("CZEKAM");

        String serverName = "localhost";
        int serverPort = 10001;
        InetAddress serverAddress = InetAddress.getByName(serverName);


//socket do polaczen
        Socket clientSocket = new Socket(serverAddress, serverPort);
        InputStream sis = clientSocket.getInputStream();
        OutputStream sos = clientSocket.getOutputStream();
        InputStreamReader sisr = new InputStreamReader(sis);
        OutputStreamWriter sosw = new OutputStreamWriter(sos);
        BufferedReader br = new BufferedReader(sisr);
        BufferedWriter bw = new BufferedWriter(sosw);

//do przeslanielisty
        PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        //do odebrania obiektu
        ObjectInputStream mapInputStream = new ObjectInputStream(sis);

//zebranie plikow z folderu
        String directory = "C:\\TORrent_" + number;
        File file = new File(directory);
        File[] list = file.listFiles();

//wyslanie nr portu
        bw.write(Integer.toString(SERVER_PORT));
        bw.newLine();
        bw.flush();
//podanie wielkosci listy plikow
        bw.write(Integer.toString(list.length));
        bw.newLine();
        bw.flush();

        log("Lista posiadanych plikow w folderze");
        System.out.println();
        String t = "";
        for (int j = 0; j < list.length; j++) {
            t = list[j].getName();


            //stworzenie sumy MD5
            File file_directory = new File(directory + "\\" + t);
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            String checksum = s.getFileChecksum(md5Digest, file_directory);

            System.out.println(t + " " + checksum);
            outToClient.println(t + " " + checksum);
            outToClient.flush();
        }
////////////////////////////////////////////////////////////////////////////////////odebranie mapy
        bw.write("give data");
        bw.newLine();
        bw.flush();
        try {
            filemap = (HashMap) mapInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
        log("WYSWIETLANIE LISTY PLIKOW POBRANYCH Z SERWERA ");
        System.out.println();
        for (Map.Entry me : filemap.entrySet()) {
            System.out.println("Key: " + me.getKey() + " & Value: " + me.getValue());
        }
        clientSocket.close();
        System.out.println();
        System.out.println();
        ////////////////////////////////////////////////////////////////////////////////

//wyswietlenie plikow otrzymanych od serwera
        for (Map.Entry me : filemap.entrySet()) {
            String s = me.getKey().toString();
            String m = me.getValue().toString();
            int mm = Integer.parseInt(m);


            if (matchName(s).equals(wantget)) {
                log("Lacze sie z innym klientem-serwerem");

                clientSocket = new Socket(serverAddress, mm);
                client_handler = new ClientHandler(clientSocket, send, wantget, number);
                thread = new Thread(client_handler);
                thread.start();
            }
        }
        log("ZMIENIAM SIE W SERWER");
//klient staje sie teraz serwerem
        try {
            while (true) {
                server_handler = new ServerHandler(welcomeSocket.accept(), number);
                thread = new Thread(server_handler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String matchName(String s) {
        Pattern p = Pattern.compile(".*\\.txt");
        Matcher m = p.matcher(s);

        if (m.find()) {
            return m.group(0);
        } else {
            return "nie";
        }
    }
}
















































