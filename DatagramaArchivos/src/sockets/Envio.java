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
    private static final int TIMEOUT = 3000; // milisegundos
    private static final int INTENTOS_MAX = 5;
    private static final int PUERTO = 9980;

    public void enviarArchivo(File archivo, String destino) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        DataInputStream dis = new DataInputStream(new FileInputStream(archivo.getAbsolutePath()));
        InetAddress servidor = InetAddress.getByName("localhost");

        long tamArchivo = archivo.length();
        long enviado = 0;
        int fraccion;

        socket.setSoTimeout(TIMEOUT);
        while (enviado < tamArchivo) {
            int intentos = 0;
            boolean respuesta = false;
            Archivo a = new Archivo(archivo.getName(), destino, archivo.length());

        //baos: Regresa un objeto como un arreglo de bytes, ya que un socket datagrama solo envía arreglos de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
            oos.flush();
        //Llenando el objeto a envíar
            byte[] b = new byte[4000]; // Maximo se van a leer 4000 bytes del archivo
            fraccion = dis.read(b); // bytes que se leyeron
            a.setDatos(b);
            a.setBytesEnviados(fraccion);
            System.out.println("Bytes leidos en el buffer de lectura: " + a.getBytesEnviados());
        //Escribiendo el objeto en el Stream de Bytes
            oos.writeObject(a);
            oos.flush();
        //Creando un arreglo de bytes[] con el Stream de Bytes que contiene a nuestro objeto    
            byte[] datos = baos.toByteArray();
            System.out.println("Tam paquete buffer: " + datos.length);
            DatagramPacket paqueteEnvio = new DatagramPacket(datos, datos.length, servidor, PUERTO);
            DatagramPacket paqueteConfirmacion = new DatagramPacket(new byte[datos.length],
                    datos.length);

            do {
                socket.send(paqueteEnvio);
                try {
                    socket.receive(paqueteConfirmacion);
                    if (!paqueteConfirmacion.getAddress().equals(servidor))
                        throw new IOException("No se quien es");
                    respuesta = true;
                } catch (InterruptedIOException e) {
                    intentos++;
                    System.out.println("Time out, " + (INTENTOS_MAX - intentos) + " veces mas");
                }
            } while (!respuesta && intentos < INTENTOS_MAX);

            if (respuesta) System.out.println("Recibi la confirmacion del servidor continuando...");
            else System.out.println("Sin respuesta - me doy");

            enviado += fraccion;
            oos.close();
            baos.close();
//            try {
//                Thread.sleep(15);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        System.out.println("Cerrando todo...");
        socket.close();
        dis.close();
    }
}
