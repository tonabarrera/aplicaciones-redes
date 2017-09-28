package logica;

import interfaz.Chat;

import java.io.*;

import javax.swing.text.BadLocationException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author tona created on 14/09/2017 for Practica4.
 */
public class Recibir implements Runnable, MulticastConstantes{
    private MulticastSocket m;
    private String carpeta;

    public Recibir(MulticastSocket multicastSocket, String usuario) {
        this.m = multicastSocket;
        this.carpeta = usuario;
    }

    @Override
    public void run() {
        DatagramPacket p = new DatagramPacket(new byte[TAM_BUFFER], TAM_BUFFER);
        while (true) {
            try {
                System.out.println("Recibiendo informacion...");
                m.receive(p);
                Mensaje mensaje = recuperarMensaje(p);
                if (mensaje.getTipoMensaje() == Mensaje.ANUNCIO) {
                    Chat.agregarMensaje(mensaje);
                    Chat.agregarUsuarioLista(mensaje.getUsuario());
                } else if (mensaje.getTipoMensaje() == Mensaje.LISTA_CONECTADOS) {
                    System.out.println("Recibiendo lista de conectados");
                    Chat.cargarListaConectados(mensaje.getConectados());
                } else if (mensaje.getTipoMensaje() == Mensaje.IMAGEN) {
                    String archivo = this.carpeta + "_" + mensaje.getImagen();
                    FileOutputStream fos = new FileOutputStream(archivo, true);
                    fos.write(mensaje.getDatos(), 0, mensaje.getEnviados());
                    fos.close();
                    File f = new File(archivo);
                    System.out.println(f.length() + " " + mensaje.getImgTam());
                    String clave = Enviar.obtenerClaveHash(f);
                    if (clave.equals(mensaje.getClave())) {
                        System.out.println("Mostar imagen");
                        mensaje.setImagen(f.getAbsolutePath());
                        Chat.agregarMensaje(mensaje);
                    } else
                        System.out.println("Aun no");
                }else
                    Chat.agregarMensaje(mensaje);

            } catch (IOException | ClassNotFoundException | BadLocationException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            p.setLength(TAM_BUFFER);
        }
    }
    
    private static Mensaje recuperarMensaje(DatagramPacket paquete) throws IOException, ClassNotFoundException {
        System.out.println("Datagrama recibido, extrayendo informacion...");
        System.out.printf("Host remoto: %s:%s\n", paquete.getAddress().getHostAddress(), paquete.getPort());
        System.out.println("Datos del paquete:");
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(paquete.getData()));
        Mensaje msj = (Mensaje) ois.readObject();
        System.out.println(msj.toString());
        System.out.println("**********************************************************************************");
        ois.close();
        return msj;
    }

}

