package eco;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author tona created on 19/08/2017 for SimpleSocket.
 */
public class Client {
    public static void main(String[] args) {
        try{
        //Stream para lectura de caracteres a través de la línea de comandos
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Escribe la direccion del servidor:");
            String host = br.readLine(), datos;
            int pto = 1234;
            //Socket de Flujo
            Socket cl = new Socket(host,pto);
        //Canales (manejo de caracteres)
            PrintWriter pu = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        //Conexión permanente del socket
            while(true){
                System.out.println("Escribe una cadena, <Enter> para enviar, \"salr\" para terminar");
                datos = br.readLine();
                if(datos.compareToIgnoreCase("salir") == 0){
                //Escritura - Writter (CharacterStream) -> OutputStream
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
                //Lectura - Reader (CharacterStream) -> InputStream
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
