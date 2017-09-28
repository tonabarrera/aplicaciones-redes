package ares;

import java.net.*;
import java.net.MulticastSocket;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author escom
 */
public class Descubre {
    public static void main(String[] args ){
        InetAddress gpo=null;
        int pto =9999;
        try{
            MulticastSocket cl= new MulticastSocket(9999);
            System.out.println("Cliente escuchando puerto "+ cl.getLocalPort());
            cl.setReuseAddress(true);
            try{
                gpo = InetAddress.getByName("228.1.1.1");
            }catch(UnknownHostException u){
                System.err.println("Direccion no valida");
            }//catch
            cl.joinGroup(gpo);
            System.out.println("Unido al grupo");
            for(;;){
                DatagramPacket p = new DatagramPacket(new byte[10],10);
                cl.receive(p);
                System.out.println("Datagrama recibido..");
                String msj = new String(p.getData());  
                System.out.println("Servidor descubierto: "+p.getAddress());
               
            }//for
            
        }catch(Exception e){
            
        }//catch
    }//main
}