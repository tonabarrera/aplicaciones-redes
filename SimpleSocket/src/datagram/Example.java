package datagram;

/**
 * @author tona created on 24/08/2017 for SimpleSocket.
 */
public class Example {
    /*
    * Clase DatagramPacket
    *
    * Constructores:
    * Para recibir
    * DatagramPacket(byte[] b, int t)
    * Para enviar
    * DatagramPacket(byte []b, int t, InetAddress dst, int pto)
    * DatagramPacket(byte[] b, int t, SocketAddress dst)
    *
    * clase DatagramSocket
    *
    * Constructores:
    * crea y asoca al primer puerto que encuentre
    * DatagramSocket()
    * setBroadcast(boolean) // enable a la opcion de broadcast
    * getInetAddress y getPort() soolo funciona si use el metodo connect(InetAddress, int)
    * getSoTimeout() & setSoTimeout(int) temporizador  para cerrar el socket despues de un tiempo
    * */
}
