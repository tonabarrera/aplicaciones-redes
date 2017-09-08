package datagram.multicast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class EnviaUDP {
    public static void main(String args[]) throws IOException {
        if (args.length < 2 || args.length > 3)
            throw new IllegalArgumentException("Parametros: <multicast address> <port> <ttl>");
        InetAddress destino = InetAddress.getByName(args[0]);
        if (!destino.isMulticastAddress())
            throw new IllegalArgumentException("No es multicast");

        int puertoDestino = Integer.parseInt(args[1]);
        int TTL; // tiempo de vida del datagrama

        if (args.length == 3)
            TTL = Integer.parseInt(args[2]);
        else
            TTL = 1;

        MulticastSocket socket = new MulticastSocket(); // socket que envia
        Objeto o = new Objeto(1, "hola", true);
        socket.setTimeToLive(TTL);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
        oos.flush();
        oos.writeObject(o);
        oos.flush();
        byte[] data = baos.toByteArray();

        DatagramPacket msj = new DatagramPacket(data, data.length, destino, puertoDestino);
        socket.send(msj);
        socket.close();
    }
}
