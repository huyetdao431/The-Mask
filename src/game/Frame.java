package game;

import javax.swing.*;

public class Frame extends JFrame {
    public Frame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tomb of The Mask");
        Panel gamePanel = new Panel();
        add(gamePanel);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}