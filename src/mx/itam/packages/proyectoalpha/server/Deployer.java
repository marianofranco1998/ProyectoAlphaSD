package mx.itam.packages.proyectoalpha.server;

import mx.itam.packages.proyectoalpha.interfaces.Authenticate;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class Deployer {

    public static void main(String[] args) {

        // SETUP
        // Security Policy / Security Manager
        System.setProperty("java.security.policy", "src/mx/itam/packages/proyectoalpha/server/server.policy");
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // connectionData
        String IP = "localhost";
        String mcIP = "228.5.6.7";
        String mcPort = "49155";
        String TCPPort = "49152";

        // RMI and Multicast
        RMINode engine = null;
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            String name = "Authenticate";

            // RMI Things
            int maxScore = 4;
            engine = new RMINode(IP, mcIP, mcPort, TCPPort, maxScore);
            Authenticate stub = (Authenticate) UnicastRemoteObject.exportObject(engine, 0);
            registry.rebind(name, stub);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Multicast Thread Up
        assert engine != null;
        MCConnection mcThread = new MCConnection(engine);
        mcThread.start();

        // TCP Thread Loop
        try {
            ServerSocket listenSocket = new ServerSocket(Integer.parseInt(TCPPort));
            while (true) {
                Socket clientSocket = listenSocket.accept();
                TCPConnection c = new TCPConnection(engine, clientSocket);
                c.start();
            }
        } catch (IOException e) {
            System.out.println("Listen :" + e.getMessage());
        }
    }
}

class TCPConnection extends Thread {

    private RMINode engine;
    private DataInputStream in;
    private Socket clientSocket;
    private final String[] connectionData;

    public TCPConnection(RMINode engine, Socket aClientSocket) {
        // TCP
        this.engine = engine;
        try {
            this.clientSocket = aClientSocket;
            this.in = new DataInputStream(this.clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
        this.connectionData = engine.getConnectionData();
    }

    public synchronized void run() {
        String tcpMssg = null;
        String monster;
        int maxScore = engine.getMaxScore();
        try {
            tcpMssg = in.readUTF();

            System.out.println();
            System.out.println("Message: " + tcpMssg + ", received from: " + clientSocket.getRemoteSocketAddress());

            String[] parameters = tcpMssg.split("\\.");

            monster = parameters[1] + "." + parameters[2] + "." + parameters[3] + "." + "0000000000";

            if(engine.remove(monster)) {
                System.out.println("User: " + parameters[0] + " hit monster w/ id: " + tcpMssg + ".");
                if(engine.addScore(parameters[0])) {
                    sendWinner(parameters[0],maxScore);
                }
            }

        } catch (EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public synchronized void sendWinner(String user, int maxScore) {
        // Multicast
        MulticastSocket socket = null;
        try {
            InetAddress group = InetAddress.getByName(connectionData[1]);
            socket = new MulticastSocket(Integer.parseInt(connectionData[2]));
            socket.joinGroup(group);
            String winMessage = "*." + user + "." + maxScore + "." + engine.getRound()+ ".";
            byte[] m = winMessage.getBytes();
            DatagramPacket messageOut = new DatagramPacket(m, m.length, group, Integer.parseInt(this.connectionData[2]));
            socket.send(messageOut);
            engine.nextRound();
            socket.leaveGroup(group);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}

class MCConnection extends Thread {

    private final RMINode engine;
    private final String[] connectionData;
    private MulticastSocket socket;
    private InetAddress group;

    public MCConnection(RMINode engine) {
        this.engine = engine;
        this.connectionData = this.engine.getConnectionData();
        this.socket = null;
        try {
            this.group = InetAddress.getByName(this.connectionData[1]);
            this.socket = new MulticastSocket(Integer.parseInt(this.connectionData[2]));
            this.socket.joinGroup(this.group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        int currentRound = this.engine.getRound();
        int monNum = 1;
        int cell;
        String monster;
        Random rand = new Random();

        try {
            while(true) {
                if(currentRound != this.engine.getRound()) {
                    currentRound = this.engine.getRound();
                    monNum = 1;
                } else {
                    monNum += 1;
                }
                cell = rand.nextInt(9 - 1 + 1) + 1;

                monster = cell + "." + currentRound + "." + monNum + "." + "0000000000";
                byte[] m = monster.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, this.group, Integer.parseInt(connectionData[2]));
                this.socket.send(messageOut);
                System.out.println("Monster w/ id: " + monster + " sent.");
                this.engine.addMonster(monster);
                Thread.sleep(3000);
            }
            //this.socket.leaveGroup(group);
        } catch (IOException | InterruptedException e) {
                e.printStackTrace();
        } finally {
            if (socket != null) socket.close();
        }
    }
}