package files;

import java.io.*;
import java.net.Socket;

class ServerHandler implements Runnable {
    private Socket clientSocket;
    int number;
    Split s = new Split();
    boolean brepeat;
    String repeat;
    String filename;

    ServerHandler(Socket clientSocket, int number) {
        this.clientSocket = clientSocket;
        this.number = number;
    }


    public void run() {
        try {

            InputStream sis = clientSocket.getInputStream();
            OutputStream sos = clientSocket.getOutputStream();
            InputStreamReader sisr = new InputStreamReader(sis);
            OutputStreamWriter sosw = new OutputStreamWriter(sos);
            BufferedReader br = new BufferedReader(sisr);
            BufferedWriter bw = new BufferedWriter(sosw);

            System.out.println("PODLACZONY " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
            System.out.println();
            System.out.println();

// odebranie nazwy i rozmiaru pliku
            do {
                String filename = br.readLine();

//zapisanie odebranego pliku
                System.out.println("Odbieram plik..." + s.matchN(filename) + " o rozmiarze " + s.checkSize(filename));
                byte d[] = new byte[Integer.parseInt(s.checkSize(filename))];
                InputStream is = clientSocket.getInputStream();
                FileOutputStream os = new FileOutputStream("C:\\TORrent_" + number + "\\" + s.matchN(filename));
                is.read(d, 0, d.length);
                os.write(d, 0, d.length);

/////////tworzenie sumy md5
                s.MD5(number, s.matchN(filename));
                String recMD5 = br.readLine();

                if (s.MD5(number, s.matchN(filename)).equals(recMD5)) {
                    brepeat = false;
                    System.out.println((s.matchN(filename) + " ma taka sama wielkosc"));
                    bw.write("OK");
                    bw.newLine();
                    bw.flush();
                } else {
                    brepeat = true;
                    bw.write("REPEAT");
                    bw.newLine();
                    bw.flush();
                }
            } while (brepeat == true);

///////czesc z wysylaniem pliku który by żądany ->
            do {
                filename = br.readLine();

                java.io.File file = new java.io.File("C:\\TORrent_" + number + "\\" + filename);
                System.out.println("Wysylam plik  " + filename);

/////przeslanie wielkosci pliku
                bw.write(Integer.toString((int) file.length()));
                bw.newLine();
                bw.flush();

///wyslanie pliku
                FileInputStream fr = new FileInputStream("C:\\TORrent_" + number + "\\" + filename);
                byte b[] = new byte[(int) file.length()];
                fr.read(b, 0, b.length);
                OutputStream oss = clientSocket.getOutputStream();
                oss.write(b, 0, b.length);

///wysłanie MD5
                bw.write(s.MD5(number, filename));
                bw.newLine();
                bw.flush();

                repeat = br.readLine();
                if (repeat.equals("OK")) {
                    brepeat = false;
                } else
                    brepeat = true;

            } while (brepeat == true);


                System.out.println("END");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
