package com.justinfrasier.robot.setup.webcam;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(Master master) throws InterruptedException {
        Thread.sleep(500);
        this.setTitle("Server");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(650,515);
        this.setVisible(true);
        JPanel panel = new VideoPanel(master);
        panel.repaint();
        this.add(panel);
        while(true){
            panel.repaint();
        }
    }

}
