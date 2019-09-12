package files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Split {

    public static String matchMD5(String s) {
        Pattern p = Pattern.compile("(?<= ).*");
        Matcher m = p.matcher(s);

        if (m.find()) {
            return m.group(0);
        } else {
            return "nie";
        }
    }

    public static String matchN(String s) {
        Pattern p = Pattern.compile(".*\\.txt");
        Matcher m = p.matcher(s);

        if (m.find()) {
            return m.group(0);
        } else {
            return "nie";
        }
    }


    public static String checkSize(String s) {
        Pattern p = Pattern.compile("txt(.*)$");
        Matcher m = p.matcher(s);

        if (m.find()) {
            return m.group(1);
        } else {
            return "nie";
        }
    }

    //Tworzenie sumy MD5
    static String MD5(int number, String name) {
        File f = new File("C:\\TORrent_" + number + "\\" + name);
        String checksumm = null;
        try {
            MessageDigest md5Digestt = MessageDigest.getInstance("MD5");
            checksumm = getFileChecksum(md5Digestt, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checksumm;
    }

    //funkcja potrzebna do stworzenia MD5
    static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        //input stream do odczytania zawartosci
        FileInputStream fis = new FileInputStream(file);

        //Create byte array to read data in chunks
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        ;

        //close the stream; We don't need it now.
        fis.close();

        //Get the hash's bytes
        byte[] bytes = digest.digest();

        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        //return complete hash
        return sb.toString();
    }
}


