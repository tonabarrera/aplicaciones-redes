package file;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * @author tona created on 19/08/2017 for SimpleSocket.
 */
public class Envia {
    public static void main(String args[]) {
        JFileChooser jf = new JFileChooser();
        int r = jf.showOpenDialog(null);
        if (r==JFileChooser.APPROVE_OPTION){
            File f = jf.getSelectedFile();
            String nombre = f.getName();
            long tam = f.length();
            long enviados = 0;
            String ruta = f.getAbsolutePath();
            String host = "localhost";
            int pto = 9999;
            int n;
            int porcentaje = 0;
            try {
                Socket cl = new Socket(host, pto);
                System.out.println("Conexion establecida, comienza envio del archivo: "+ruta);
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                DataInputStream dis = new DataInputStream(new FileInputStream(ruta));
                dos.writeUTF(nombre);
                dos.flush();
                dos.writeLong(tam);
                dos.flush();
                while (enviados<tam) {
                    byte[] b = new  byte[1500];
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    enviados = enviados + n;
                    porcentaje = (int) (enviados*100/tam);
                    System.out.println("\r Se ha transmitido el: " + porcentaje + "%");
                }
                System.out.println("Archivo enviado");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
