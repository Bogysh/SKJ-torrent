package files;

import java.io.*;
import java.net.Socket;
import java.util.Map;

class Info_handler implements Runnable {
    private Socket clientSocket;
    Data_Storage d = new Data_Storage();

    Info_handler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {


            InputStream sis = clientSocket.getInputStream();
            OutputStream sos = clientSocket.getOutputStream();
            InputStreamReader sisr = new InputStreamReader(sis);
            OutputStreamWriter sosw = new OutputStreamWriter(sos);
            BufferedReader br = new BufferedReader(sisr);
            BufferedWriter bw = new BufferedWriter(sosw);

            //stream do wysylania obiektow
            ObjectOutputStream mapOutputStream = new ObjectOutputStream(sos);


            //odebranie NR PORTU CLIENTA
            int clientport = Integer.parseInt(br.readLine());


            System.out.println("PODLACZONY " + clientSocket.getInetAddress() + " " + clientport);


            System.out.println("Lista otrzymanych plikow:");
            String data = "";

            //odebranie wielkosci tablicy
            data = br.readLine();
            int numfiles = Integer.parseInt(data);

            for (int i = 0; i < numfiles; i++) {
                data = br.readLine();
                System.out.println(data);
                d.all.put(data, clientport);
            }

            System.out.println();
            System.out.println("Lista wszystkich przechowywanych plikow");
            for (Map.Entry me : d.all.entrySet()) {
                System.out.println("FILE: " + me.getKey() + " & PORT: " + me.getValue());
            }
            System.out.println();
            System.out.println("============================================================");
            System.out.println();
            //  clientSocket.close();
///////////////////////////////////////////////////////////////////////////////////// wysylanie mapy


            //stream do wysylania obiektow
            mapOutputStream.writeObject(d.all);
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }
}



























