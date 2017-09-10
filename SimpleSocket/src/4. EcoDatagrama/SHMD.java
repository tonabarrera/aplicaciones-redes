package datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author tona created on 24/08/2017 for SimpleSocket.
 */
public class SHMD {
    public static void main(String args[]) {
        try {
            //inetaddress es del que genero
            int puerto = 5000;
            DatagramSocket s = null;
            s = new DatagramSocket(puerto);
            System.out.println("Esperando datagramas...");
            for (;;) {
                DatagramPacket p = new DatagramPacket(new byte[1500], 1500);
                s.receive(p);
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
