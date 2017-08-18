package c.eco.java;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/*
    Este es un cliente de eco
*/

public class CEcoJava {

    public static void main(String[] args) {
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Escribe la direccion del servidor:");
            String host = br.readLine(), datos;
            int pto = 1234;
            Socket cl = new Socket(host,pto);
            PrintWriter pu = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            while(true){
                System.out.println("Escribe una cadena, <Enter> para enviar, \"salr\" para terminar");
                datos = br.readLine();
                if(datos.compareToIgnoreCase("salir") == 0){
                    pu.println(datos);
                    pu.flush();
                    System.out.println("Termina aplicacion");
                    pu.close();
                    br2.close();
                    br.close();
                    cl.close();
                    System.exit(0);
                }
                else{
                    pu.println(datos);
                    pu.flush();
                    System.out.println("Preparado para recibir eco:");
                    String eco = br2.readLine();
                    System.out.println("Eco:" + eco);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            //aqui podriamos invocar al metodo main de nuevo
        }
    }
    
}
