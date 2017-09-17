package logica;

import interfaz.Chat;

import javax.swing.text.BadLocationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Servidor implements Runnable {
    private MulticastSocket m;

    public Servidor(MulticastSocket multicastSocket) {
        this.m = multicastSocket;
    }

    @Override
    public void run() {
        DatagramPacket p = new DatagramPacket(new byte[6400], 6400);
        while (true) {
            try {
                System.out.println("Recibiendo informacion...");
                m.receive(p);
                Mensaje mensaje = recuperarArchivo(p);
                Chat.agregarMensaje(mensaje);
                if (mensaje.getTipoMensaje() == Mensaje.ANUNCIO)
                    Chat.agregarUsuarioLista(mensaje.getUsuario());
            } catch (IOException | ClassNotFoundException | BadLocationException e) {
                e.printStackTrace();
            }
            p.setLength(6400);
        }
    }

    private Mensaje recuperarArchivo(DatagramPacket p) throws IOException, ClassNotFoundException {
        System.out.println("Datagrama recibido, extrayendo informacion...");
        System.out.printf("Host remoto: %s:%s", p.getAddress().getHostAddress() ,p.getPort());
        System.out.println("Datos del paquete:");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(p.getData()));
        Mensaje msj = (Mensaje) ois.readObject();
        System.out.println(msj.toString());
        System.out.println("******************************************************");
        ois.close();
        return msj;
    }
}

