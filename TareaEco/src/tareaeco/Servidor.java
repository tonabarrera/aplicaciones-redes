/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareaeco;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * @author tona
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //inetaddress es del que genero
            int puerto = 7000;
            DatagramSocket s = null;
            String msg = "salir";
            s = new DatagramSocket(puerto);
            System.out.println("Esperando datagramas...");
            while (true) {
                DatagramPacket p = new DatagramPacket(new byte[1500], 1500);
                s.receive(p);
                msg = new String(p.getData());
                if (msg.compareToIgnoreCase("salir") == 0) {
                    s.close();
                    break;
                }
                System.out.println("Datagrama recibido desde: " + p.getAddress() + ":"+p.getPort() +
                        " con el mensaje: " + new String(p.getData(), 0, p.getLength()));
                System.out.println("Se devuelve saludo");
                s.send(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
