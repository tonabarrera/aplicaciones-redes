package sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Servidor {
    private static final String DIRECCION = "230.1.1.1";
    private static final int PUERTO = 4000;
    private static final String SALUDO ="Hola te uniste al grupo =)";
    public static void main(String[] args) {
        try {
            MulticastSocket socket = new MulticastSocket(PUERTO);
            socket.setReuseAddress(true);
            socket.setTimeToLive(255);
            InetAddress grupo = InetAddress.getByName(DIRECCION);
            socket.joinGroup(grupo);
            System.out.printf("Uniendose al grupo %s Comienza el envio...\n", DIRECCION);
            for (;;){
                DatagramPacket paquete = new DatagramPacket(SALUDO.getBytes(), SALUDO.length(),
                        grupo, PUERTO);
                System.out.printf("Enviando mensaje %s con un TTL = %s\n",
                        SALUDO, socket.getTimeToLive());
                socket.send(paquete);
                try{
                    Thread.sleep(10000);
                }catch(InterruptedException ie){
                    System.out.println("Se murio el sleep");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
