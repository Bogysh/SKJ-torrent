package files;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Socket clientSocket;
    static String send;
    static String receive;
    static int number;
    Split s = new Split();
    String arrsize;
    String repeat;
    boolean brepeat;

    public ClientHandler(Socket clientSocket, String send, String receive, int number) {
        this.clientSocket = clientSocket;
        this.send = send;
        this.receive = receive;
        this.number = number;
    }

    public void run() {
        try {

            InputStream sis1 = clientSocket.getInputStream();
            OutputStream sos1 = clientSocket.getOutputStream();
            InputStreamReader sisr1 = new InputStreamReader(sis1);
            OutputStreamWriter sosw1 = new OutputStreamWriter(sos1);
            BufferedReader br1 = new BufferedReader(sisr1);
            BufferedWriter bw1 = new BufferedWriter(sosw1);


            do {
/////wysylanie nazwy i wielkosci pliku
                java.io.File file = new java.io.File("C:\\TORrent_" + number + "\\" + send);
                System.out.println("Wysylam plik  " + send + " o rozmiarze " + file.length() + " kb");
                bw1.write(send + file.length());
                bw1.newLine();
                bw1.flush();

///wyslanie pliku
                FileInputStream fr = new FileInputStream("C:\\TORrent_" + number + "\\" + send);
                byte b[] = new byte[(int) file.length()];
                fr.read(b, 0, b.length);
                OutputStream os = clientSocket.getOutputStream();
                os.write(b, 0, b.length);

//////wysylam md5

                bw1.write(s.MD5(number, send));
                bw1.newLine();
                bw1.flush();

// otrzymanie informacji czy plik otrzymany i wyslany jest taki sam

                repeat = br1.readLine();
                if (repeat.equals("OK")) {
                    brepeat = false;
                } else
                    brepeat = true;

            } while (brepeat == true);

            /////////////////////////////////////////////////////czesc z zadaniem pliku ->

//wyslanie zadania z nazwa pliku
            do {
                System.out.println("A teraz zadam " + receive);
                bw1.write(receive);
                bw1.newLine();
                bw1.flush();

                arrsize = br1.readLine();

//zapisanie zadanego pliku
                byte d[] = new byte[(Integer.parseInt(arrsize))];
                InputStream is = clientSocket.getInputStream();
                FileOutputStream oss = new FileOutputStream("C:\\TORrent_" + number + "\\" + receive);
                is.read(d, 0, d.length);
                oss.write(d, 0, d.length);

///sprawdzenie i wyslanie sumy md5

                String recMD5 = br1.readLine();
                if (s.MD5(number, receive).equals(recMD5)) {
                    System.out.println((receive + " ma taka sasma wielkosc"));
                    brepeat = false;
                    bw1.write("OK");
                    bw1.newLine();
                    bw1.flush();
                } else {
                    brepeat = true;
                    bw1.write("REPEAT");
                    bw1.newLine();
                    bw1.flush();
                }
            } while (brepeat == true);

            System.out.println("END");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
