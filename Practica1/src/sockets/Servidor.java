/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tona
 */
public class Servidor {
    public static void main(String[] args) {
        int puerto = 9999;
        int n;
        try {
            ServerSocket s;
            s = new ServerSocket(puerto);
            s.setReuseAddress(true);
            System.out.println("Servicio iniciado...");
            for (; ; ) {
                System.out.println("Eperando conexion...");
                Socket cl = s.accept();
                System.out.format("Cliente conectado desde: %s:%s\n", cl.getInetAddress(),
                        cl.getPort());
                DataInputStream dis = new DataInputStream(cl.getInputStream());
                String nombre = dis.readUTF();
                long tam = dis.readLong();
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                long recibidos = 0;
                System.out.format("Escribiendo el archivo: %s\n", nombre);
                while (recibidos < tam) {
                    byte[] b = new byte[1500];
                    n = dis.read(b);
                    dos.write(b, 0, n);
                    dos.flush();
                    recibidos = recibidos + n;
                }
                System.out.println("¡Archivo recibido!\n");
                dos.close();
                dis.close();
                cl.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
