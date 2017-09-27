package logica;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class Servidor implements MulticastConstantes {
    private static ArrayList<String> conectados = new ArrayList<>();
    private static InetAddress grupo;
    private static MulticastSocket multicastSocket;
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            grupo = InetAddress.getByName(DIRECCION);
            multicastSocket = new MulticastSocket(PUERTO);
            multicastSocket.joinGroup(grupo);
            multicastSocket.setReuseAddress(true);
            DatagramPacket paquete = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
            System.out.println("Servidor iniciado");
            while (true) {
                System.out.println("Recibiendo informacion en el servidor...");
                multicastSocket.receive(paquete);
                Mensaje msj =  recuperarMensaje(paquete);
                System.out.println(msj.toString());
                if (msj.getTipoMensaje() == Mensaje.ANUNCIO){
                    System.out.println("Anuncio recibido...");
                    conectados.add(msj.getUsuario());
                    Mensaje respuesta = new Mensaje();
                    respuesta.setConectados(conectados);
                    respuesta.setTipoMensaje(Mensaje.LISTA_CONECTADOS);
                    enviarMensaje(respuesta);
                    System.out.println("Lista de conectados enviados");
                }
                paquete.setLength(TAM_BUFFER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected static Mensaje recuperarMensaje(DatagramPacket paquete) throws IOException, ClassNotFoundException {
        System.out.println("Datagrama recibido, extrayendo informacion...");
        System.out.printf("Host remoto: %s:%s", paquete.getAddress().getHostAddress(), paquete.getPort());
        System.out.println("Datos del paquete:");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(paquete.getData()));
        Mensaje msj = (Mensaje) ois.readObject();
        System.out.println(msj.toString());
        System.out.println("******************************************************");
        ois.close();
        return msj;
    }

    protected static void enviarMensaje(Mensaje msj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TAM_BUFFER);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
        oos.flush();
        oos.writeObject(msj);
        oos.flush();
        byte[] datos = baos.toByteArray();
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, grupo, PUERTO);
        multicastSocket.send(paquete);
        System.out.println("Enviando");
        oos.close();
        baos.close();
    }
}
