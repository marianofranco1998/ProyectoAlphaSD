package mx.itam.packages.proyectoalpha.server;

import mx.itam.packages.proyectoalpha.interfaces.Authenticate;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class RMINode implements Authenticate {
    public HashMap<String, ArrayList<String>> lookup;
    public String[] connectionData;
    public int round = 1;
    public ArrayList<String> monsters;
    private final int maxScore;

    public RMINode(String IP, String mcIP, String mcPort, String TCPPort, int maxScore) throws RemoteException {
        super();
        this.lookup = new HashMap<>();
        this.connectionData =  new String[]{IP, mcIP, mcPort, TCPPort};
        this.maxScore = maxScore;
        this.monsters = new ArrayList<>();
    }

    @Override
    public String[] authenticate(String user) throws RemoteException {
        ArrayList<String> player = this.lookup.get(user);           // Look for user

        if (player == null) {                                       // If user not in Hashmap then
            ArrayList<String> newPlayer = new ArrayList<>();
            newPlayer.add(user);
            this.lookup.put(user,newPlayer);
            for(int i = 1; i < round; i++) {
                newPlayer.add("0");                               // User "did not play" in round i
            }
            newPlayer.add("0");
            this.lookup.put(user, newPlayer);                       // We add new a player to Hashmap
        }
        return this.connectionData;                             // We return player connection data
    }

    public void nextRound() {
        this.round += 1;
    }

    public boolean addScore(String user) {
        ArrayList<String> player = this.lookup.get(user);
        int currentScore;
        if (this.round > player.size() - 1) {
            for(int i = 0; i < this.round - player.size() + 2; i++) {
                player.add("0");
            }
            currentScore = 1;
            player.add(this.round,Integer.toString(currentScore));
        } else {
            currentScore = Integer.parseInt(player.get(this.round));
            currentScore += 1;
            player.set(this.round,Integer.toString(currentScore));
        }
        System.out.println(user + ": " + currentScore + " on round: " + round);
        return (currentScore >= this.maxScore);
    }

    public String[] getConnectionData() {
        return connectionData;
    }

    public int getRound() {
        return round;
    }

    public void addMonster(String monster) {
        this.monsters.add(monster);
    }

    public boolean remove(String monster) {
        int index = this.monsters.indexOf(monster);
        if (index >= 0) {
            this.monsters.remove(index);
            return true;
        } else {
            return false;
        }
    }

    public int getMaxScore() {
        return maxScore;
    }
}
