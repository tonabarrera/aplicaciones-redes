package datagram.multicast;

import object.Objeto;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class RecibeUDP {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        if (args.length != 2) // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Multicast Addr> <Port>");

        InetAddress address = InetAddress.getByName(args[0]); // Multicast address
        if (!address.isMulticastAddress()) // Test if multicast address
            throw new IllegalArgumentException("Not a multicast address");

        int port = Integer.parseInt(args[1]); // Multicast port
        MulticastSocket sock = new MulticastSocket(port) ; // Multicast receiving socket
        sock.joinGroup(address); // Join the multicast group

        // Create and receive a datagram
        DatagramPacket packet = new DatagramPacket(
                new byte[6400], 6400);
        sock.receive(packet);

        ByteArrayInputStream baos = new ByteArrayInputStream(packet.getData());

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(baos));
        object.Objeto o2 = (Objeto) ois.readObject();
        System.out.println(o2.toString());

    }
}
