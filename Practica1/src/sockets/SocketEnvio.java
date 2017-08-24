/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author tona
 */
public class SocketEnvio {
    private final String host;
    private final int port;

    public SocketEnvio(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void sendFile(File file) throws IOException {
        Socket cl = new Socket(host, port);
        String nombre = file.getName();
        long tam = file.length();
        long enviados = 0;
        int porcentaje;
        int n;
        String ruta = file.getAbsolutePath();
        System.out.println("Conexion establecida");
        DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
        DataInputStream dis = new DataInputStream(new FileInputStream(ruta));
        
        dos.writeUTF(nombre);
        dos.flush();
        dos.writeLong(tam);
        dos.flush();
        System.out.format("Enviando el archivo: %s...", nombre);
        while (enviados < tam) {
            byte[] b = new byte[1500];
            n = dis.read(b);
            System.out.println("La otra n: " + n);
            dos.write(b, 0, n);
            dos.flush();
            enviados = enviados + n;
            porcentaje = (int) (enviados * 100 / tam);
            System.out.println("\rSe ha transmitido el: " + porcentaje + "% ...");
        }
        System.out.println("Archivo enviado");
        cl.close();
        dos.close();
        dis.close();
    }

}
