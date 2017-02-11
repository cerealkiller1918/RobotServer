package com.justinfrasier.robot.setup.webcam;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {

    private ServerSocket serverSocket;
    private Transaction transaction;
    private StreamImage streamImage;

    public void run(){
        try {
            SetupServerSocket();
            transaction = new Transaction();
            streamImage = new StreamImage(transaction);
            streamImage.startLoop();
            while (true) try {
                acceptClient();
            } catch (IOException ignored){}
        }catch (IOException ignored){}
    }

    private void acceptClient() throws IOException {
        transaction.add(getAccept());
        System.out.println("Client joined");
    }

    private Socket getAccept() throws IOException {
        return serverSocket.accept();
    }

    private void SetupServerSocket() throws IOException {
        serverSocket = new ServerSocket(444);
    }


}
