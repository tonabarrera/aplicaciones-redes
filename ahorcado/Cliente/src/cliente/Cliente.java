/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author tona
 */
public class Cliente {
    private static final int SOLICITAR_PALABRA = 1;
    private static final int CERRAR_CONEXION = 0;
    private static final int MAX_NUM_INTENTOS = 3;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        System.out.println("CLIENTE AHORCADO...");
        Socket cliente = new Socket("localhost", 7777);
        DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
        DataInputStream dis = new DataInputStream(cliente.getInputStream());
        dos.writeInt(SOLICITAR_PALABRA);
        dos.flush();
        byte palabra[] = new byte[1200];
        int n = dis.read(palabra);
        String nueva_palabra = new String(palabra, 0, n);
        System.out.println("----------COMIENZA EL JUEGO----------");
        jugar(nueva_palabra);
        dos.writeInt(CERRAR_CONEXION);
        dis.close();
        dos.close();
        cliente.close();
    }

    private static void jugar(String palabra) {
        Scanner scanner = new Scanner(System.in);
        char muestra[] = new char[palabra.length()];
        Arrays.fill(muestra, '_');
        int contador = palabra.length();
        boolean reducir;
        for (int i = 0; i < MAX_NUM_INTENTOS; i++) {
            reducir = false;
            System.out.println("---Intentos restantes: " + (MAX_NUM_INTENTOS-i) + "---");
            System.out.println(Arrays.toString(muestra));
            System.out.println("Ingresa una letra");
            char letra = scanner.nextLine().charAt(0);
            for (int j = 0; j < palabra.length(); j++) {
                if (palabra.charAt(j) == letra) {
                    if (muestra[j] == '_') {
                        muestra[j] = letra;
                        contador--;
                        reducir = true;
                    }
                }
            }
            if (reducir)
                i--;
            if (contador == 0){
                System.out.println("----------GANASTE----------");
                return;
            }
        }
        System.out.println("----------PERDISTE----------");
    }

}
