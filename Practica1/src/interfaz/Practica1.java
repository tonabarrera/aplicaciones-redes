/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

/**
 * @author tona
 */
public class Practica1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Envia iniciado");
        JFileChooser jf = new JFileChooser();
        jf.setMultiSelectionEnabled(true);
        int r = jf.showOpenDialog(null);
        if (r == JFileChooser.APPROVE_OPTION) {
            String host = "localhost";
            int pto = 9999;
            int n;

            File files[] = jf.getSelectedFiles();
            for (File f : files) {
                String nombre = f.getName();
                long tam = f.length();
                long enviados = 0;
                int porcentaje;

                String ruta = f.getAbsolutePath();
                try {
                    Socket cl = new Socket(host, pto);
                    System.out.println("Conexion establecida");
                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    DataInputStream dis = new DataInputStream(new FileInputStream(ruta));
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();
                    System.out.println("Enviando el archivo: " + nombre);
                    while (enviados < tam) {
                        byte[] b = new byte[1500];
                        n = dis.read(b);
                        System.out.println("La otra n: " + n);
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados = enviados + n;
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.println("\rSe ha transmitido el: " + porcentaje + "%");
                    }
                    System.out.println("Archivo enviado");
                    cl.close();
                    dos.close();
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
