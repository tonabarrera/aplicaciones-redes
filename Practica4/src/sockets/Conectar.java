package sockets;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Conectar {
    private MulticastSocket s;
    private static final String dir="230.1.1.1";
    private static final int PUERTO = 4000;
    InetAddress grupo = null;

    public Conectar() throws IOException {
        s = new MulticastSocket(PUERTO);
        s.setReuseAddress(true);
        grupo = InetAddress.getByName(dir);
        s.joinGroup(grupo);
        mandarMensaje("Ya llegue");
    }

    private void mandarMensaje(String msg) throws IOException {
        DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), grupo, PUERTO);
        s.send(packet);
    }
}
