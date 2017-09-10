package datagram.eco;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;

/**
 * @author tona created on 07/09/2017 for SimpleSocket.
 */
public class Cliente {
    private static final int TIMEOUT = 3000; // milisegundos
    private static final int INTENTOS_MAX = 5;
    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args.length>3)
            throw new IllegalArgumentException("Parametros: <servidor> <palabra> <puerto>");
        InetAddress serverAddress = InetAddress.getByName(args[0]);
        byte[] bytesEnviados = args[1].getBytes(); // mensaje
        int servidorPuerto = (args.length == 3) ? Integer.parseInt(args[2]) : 7;
        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(TIMEOUT); // tiempo de bloqueo
        // Preparando el envio
        DatagramPacket paqueteEnviado = new DatagramPacket(bytesEnviados, bytesEnviados.length,
                serverAddress, servidorPuerto);
        // Preparando la respuesta
        DatagramPacket paqueteRecibido = new DatagramPacket(new byte[bytesEnviados.length],
                bytesEnviados.length);
        int intentos = 0; // Por si hay perdidas
        boolean respuesta = false;
        do {
            socket.send(paqueteEnviado); // envio
            try {
                socket.receive(paqueteRecibido);
                // Verificar el origen de la respuesta
                if (!paqueteRecibido.getAddress().equals(serverAddress))
                    throw new IOException("No se quien es");
                respuesta = true;
            } catch (InterruptedIOException e) {
                // No atrapamos nada
                intentos++;
                System.out.println("Time out, " + (INTENTOS_MAX - intentos) + " veces mas");
            }
        } while (!respuesta && intentos < INTENTOS_MAX);

        if (respuesta) System.out.println("Recibi: " + new String(paqueteRecibido.getData()));
        else System.out.println("Sin respuesta - me doy");

        socket.close();
    }
}
