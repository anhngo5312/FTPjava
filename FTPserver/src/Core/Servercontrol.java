package Core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ngocanh
 */
public class Servercontrol {

    private final int Port = 9000;
    private final Serverview View;
    private final ServerSocket server;
   
    public Servercontrol() throws IOException {
        View = new Serverview();
        server = new ServerSocket(Port);
        int ClientNumber = 0;
        while (true) {
            System.out.println("cho ket noi ...");
            View.addLog("Cho ket noi ...\n");
            Socket client = server.accept();
            new ServiceThread(client, ClientNumber++, View).start();
        }
    }

    public static void main(String[] args) throws IOException, Exception {
        Servercontrol Server = new Servercontrol();
    }
}
