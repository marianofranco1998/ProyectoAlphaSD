package mx.itam.packages.proyectoalpha.guis;

import javax.swing.*;
import java.awt.*;

public class WinGUI extends JFrame {

    public JLabel userLabel;
    public JLabel scoreLabel;

    public WinGUI() {
        super("Alpha Winner");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        setSize(300, 100);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        add(panel);

        panel.setLayout(null);
        this.userLabel = new JLabel("");
        this.userLabel.setBounds(10,20,280,25);
        panel.add(this.userLabel);

        this.scoreLabel = new JLabel("");
        this.scoreLabel.setBounds(10,40,200,25);
        panel.add(this.scoreLabel);
    }

    public void placeComponents(String user, String maxScore, String round) {
        userLabel.setText("Winner of round #" + round + " : " + user);
        scoreLabel.setText("Score: " + maxScore);

        setVisible(true);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
    }
}
