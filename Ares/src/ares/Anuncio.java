
package ares;

import java.net.*;
/**
 *
 * @author escom
 */
public class Anuncio {
    public static void main(String[] args ){
        try{
            /**
             * An instance of an InetAddress consists of an:
             *  IP address and possibly its corresponding host name 
             * Types:
             *  1 - Unicast 
             *  2 - Multicast
             * */
            InetAddress direccionGrupo=null;
            try{
                direccionGrupo = InetAddress.getByName("228.1.1.1");
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }
            /**
             * MulticastSocket:
             * Is a (UDP) DatagramSocket, with additional capabilities 
             * for joining "groups" of other multicast hosts on the internet
             * 
             * Is specified by:
             *   - A class D IP address
             *   - A standard UDP port number
             * 
             * Class D IP addresses are in the range:
             *   -224.0.0.0 to 239.255.255.255, inclusive
             */
            MulticastSocket multicastSocket= new MulticastSocket(9876);
            multicastSocket.setReuseAddress(true);
            multicastSocket.setTimeToLive(128);
            multicastSocket.joinGroup(direccionGrupo);
            
            String mensaje ="hola";
            byte[] bytesMensaje = mensaje.getBytes();
            
            for(;;){
                /**
                 * Datagram Packet
                 */
                DatagramPacket datagramPacket = 
                        new DatagramPacket(bytesMensaje,bytesMensaje.length,direccionGrupo,9999);
                
                multicastSocket.send(datagramPacket);
                System.out.println("Enviando mensaje "+mensaje
                        + " con un TTL= "+ multicastSocket.getTimeToLive());
                try{
                    Thread.sleep(3000);
                }catch(InterruptedException ie){}
            }//for
        }catch(Exception e){
            
        }//catch
    }//main
}