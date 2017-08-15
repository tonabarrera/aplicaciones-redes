import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tona on 15/08/2017 for SimpleSocket.
 */
public class Server {
    public static void main(String args[]) {
        try {
            String message = "Hola";
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Esperando...");
            Socket socket = new Socket();
            socket = ss.accept();
            System.out.println("Conectado");
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            bos.write(message.getBytes());
            bos.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
