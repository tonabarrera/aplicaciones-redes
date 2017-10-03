
package ares;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;
/**
 *
 * @author escom
 */
public class Anuncio {
    /**
     * Estados en los que se encuentra el programa:
     * Wait: Ignora los {@link Paquete}s con el tipo Envio o Retransmición, y acepta el tipo solicitud
     * Envío: Escucha unicamente los {@link Paquetes}s con el tipo Retransmición
     * Recepción: Escucha unicamente los {@link Paquete}s con el tipo Envío
     */
    private static final int MODO_WAIT = 1;
    private static final int MODO_ENVIO = 2;
    private static final int MODO_RECEPCION = 3;
    private static final int MODO_SOLICITUD = 4;
    private static final int RESPUESTA = 5;
    private static final int RETRANSMICION = 6;
    private static final int TAM_PAQUETE = 6500;
    
    private static MulticastSocket multicastSocket;
    private static final File archivosFolder = new File(System.getProperty("user.dir")+"/archivos");
    private static  File archivo;
    
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
            multicastSocket = new MulticastSocket(9999);
            multicastSocket.setReuseAddress(true);
            multicastSocket.setTimeToLive(128);
            multicastSocket.joinGroup(direccionGrupo);
            Scanner sc = new Scanner(System.in);
            int estado = MODO_WAIT;
            int minimoRetransmicion;
            
            System.out.println("Unido al grupo");
            for(;;){
                //TODO: sacar esto
                System.out.println("pedir?");
                estado = sc.nextInt();
                //
                if(estado == MODO_WAIT){
                    System.out.println("MODO: wait");
                    DatagramPacket datagramaBuscador = new DatagramPacket(new byte[TAM_PAQUETE], TAM_PAQUETE);
                    multicastSocket.receive(datagramaBuscador);
                    
                    Paquete recibido = leerMensaje(datagramaBuscador);
                //Solo hacer caso a las solicitudes si el cliente en modo wait
                    if(recibido.getTipo() == MODO_SOLICITUD){
                        String nombreArchivo = new String(datagramaBuscador.getData()); 
                        System.out.println("Buscando: "+nombreArchivo);
                        if(buscarArchivo(nombreArchivo)){
                            System.out.println("Encontre archivo");
                            DatagramPacket datagramaRespuesta = crearMensaje(MODO_WAIT, 0, null, direccionGrupo, null, null);
                            multicastSocket.send(datagramaRespuesta);
                            estado = MODO_ENVIO;
                        }
                    }
                }else if(estado == MODO_SOLICITUD){
                //Pidiendo el archivo
                    System.out.println("MODO: Solicitud, nombre archivo:");
                    String nombreArchivo = sc.nextLine();
                    DatagramPacket datagramPacket = crearMensaje(MODO_SOLICITUD,0,null,direccionGrupo,nombreArchivo,null);
                    multicastSocket.send(datagramPacket);
                    try{ Thread.sleep(1000); }catch(InterruptedException ie){}
                //Esperado respuesta de los demás clientes
                    DatagramPacket respuestaClientes = new DatagramPacket(new byte[TAM_PAQUETE], TAM_PAQUETE);
                    multicastSocket.receive(respuestaClientes);
                    Paquete paquete = leerMensaje(respuestaClientes);
                //Se encontro el archivo, se prepara su recpeción
                    if(paquete.getTipo() == RESPUESTA){
                        System.out.println("Alguien lo encontro");
                        estado = MODO_RECEPCION;
                        archivo = new File(nombreArchivo);
                    }else{
                        System.out.println("Nadie lo tiene");
                        //TODO: mostrar menu
                    }
                }else if(estado == MODO_ENVIO){
                    System.out.println("MODOD: Envío");
                //Envio archivo
                    
                }else if(estado == MODO_RECEPCION){
                    System.out.println("MODO: Recepción");
                    
                }
            }//for
        }catch(IOException | ClassNotFoundException e){
            
        }//catch
    }//main
    
    /**
     * @param 
     * @return DatagramPacket
     */
    private static DatagramPacket crearMensaje(
            int tipo, int numero, byte[] bytesArchivo, InetAddress direccionGrupo, String archivo, String carpeta) 
            throws IOException{
        DatagramPacket datagramPacket;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(baos));
        byte [] b = new byte[6400];
        //Llenando paquete
        Paquete paquete = new Paquete(tipo);
        switch(tipo){
            case MODO_WAIT:
                paquete.setTipo(RESPUESTA);
            case MODO_RECEPCION:
                paquete.setTipo(RETRANSMICION); //se solicita una retransmición
            case MODO_SOLICITUD:
                paquete.setNombreArchivo(archivo);
                paquete.setTipo(MODO_WAIT); //modo que lo recibirá
            case MODO_ENVIO:
                paquete.setTipo(MODO_RECEPCION);
                paquete.setNombreArchivo(archivo);
                paquete.setNombreCarpeta(carpeta);
                paquete.setBytes(bytesArchivo);
                paquete.setNumero(numero);
        }
        //Escribiendo objeto en ByteArrayOutputStream
        oos.writeObject(paquete);
        oos.flush();
        //Serializando objeto escrito
        b = baos.toByteArray();
        //Creando datagrama con el objeto
        datagramPacket = new DatagramPacket(b, b.length, direccionGrupo, 9999);
        return datagramPacket;
    }
    /**
     * @param 
     * @return 
     */
    private static Paquete leerMensaje(DatagramPacket datagramPacket) throws IOException, ClassNotFoundException{
        Paquete paquete;
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(datagramPacket.getData()));
        paquete = (Paquete)ois.readObject();
        System.out.println("Paquete: "+paquete.toString());
        return paquete;
    }
    private static boolean buscarArchivo(String nombreArchivo){
        boolean encontrado =  false;
        File[] listOfFiles = archivosFolder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println("archivo: "+file.getName());
                if(file.getName().equals(nombreArchivo.trim())){
                    encontrado = true;
                    archivo = file;
                }
            }
        }
        return encontrado;
    }
}