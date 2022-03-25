package mx.itam.packages.proyectoalpha.client;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {

    public void execute(String receive, String[] connectionData) {

        Socket s = null;

        try {
            s = new Socket(connectionData[0], Integer.parseInt(connectionData[3]));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            out.writeUTF(receive);

        } catch (UnknownHostException e) {
            System.out.println("Sock:" + e.getMessage());
        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if (s != null) try {
                s.close();
            } catch (IOException e) {
                System.out.println("close:" + e.getMessage());
            }
        }
    }

}
