package com.justinfrasier.robot.setup.webcam;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio;

import java.io.IOException;

import static org.bytedeco.javacpp.opencv_videoio.CV_CAP_ANY;
import static org.bytedeco.javacpp.opencv_videoio.cvCreateCameraCapture;
import static org.bytedeco.javacpp.opencv_videoio.cvQueryFrame;

class StreamImage {

    private Transaction transaction;
    private SyncList list;
    private ImageConverter converter;
    private byte[] bytes;

    StreamImage(Transaction transaction) {
        this.transaction = transaction;
        converter = new ImageConverter();
    }

    void startLoop(){
        list = transaction.getList();
        Thread thread = new Thread(getRunnable());
        thread.start();
    }

    private Runnable getRunnable() {
        return () ->{
                opencv_videoio.CvCapture capture = webCam();

                opencv_core.IplImage frame;
            while (true) try {
                frame = cvQueryFrame(capture);
                //sleep(50);
                bytes = converter.IplImageToByteArray(frame);
                iterateThroughClients();
            } catch (Exception ignored) {}
            };
    }

    private void iterateThroughClients() throws InterruptedException {
        for (int i = 0; i < list.listSize(); i++) {
            try {
                pushImageDownStream(i);
            } catch (Exception e) {
                removeClientFromList(i);
            }
            ifListIsGreaterThenOneThenSleep(10);
        }
    }

    private void ifListIsGreaterThenOneThenSleep(int mill) throws InterruptedException {
        if(list.listSize() > 1) Thread.sleep(mill);
    }

    private void removeClientFromList(int i) {
        list.removeOutputStream(i);
        System.out.println("Client has left");
    }

    private void pushImageDownStream(int i) throws IOException {
        list.getOutputStream(i).writeInt(bytes.length);
        list.getOutputStream(i).write(bytes);
        list.getOutputStream(i).flush();
    }

    private opencv_videoio.CvCapture webCam() {

        return cvCreateCameraCapture(1);
    }
    private void sleep(long millis){
        try{
            Thread.sleep(millis);
        }catch (InterruptedException ignored){}
    }
}
