package com.justinfrasier.robot.setup.webcam;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_videoio;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.bytedeco.javacpp.opencv_videoio.CV_CAP_ANY;
import static org.bytedeco.javacpp.opencv_videoio.cvCreateCameraCapture;
import static org.bytedeco.javacpp.opencv_videoio.cvQueryFrame;

public class Master {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedImage image;
    private byte[] bytes;
    private DataOutputStream outputStream;
    private static ImageConverter converter = new ImageConverter();
    private SyncList outputStreamList;
    private Transaction transaction;
    private StreamImage streamImage;

    public void run(){
        try {
            SetupServerSocket();
            transaction = new Transaction();
            streamImage = new StreamImage(transaction);
            streamImage.startLoop();
            while (true){
                System.out.println("Waiting on client");
                transaction.add(serverSocket.accept());
                System.out.println("Client joined");
            }
        }catch (IOException e){}
    }



    private void initOutputStream() throws IOException {
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    private void SetupSocketFromServerSocket() throws IOException {
        socket = serverSocket.accept();
    }

    private void SetupServerSocket() throws IOException {
        serverSocket = new ServerSocket(444);
    }

    private void UpdateImageLoop() {
        opencv_videoio.CvCapture capture = webCam();
        opencv_core.IplImage frame;
        while(true) {
            try {
                frame = cvQueryFrame(capture);
                bytes = converter.IplImageToByteArray(frame);
                outputStream.writeInt(bytes.length);
                outputStream.write(bytes);
                outputStream.flush();
                image = converter.BytesToBufferedImage(bytes);
            }catch (Exception e){}
        }
    }

    public opencv_videoio.CvCapture webCam() {
        opencv_videoio.CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
        return capture;
    }

    public BufferedImage getBufferImage(){
        return image;
    }

}
