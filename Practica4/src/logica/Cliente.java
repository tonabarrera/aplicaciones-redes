package logica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Cliente {
    private MulticastSocket m;
    private InetAddress grupo;

    public Cliente(MulticastSocket multicastSocket, InetAddress grupo) {
        this.m = multicastSocket;
        this.grupo = grupo;
    }

    public void enviarMensaje(Mensaje msj) throws IOException {
        DatagramPacket p = new DatagramPacket(msj.getMensaje().getBytes(), msj.getMensaje().length(), grupo, 8000);
        m.send(p);
        System.out.println("Enviando");
    }
}
