/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author tona
 */
public class Envio {

    public void enviarArchivo(File archivo, String destino) throws IOException {
        DatagramSocket cl = new DatagramSocket();
        DataInputStream dis = new DataInputStream(new FileInputStream(archivo.getAbsolutePath()));
        long tamArchivo = archivo.length();
        long enviado = 0;
        int fraccion;
        while (enviado < tamArchivo) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
            oos.flush();
            Archivo a = new Archivo();
            a.setTam(archivo.length());
            a.setNombre(archivo.getName());
            a.setRuta(destino);
            byte[] b = new byte[1500];
            fraccion = dis.read(b);
            a.setDatos(b);
            a.setBytesEnviados(fraccion);
            System.out.println("Bytes enviados: " + a.getBytesEnviados());
            oos.writeObject(a);
            oos.flush();
            byte data[] = baos.toByteArray();
            System.out.println("Data: " + data.length);
            DatagramPacket packet = new DatagramPacket(data, data.length,
                    InetAddress.getByName("localhost"), 5000);
            cl.send(packet);
            enviado += fraccion;
            oos.close();
            baos.close();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        cl.close();

    }
}
