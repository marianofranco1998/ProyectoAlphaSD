package mx.itam.packages.proyectoalpha.guis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static mx.itam.packages.proyectoalpha.client.ClientLauncher.*;

public class LoginGUI extends JFrame {

    public LoginGUI() {
        super("Alpha Login");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        placeComponents();
    }

    private void placeComponents() {
        setSize(350, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        add(panel);

        panel.setLayout(null);
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        JLabel portLabel = new JLabel("RMI Port: ");
        portLabel.setBounds(10,50,80,25);
        panel.add(portLabel);

        JTextField portText = new JTextField(20);
        portText.setBounds(100,50,165,25);
        panel.add(portText);

        JButton loginButton = new JButton("login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String port = portText.getText();
                String[] connectionData = clientRMI(user, port);

                clientGo(user, connectionData);
                setVisible(false);
            }
        });

        panel.add(loginButton);
        setVisible(true);
    }
}
