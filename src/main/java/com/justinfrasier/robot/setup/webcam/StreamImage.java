package com.justinfrasier.robot.setup.webcam;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio;

import static org.bytedeco.javacpp.opencv_videoio.CV_CAP_ANY;
import static org.bytedeco.javacpp.opencv_videoio.cvCreateCameraCapture;
import static org.bytedeco.javacpp.opencv_videoio.cvQueryFrame;

public class StreamImage {

    private Transaction transaction;
    private SyncList list;
    private ImageConverter converter;
    private byte[] bytes;

    public StreamImage(Transaction transaction) {
        this.transaction = transaction;
        converter = new ImageConverter();
    }

    public void startLoop(){
        list = transaction.getList();
        Runnable runnable = () ->{
            opencv_videoio.CvCapture capture = webCam();
            opencv_core.IplImage frame;
            while(true) {
                try {
                    frame = cvQueryFrame(capture);
                    bytes = converter.IplImageToByteArray(frame);
                    for(int i = 0; i < list.listSize(); i++) {
                       try {
                           list.getOutputStream(i).writeInt(bytes.length);
                           list.getOutputStream(i).write(bytes);
                           list.getOutputStream(i).flush();
                       }catch (Exception e){
                           list.removeOutputStream(i);
                           System.out.println("Client has left");
                       }
                       Thread.sleep(10);
                    }
                }catch (Exception e){}
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public opencv_videoio.CvCapture webCam() {
        opencv_videoio.CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
        return capture;
    }
}
