package mx.itam.packages.proyectoalpha.client;

import mx.itam.packages.proyectoalpha.guis.GameGUI;
import mx.itam.packages.proyectoalpha.guis.LoginGUI;
import mx.itam.packages.proyectoalpha.guis.WinGUI;
import mx.itam.packages.proyectoalpha.interfaces.Authenticate;
import mx.itam.packages.proyectoalpha.server.RMINode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientLauncher {


    public static String[] clientRMI(String user, String port) {

        // Security Policy / Security Manager
        System.setProperty("java.security.policy","src/mx/itam/packages/proyectoalpha/client/client.policy");

        if(System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // Register RMI and return connectionData
        String name = "Authenticate";
        Registry registry= null;
        try {
            registry = LocateRegistry.getRegistry(Integer.parseInt(port));
            Authenticate auth= (Authenticate) registry.lookup(name);

            return auth.authenticate(user);
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public static void clientGo(String user, String[] connectionData) {
        // Star Multicast Listen Thread
        MCClientThread goMc = new MCClientThread(user, connectionData);
        goMc.start();

    }

    public static void main(String[] args) {
        // Start login GUI
        LoginGUI login = new LoginGUI();

    }
}

class MCClientThread extends Thread {

    private final String user;
    private final String[] connectionData;

    public MCClientThread(String user, String[] connectionData) {
        //Constructors
        this.user = user;
        this.connectionData = connectionData;
    }

    public void run() {
        // Initialize values for receiving multicast messages
        InetAddress group;
        MulticastSocket mcSocket = null;

        // Multicast Setup
        try {
            group = InetAddress.getByName(connectionData[1]);
            mcSocket = new MulticastSocket(Integer.parseInt(connectionData[2]));
            mcSocket.joinGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Game begins
        byte[] buffer = new byte[1000];
        boolean gameIsGoing = true;
        DatagramPacket messageIn = null;
        String message;
        String[] parameters;

        GameGUI game = new GameGUI(this.user, this.connectionData);
        WinGUI winner = new WinGUI();

        while(true) {
            try {
                messageIn = new DatagramPacket(buffer, buffer.length);
                assert mcSocket != null;
                mcSocket.receive(messageIn);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message = new String(messageIn.getData()).trim();
            parameters = message.split("\\.");

            System.out.println(message);
            if(!parameters[0].equals("*")) {
                game.placeComponents(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
            } else {
                winner.placeComponents(parameters[1], parameters[2], parameters[3]);
            }
        }
        //mcSocket.leaveGroup(group);
    }
}