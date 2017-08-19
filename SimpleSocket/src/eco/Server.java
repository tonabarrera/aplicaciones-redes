package eco;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author tona created on 19/08/2017 for SimpleSocket.
 */
public class Server {
    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Servicio iniciado. Esperando clientes...");
            while(true){
                Socket nw = ss.accept();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(nw.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(nw.getInputStream()));
                while(true){
                    String mensaje = br.readLine();
                    if(mensaje.compareToIgnoreCase("salir") == 0){
                        pw.close();
                        br.close();
                        nw.close();
                        break;
                    }
                    else{
                        String eco = mensaje + "-> eco";
                        pw.println(eco);
                        pw.flush(); //el flush es debido a que a veces los mensajes se pierden en la pila de la tarjeta de red, con esto garantizamos que si envie
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
