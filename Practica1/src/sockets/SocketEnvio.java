/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.*;
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

    public void enviarArchivo(File file, String destino) throws IOException {
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

        dos.writeUTF(destino); // Usamos destino para ir almacenando la ruta del archivo/carpeta
        dos.flush();
        dos.writeUTF(nombre);
        dos.flush();
        dos.writeLong(tam);
        dos.flush();

        System.out.format("Enviando el archivo: %s...\n", nombre);
        System.out.format("Que esta en la ruta: %s\n", ruta);

        while (enviados < tam) {
            byte[] b = new byte[1500];
            n = dis.read(b);
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

    // el parametro destino nos permite guardar la ubicacion del archivo
    public void enviarCarpetas(File carpeta, String destino) throws IOException {
        System.out.format("Carpeta %s con los archivos:\n", carpeta.getName());

        if (destino.equals("")) destino = carpeta.getName(); // evita que se cree en c:\\
        else destino = destino + "\\" + carpeta.getName(); // manejar la ruta de los archivos

        for (File file : carpeta.listFiles()) {
            if (file.isDirectory()) enviarCarpetas(file, destino);
            else enviarArchivo(file, destino);
        }
    }

}
