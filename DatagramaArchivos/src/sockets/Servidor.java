/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author tona
 */
public class Servidor {
    public static int PUERTO = 9980;
    private static final int TAM_PAQUETE = 6500;
    private static Hashtable<String, DataOutputStream> tablini = new Hashtable<>();
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        DatagramSocket socket = new DatagramSocket(PUERTO);
        DatagramPacket paqueteRecibido = new DatagramPacket(new byte[TAM_PAQUETE], TAM_PAQUETE);

        System.out.println("Servidor iniciado...");
        for (; ; ) {
            DataOutputStream dos;
            System.out.println("Recibiendo informacion...");
            socket.receive(paqueteRecibido);
            Archivo archivo = recuperarArchivo(paqueteRecibido);
            String destino = archivo.getRuta();
            String ruta = archivo.getNombre();
            if (!destino.equals("")) {
                File nuevo = new File(destino);
                if (!nuevo.exists()) {
                    try {
                        if (nuevo.mkdir()) System.out.println("carpeta creada");
                        else System.out.println("No se pudo crear la carpeta");
                    } catch (SecurityException se) {
                        se.printStackTrace();
                    }
                }
                ruta = destino + "\\" + ruta;
            }
            File f = new File(ruta);
            if (f.exists()) {
                dos = tablini.get(archivo.getClave());
                if (dos != null) {
                    System.out.println("Recuperando el dos");
                    if (dos.size() >= archivo.getTam()) {
                        dos.close();
                        System.out.println("-----------------CERRO------------------------");
                    } else System.out.println("SIGUE ESCRIBIENDO");
                }
            } else {
                dos = new DataOutputStream(new FileOutputStream(ruta));
                tablini.put(archivo.getClave(), dos);
            }
            if (dos != null) {
                dos.write(archivo.getDatos(), 0, archivo.getBytesEnviados());
                dos.flush();
                System.out.println("En dos: " + dos.size() + " tam: " + archivo.getTam());
                if (dos.size() >= archivo.getTam()) {
                    System.out.println("CERRANDO TODO ALV");
                    dos.close();
                    tablini.remove(archivo.getClave());
                }
            } else {
                System.out.println("-------Por alguna razon es null");
            }
            // Esto solo es de adorno para que no me marque error el editor
            if (archivo.getNombre().equals("FIN")) break;

            socket.send(paqueteRecibido);
            paqueteRecibido.setLength(TAM_PAQUETE);
        }
        socket.close();
    }

    private static Archivo recuperarArchivo(
            DatagramPacket packet) throws IOException, ClassNotFoundException {
        System.out.println("Datagrama recibido... extrayendo informacion");
        System.out.println(
                "Host remoto:" + packet.getAddress().getHostAddress() + ":" + packet.getPort());
        System.out.println("Datos del paquete:");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Archivo a = (Archivo) ois.readObject();
        System.out.println("Nombre: " + a.getNombre());
        System.out.println("Tam: " + a.getTam());
        System.out.println("Bytes recibidos: " + a.getBytesEnviados());
        System.out.println("****************************************");
        ois.close();
        return a;
    }
}
