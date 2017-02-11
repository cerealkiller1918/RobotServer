package com.justinfrasier.robot.setup.webcam;

import java.io.DataOutputStream;
import java.util.ArrayList;

public class SyncList {

    private ArrayList<DataOutputStream> streams;

    public SyncList() {
        streams = new ArrayList<>();
    }

    synchronized DataOutputStream getOutputStream(int index){
        return streams.get(index);
    }
    synchronized void addOutputStream(DataOutputStream stream){
        streams.add(stream);
    }
    synchronized void removeOutputStream(int index){
        streams.remove(index);
    }
    synchronized int listSize(){
        return streams.size();
    }
}

