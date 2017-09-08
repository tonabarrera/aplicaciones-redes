package datagram.eco;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author tona created on 07/09/2017 for SimpleSocket.
 */
public class Servidor {
    private static final int ECHO_MAX = 255; // tam. del datagrama
    public static void main(String args[]) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("Parametro: <puerto>");
        int puerto = Integer.parseInt(args[0]);
        DatagramSocket socket = new DatagramSocket(puerto);
        DatagramPacket packet = new DatagramPacket(new byte[ECHO_MAX], ECHO_MAX);
        // Para hacer el eco
        for (;;) {
            socket.receive(packet);
            System.out.println("Cliente: " + packet.getAddress().getHostAddress() + " en puerto: " +
                    "" + packet.getPort());
            socket.send(packet); // MAKUUUU!
            // Reiniciamos la longitud para evitar que se redusca el buffer
            packet.setLength(ECHO_MAX);

        }
    }
}
