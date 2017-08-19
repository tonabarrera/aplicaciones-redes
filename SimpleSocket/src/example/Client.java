package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by tona on 15/08/2017 for SimpleSocket.
 */
public class Client {
    public static void main(String args[]) {
        try {
            Socket socketClient = new Socket("localhost", 1234);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socketClient.getInputStream()));
            String line = br.readLine();
            System.out.println(line);
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
