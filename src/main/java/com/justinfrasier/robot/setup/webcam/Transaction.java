package com.justinfrasier.robot.setup.webcam;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Transaction {

    private SyncList outputList;
    private DataOutputStream outputStream;

    public Transaction() {
        outputList = new SyncList();
    }

    public void add(Socket socket){
        try{
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.flush();
            outputList.addOutputStream(outputStream);
        }catch (IOException | NullPointerException e){}
    }

    public SyncList getList(){
        return outputList;
    }
}
