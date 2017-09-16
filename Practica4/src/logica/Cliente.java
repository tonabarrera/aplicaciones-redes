package logica;

import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
        oos.flush();
        oos.writeObject(msj);
        oos.flush();
        byte[] datos = baos.toByteArray();
        DatagramPacket p = new DatagramPacket(datos, datos.length, grupo, 4445);
        m.send(p);
        System.out.println("Enviando");
        oos.close();
        baos.close();
    }

    public void enviarAnuncio(String nickname) {
        Mensaje anuncio = new Mensaje();
        anuncio.setTipoMensaje(Mensaje.ANUNCIO);
        Random rand = new Random();
        String r = Integer.toHexString(rand.nextInt(256));
        String g = Integer.toHexString(rand.nextInt(256));
        String b = Integer.toHexString(rand.nextInt(256));
        String color = r + g + b;

        System.out.println(color);
        anuncio.setUsuario(nickname);
        nickname = "<div style=\"color:#"+color+"\"><b>" + nickname + "</b> se ha conectado</div>";
        anuncio.setMensaje(nickname);
        try {
            enviarMensaje(anuncio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
