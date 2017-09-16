package logica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Servidor implements Runnable{
    private MulticastSocket m;

    public Servidor(MulticastSocket multicastSocket) {
        this.m = multicastSocket;
    }

    @Override
    public void run() {
        System.out.println(m.isBound());
        while(true) {
            DatagramPacket p = new DatagramPacket(new byte[350],350);
            try {
                System.out.println("Recibiendo informacion...");
                m.receive(p);
                String mensaje = new String (p.getData(),0, p.getLength());
                System.out.printf("El mensaje " + mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
