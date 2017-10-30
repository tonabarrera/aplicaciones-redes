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

/**
 *
 * @author tona
 */
public class Cliente {
    static final int SOLICITAR_PALABRA = 1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        System.out.println("Cliente");
        Socket cliente = new Socket("localhost", 7777);
        DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
        DataInputStream dis = new DataInputStream(cliente.getInputStream());
        dos.write(SOLICITAR_PALABRA+'0');
        byte palabra[] = new byte[1200];
        int n = dis.read(palabra);
        System.out.println(new String(palabra, 0, n));
        dos.close();
        dis.close();
        cliente.close();
    }
    
}
