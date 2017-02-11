package com.justinfrasier.robot.setup.webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class VideoPanel extends JPanel{

    BufferedImage image;
    Master master;

    public VideoPanel(Master master){
         this.master = master;
    }
    @Override
    protected void paintComponent(Graphics g){
        updateImage();
        super.paintComponent(g);
        g.drawImage(image,0,0,null);
        System.out.println("Painting");
    }

    public void updateImage(){
        image = master.getBufferImage();
    }
}
