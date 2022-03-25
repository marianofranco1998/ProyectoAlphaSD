package mx.itam.packages.proyectoalpha.guis;

import mx.itam.packages.proyectoalpha.client.TCPClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GameGUI extends JFrame implements ActionListener {

    private final String user;
    private final String[] connectionData;
    private final TCPClient clientPlayer;
    private int currentRound;
    private int currentMonster;

    public GameGUI(String user, String[] connectionData){
        super("Alpha Game");
        this.user = user;
        this.connectionData = connectionData;
        this.clientPlayer = new TCPClient();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        placeComponents();
    }

    public void placeComponents() {
        setSize(300, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel grid = new JPanel(new GridLayout(0,3,10,10));
        add(grid);

        for(int i = 1; i < 10; i++){
            JCheckBox c = new JCheckBox("Celda " + i);
            c.addActionListener(this);
            c.setActionCommand(String.valueOf(i));
            grid.add(c);
        }
        setVisible(true);
    }

    public void placeComponents(int j, int currentRound, int monster) {
        this.currentMonster = monster;
        this.currentRound = currentRound;
        setSize(300, 300);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel grid = new JPanel(new GridLayout(0,3,10,10));
        add(grid);

        for(int i = 1; i < 10; i++){
            JCheckBox c = new JCheckBox("Celda " + i,i==j);
            if(i==j) {
                System.out.println("prendemos la celda" + i);
            }
            c.addActionListener(this);
            c.setActionCommand(String.valueOf(i));
            grid.add(c);
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(int i = 1; i < 10; i++) {
            if(Objects.equals(String.valueOf(e.getActionCommand()), String.valueOf(i))) {
                //JCheckBox thisOne = (JCheckBox) e.getSource();
                //if(!thisOne.isSelected()) {
                System.out.println("We send " + i + " through TCP to " + connectionData[0] + ", Port: " + connectionData[3]);
                clientPlayer.execute(user + "." + i + "." + currentRound + "." + currentMonster+ "." + "0000000000", connectionData);
                //}
            }
        }
    }
}
