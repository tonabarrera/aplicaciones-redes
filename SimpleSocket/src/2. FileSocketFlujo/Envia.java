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
        //Params archivo
            File f = jf.getSelectedFile();
            String nombre = f.getName();
            long tam = f.length();
            long enviados = 0;
            String ruta = f.getAbsolutePath();
        //Params socket
            String host = "localhost";
            int pto = 9999;
            int n;
            int porcentaje = 0;
            try {
            //Socket de flujo
                Socket cl = new Socket(host, pto);
                System.out.println("Conexion establecida, comienza envio del archivo: "+ruta);
            //Stream for primitive data (byte[] en este caso)
                DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            //Stream for read primitive data (from a file in this case)
                DataInputStream dis = new DataInputStream(new FileInputStream(ruta));
            //Enviando params del archivo para que el servidor lo cree
                dos.writeUTF(nombre);
                dos.flush();
                dos.writeLong(tam);
                dos.flush();
            //Contenido del archivo
                while (enviados<tam) {
                    byte[] b = new  byte[1500]; //datos primitivos a enviar
                    n = dis.read(b); //Leyendo bytes del archivo, se guardan en b, y n recibe el numero de bytes leidos
                //Envío socket
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
