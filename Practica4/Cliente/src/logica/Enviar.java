package logica;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;
import static logica.MulticastConstantes.TAM_BUFFER;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */

public class Enviar implements MulticastConstantes{
    private MulticastSocket socket;
    private InetAddress grupo;
    private int puerto;
    private Random random;

    public Enviar(MulticastSocket multicastSocket, InetAddress grupo, int puerto) {
        this.socket = multicastSocket;
        this.grupo = grupo;
        this.puerto = puerto;
        this.random = new Random();
    }
    
    public void enviarMensaje(Mensaje msj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TAM_BUFFER);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
        oos.flush();
        oos.writeObject(msj);
        oos.flush();
        byte[] datos = baos.toByteArray();
        DatagramPacket paquete = new DatagramPacket(datos, datos.length, grupo, puerto);
        socket.send(paquete);
        System.out.println("Enviando");
        oos.close();
        baos.close();
    }

    public void enviarAnuncio(String nickname) {
        Mensaje anuncio = new Mensaje();
        anuncio.setTipoMensaje(Mensaje.ANUNCIO);
        String r = Integer.toHexString(random.nextInt(256));
        String g = Integer.toHexString(random.nextInt(256));
        String b = Integer.toHexString(random.nextInt(256));
        String color = r + g + b;

        System.out.println(color);
        anuncio.setUsuario(nickname);
        nickname = "<div style=\"color:#" + color + "\"><b>" + nickname + "</b> se ha conectado</div>";
        anuncio.setMensaje(nickname);
        try {
            enviarMensaje(anuncio);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarImagen(File imagen, Mensaje msj) throws IOException {
        long tam = imagen.length();
        long leidos = 0;
        int n;
        DataInputStream dis = new DataInputStream(new FileInputStream(imagen.getAbsolutePath()));
        while(leidos < tam) {
            Mensaje mensaje = new Mensaje();
            mensaje.setTipoMensaje(Mensaje.IMAGEN);
            mensaje.setMensaje(msj.getMensaje());
            mensaje.setImagen(imagen.getName());
            mensaje.setImgTam(tam);
            mensaje.setUsuario(msj.getUsuario());
            byte[] b = new byte[5000];
            n = dis.read(b);
            mensaje.setDatos(b);
            mensaje.setEnviados(n);
            enviarMensaje(mensaje);
            leidos += n;
        }
        dis.close();
    }
}
