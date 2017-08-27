package tareaeco;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author tona created on 24/08/2017 for Servidor.
 */
public class Cliente {
    private static int BUFFER=10;
    public static void main(String[] args) {
        try{
            DatagramSocket socketCliente =  new DatagramSocket();
            System.out.println("Direccion del servidor: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String msj;
            int i;
            int paquetes;
            int restante,j; 
            byte[] bytesMensaje;
            do{
                System.out.println("Escribe: ");
                msj = br.readLine();
                bytesMensaje = msj.getBytes();
                paquetes = bytesMensaje.length/BUFFER;
                restante = bytesMensaje.length % BUFFER;
                for(i=0,j=0; j<paquetes;i=i+BUFFER, j++){
                    DatagramPacket datagrama = 
                            new DatagramPacket(bytesMensaje, 
                                               i, 
                                               BUFFER, 
                                               InetAddress.getByName("localhost"),
                                               7000);
                    socketCliente.send(datagrama);
                    DatagramPacket datagramaRespuesta = 
                            new DatagramPacket(new byte[1500], 1500);
                    socketCliente.receive(datagramaRespuesta);
                    System.out.println("Respuesta desde: "
                            +datagramaRespuesta.getAddress()
                            +":"+datagramaRespuesta.getPort()
                            +" con:"+new String(
                                    datagramaRespuesta.getData(),
                                    0,
                                    datagramaRespuesta.getLength()));
                    System.out.println(i);
                } 
                System.out.println(bytesMensaje.length);
                if(restante!=0){
                    DatagramPacket datagrama = 
                            new DatagramPacket(bytesMensaje, 
                                               bytesMensaje.length-restante, 
                                               restante, 
                                               InetAddress.getByName("localhost"),
                                               7000);
                    socketCliente.send(datagrama);
                    DatagramPacket datagramaRespuesta = 
                            new DatagramPacket(new byte[1500], 1500);
                    socketCliente.receive(datagramaRespuesta);
                    System.out.println("Respuesta desde: "
                            +datagramaRespuesta.getAddress()
                            +":"+datagramaRespuesta.getPort()
                            +" con:"+new String(
                                    datagramaRespuesta.getData(),
                                    0,
                                    datagramaRespuesta.getLength()));
                }
            }while(msj.compareToIgnoreCase("salir")!=0);
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
