package file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tona created on 19/08/2017 for SimpleSocket.
 */
public class Recibe {
    public static void main(String args[]) {
        int puerto = 9999;
        int n;
        try {
            ServerSocket s = null;
            s = new ServerSocket(puerto);
            s.setReuseAddress(true);
            System.out.println("Servicio iniciado");
            for(;;) {
                Socket cl = s.accept();
                System.out.println("Cliente conectado desde: " + cl.getInetAddress() + " : " + cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                String nombre = dis.readUTF();
                long tam = dis.readLong();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                long recibidos = 0;
                while (recibidos<tam) {
                    byte[] b = new byte[1500];
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    recibidos = recibidos + n;
                }
                System.out.println("Archivo recibido");
                dos.close();
                dis.close();
                cl.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
