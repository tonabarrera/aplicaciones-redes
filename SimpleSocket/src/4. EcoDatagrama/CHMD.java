package datagram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author tona created on 24/08/2017 for SimpleSocket.
 */
public class CHMD {
    public static void main(String args[]) {
        try {
            // No importa el puerto ya que el server optiene el puerto con getPort()
            DatagramSocket cl = new DatagramSocket();
            System.out.println("Escribe la direccion del servidor");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String host = br.readLine();
            String saludo = "Un saludo en datagrama";
            //Datagrama tamaño maximo 6500
            byte[] b = saludo.getBytes();
            // Especificamos la IP destino y al puerto 5000 de UDP
            DatagramPacket p = new DatagramPacket(b, b.length,
                    InetAddress.getByName(host), 5000);
            cl.send(p);
            // Recibimos la respuesta
            DatagramPacket p2 = new DatagramPacket(new byte[1500], 1500);
            cl.receive(p2);
            br.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
