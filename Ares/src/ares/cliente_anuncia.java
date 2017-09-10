
package ares;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class cliente_anuncia {
    public static void main(String[] args) throws IOException {
        try{
            String dir="235.1.1.1";
            InetAddress gpo = null;
            int pto = 8000;
            int pto1 = 8001;
            MulticastSocket s = new MulticastSocket(pto);
            s.setReuseAddress(true);
            //s.setTimeToLive(255);
            try{
                gpo = InetAddress.getByName(dir);
            }catch(UnknownHostException u){
                u.printStackTrace();
            }
            s.joinGroup(gpo);
            System.out.println("Servicio iniciado y unido al grupo: "+dir+"\nComienza envio de anuncios");
            String anuncio="anuncio";
            byte[]b=anuncio.getBytes();
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[65535],65535);
                s.receive(p);
                String mensaje = new String (p.getData(),0,p.getLength());
                if (mensaje.compareTo(anuncio)==0) {
                    System.out.println("Servidor encontrado en "+p.getAddress());
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
