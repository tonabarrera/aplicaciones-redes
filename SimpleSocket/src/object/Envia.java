package object;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author tona created on 19/08/2017 for SimpleSocket.
 */
public class Envia {
    public static void main(String args[]) {
        try {
            Socket cl = new Socket("localhost", 7777);
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
            Objeto o = new Objeto("Juan", 20, 3000f);
            oos.writeObject(o);
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            Objeto o2 = (Objeto) ois.readObject();
            System.out.println("Objecto Recibido");
            System.out.println("Nombre: " + o2.getNombre() + " edad: " + o2.getEdad()
                    + " sueldo:" + o2.getSueldo());
            ois.close();
            oos.close();
            cl.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
