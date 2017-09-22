package logica;

import interfaz.Chat;
import java.io.ByteArrayInputStream;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Recibir implements Runnable, MulticastConstantes{
    private MulticastSocket m;

    public Recibir(MulticastSocket multicastSocket) {
        this.m = multicastSocket;
    }

    @Override
    public void run() {
        DatagramPacket p = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
        while (true) {
            try {
                System.out.println("Recibiendo informacion...");
                m.receive(p);
                Mensaje mensaje = recuperarMensaje(p);
                Chat.agregarMensaje(mensaje);
                if (mensaje.getTipoMensaje() == Mensaje.ANUNCIO)
                    Chat.agregarUsuarioLista(mensaje.getUsuario());
            } catch (IOException | ClassNotFoundException | BadLocationException e) {
                e.printStackTrace();
            }
            p.setLength(6400);
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

}

