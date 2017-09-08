
package ares;

import java.net.*;
import java.io.*;

public class anuncia {
    public static void main(String[] args) throws IOException {
        try{
            String dir="235.1.1.1";
            InetAddress gpo = null;
            int pto = 8000;
            int pto1 = 8001;
            MulticastSocket s = new MulticastSocket(pto);
            s.setReuseAddress(true);
            s.setTimeToLive(255);
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
                DatagramPacket p = new DatagramPacket(b,b.length,gpo,pto1);
                s.send(p);
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException ie){
                    //nada
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
