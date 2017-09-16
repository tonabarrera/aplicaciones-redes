package logica;

import interfaz.Chat;

import javax.swing.text.BadLocationException;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
        DatagramPacket p = new DatagramPacket(new byte[6400],6400);
        while(true) {
            try {
                System.out.println("Recibiendo informacion...");
                m.receive(p);
                Mensaje m = recuperarArchivo(p);
                Chat.agregarMensaje(m);
                if (m.getTipoMensaje() == Mensaje.ANUNCIO)
                    Chat.agregarUsuarioLista(m.getUsuario());
            } catch (IOException | ClassNotFoundException | BadLocationException e) {
                e.printStackTrace();
            }
            p.setLength(6400);
        }
    }

    private Mensaje recuperarArchivo(DatagramPacket packet) throws IOException, ClassNotFoundException {
        System.out.println("Datagrama recibido... extrayendo informacion");
        System.out.println(
                "Host remoto:" + packet.getAddress().getHostAddress() + ":" + packet.getPort());
        System.out.println("Datos del paquete:");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Mensaje a = (Mensaje) ois.readObject();
        System.out.println("Nombre: " + a.getUsuario());
        System.out.println("Tam: " + a.getMensaje());
        System.out.println("****************************************");
        ois.close();
        return a;
    }
}

