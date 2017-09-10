package address;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author tona created on 03/09/2017 for SimpleSocket.
 */
public class InetAddressExample {
    public static void main(String args[]) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Local Host:");
            System.out.println("\t" + address.getHostName());
            System.out.println("\t" + address.getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("Unable to determine this host's address");
        }

        // Get name(s)/address(es) of hosts given on command line
        for (String arg : args) {
            try {
                InetAddress[] addressList = InetAddress.getAllByName(arg);
                System.out.println(arg + ":");
                // Print the first name. Assume array contains at least one entry.
                System.out.println("\t" + addressList[0].getHostName());
                for (InetAddress anAddressList : addressList)
                    System.out.println("\t" + anAddressList.getHostAddress());
            } catch (UnknownHostException e) {
                System.out.println("Unable to find address for " + arg);
            }
        }
    }
}
