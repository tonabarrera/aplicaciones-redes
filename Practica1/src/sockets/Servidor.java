package sockets;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tona
 */
public class Servidor {
    private static final int PUERTO = 9999;

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(PUERTO);
        s.setReuseAddress(true);
        System.out.println("Servicio iniciado...");
        for (; ; ) {
            System.out.println("Eperando conexion...");
            Socket cl = s.accept();
            System.out.format("Cliente conectado desde: %s:%s\n", cl.getInetAddress(),
                    cl.getPort());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            String ruta = dis.readUTF();
            String nombre = dis.readUTF();

            nombre = crearDirectorio(nombre, ruta);
            escribirArchivo(nombre, dis);

            System.out.println("¡Archivo recibido!\n");
            dis.close();
            cl.close();
        }

    }

    private static String crearDirectorio(String nombre, String ruta) {
        // Verificamos si el archivo se crea en la raiz o en alguna carpeta
        if (!ruta.equals("")) {
            File directorio = new File(ruta); // Directorio de almacenamiento
            // Si no existe lo creamos
            if (!directorio.exists()) {
                try {
                    if (directorio.mkdir()) System.out.println("Carpeta creada");
                    else System.out.println("No se pudo crear la carpeta");
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }
            // La ubicacion final de nuestro archivo
            nombre = ruta + "\\" + nombre;
        }
        return nombre;
    }

    private static void escribirArchivo(String nombre, DataInputStream dis) {
        System.out.format("Escribiendo el archivo: %s\n", nombre);
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
            long recibidos = 0;
            long tam = dis.readLong();
            int n;
            while (recibidos < tam) {
                byte[] buffer = new byte[1500];
                n = dis.read(buffer);
                dos.write(buffer, 0, n);
                dos.flush();
                recibidos += n;
            }
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
