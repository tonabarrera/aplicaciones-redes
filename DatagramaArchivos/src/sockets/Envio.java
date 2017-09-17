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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        FileInputStream fis = new FileInputStream(archivo.getAbsolutePath());
        String clave = "ERROR";
        try {
            clave = obtenerClaveHash(fis);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        DataInputStream dis = new DataInputStream(new FileInputStream(archivo.getAbsolutePath()));
        InetAddress servidor = InetAddress.getByName("localhost");

        long tamArchivo = archivo.length();
        long enviado = 0;
        int fraccion;

        socket.setSoTimeout(TIMEOUT);
        while (enviado < tamArchivo) {
            int intentos = 0;
            boolean respuesta = false;

            Archivo a = new Archivo(archivo.getName(), destino, archivo.length(), clave);

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

    public void enviarCarpetas(File carpeta, String destino) throws IOException {
        System.out.format("Carpeta %s con los archivos:\n", carpeta.getName());

        if (destino.equals("")) destino = carpeta.getName(); // evita que se cree en c:\\
        else destino = destino + "\\" + carpeta.getName(); // concatenar la ruta de los archivos
        for (File file : carpeta.listFiles()) {
            if (file.isDirectory()) enviarCarpetas(file, destino);
            else enviarArchivo(file, destino);
        }
    }

    public String obtenerClaveHash(FileInputStream fis) throws NoSuchAlgorithmException{
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        byte[] datos = new byte[1024];
        int leidos;
        try {
            while ((leidos = fis.read(datos)) != -1) {
                sha1.update(datos, 0, leidos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] hashBytes = sha1.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
