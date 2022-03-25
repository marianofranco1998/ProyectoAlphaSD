package mx.itam.packages.proyectoalpha.guis;

import javax.swing.*;
import java.awt.*;

public class WinGUI extends JFrame {

    public WinGUI() {
        super("Alpha Winner");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
    }

    public void placeComponents(String user, String maxScore, String round) {
        setSize(300, 100);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        add(panel);

        panel.setLayout(null);
        JLabel userLabel = new JLabel("Winner of round #" + round + " : " + user);
        userLabel.setBounds(10,20,280,25);
        panel.add(userLabel);

        JLabel scoreLabel = new JLabel("Score: " + maxScore);
        scoreLabel.setBounds(10,40,200,25);
        panel.add(scoreLabel);

        setVisible(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
    }
}
