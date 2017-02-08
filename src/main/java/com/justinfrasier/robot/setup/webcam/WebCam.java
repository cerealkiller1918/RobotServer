package com.justinfrasier.robot.setup.webcam;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvDecodeImage;
import static org.bytedeco.javacpp.opencv_videoio.*;
import static org.bytedeco.javacv.OpenCVFrameConverter.*;


public class WebCam{

    static ServerSocket serverSocket;
    static Socket socket;

    public static void main(String[] args){
        try {
            //serverSocket = new ServerSocket(444);
            //socket = serverSocket.accept();
            //OutputStream outputStream = socket.getOutputStream();

            CvCapture capture = webCam();
            IplImage frame;
            IplImage frame2;
            //cvNamedWindow("Video", CV_WINDOW_AUTOSIZE);
            while (true) {
                frame = cvQueryFrame(capture);
                byte[] bytes = IplImageToByteArray(frame);
                //outputStream.write(bytes);
                frame2 = ByteArrayToIplImage(bytes);
                cvShowImage("Test",frame2);
                cvWaitKey(15);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static CvCapture webCam() {
        CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
        return capture;
    }

    public static byte[] IplImageToByteArray(IplImage src){
        BufferedImage image = IplImageToBufferedImage(src);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write()
        return bytes;
    }
    public static IplImage ByteArrayToIplImage(byte[] bytes) throws IOException {

        return BufferedImageToIplImage(BytesToBufferedImage(bytes));
    }

    public static BufferedImage BytesToBufferedImage(byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return ImageIO.read(stream);
    }

    public static IplImage BufferedImageToIplImage(BufferedImage bufImage){
        ToIplImage toIplImage = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        IplImage iplImage = toIplImage.convert(java2DFrameConverter.convert(bufImage));
        return iplImage;
    }

    public static BufferedImage IplImageToBufferedImage(IplImage src){
        OpenCVFrameConverter.ToIplImage iplImage = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter bimCoverter = new Java2DFrameConverter();
        Frame frame = iplImage.convert(src);
        BufferedImage image = bimCoverter.convert(frame);
        BufferedImage result = (BufferedImage) image.getScaledInstance(image.getWidth(),image.getHeight(), Image.SCALE_DEFAULT);
        image.flush();
        return result;
    }
}
