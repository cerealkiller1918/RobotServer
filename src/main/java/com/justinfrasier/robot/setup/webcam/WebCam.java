package com.justinfrasier.robot.setup.webcam;

import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacv.*;
import org.bytedeco.javacv.Frame;
import sun.awt.image.InputStreamImageSource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
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
    static BufferedImage image;
    static byte[] bytes;
    static DataOutputStream outputStream;
    static InputStreamReader inputStream;
    static byte[] inputBytes;
    public static void main(String[] args){
        try {
            serverSocket = new ServerSocket(444);
            socket = serverSocket.accept();
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new InputStreamReader(socket.getInputStream());

            Thread thread = new Thread() {
                @Override
                public void run() {
                    CvCapture capture = webCam();
                    IplImage frame;
                    while(true) {
                        try {
                            //Thread.sleep(500);
                            frame = cvQueryFrame(capture);
                            bytes = IplImageToByteArray(frame);
                            outputStream.writeInt(bytes.length);
                            outputStream.write(bytes);
                            outputStream.flush();
                            //if(getStringFromInputStreamReader(inputStream) == "good")
                            image = BytesToBufferedImage(bytes);


                        }catch (Exception e){

                        }
                    }
                }


            };
            thread.start();
            //new Window();
            System.out.print("Hello");
        }catch (Exception e){
           main(null);
        }
    }

    public static CvCapture webCam() {
        CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
        return capture;
    }

    public static byte[] IplImageToByteArray(IplImage src) throws IOException {
        BufferedImage image = IplImageToBufferedImage(src);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        outputStream.flush();
        byte[] imageBytes = outputStream.toByteArray();
        outputStream.close();
        return imageBytes;
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
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        Frame frame = iplImage.convert(src);
        BufferedImage image = bimConverter.convert(frame);
        //return FlipImage(image);
        return image;
    }

    public static BufferedImage FlipImage(BufferedImage src){
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1.0, 1.0);
        affineTransform.translate(-src.getWidth(),0);
        AffineTransformOp transformOp = new AffineTransformOp(affineTransform,null);
        return transformOp.filter(src,null);
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream())
        {
            byte[] buffer = new byte[0xFFFF];

            for (int len; (len = is.read(buffer)) != -1;)
                os.write(buffer, 0, len);

            os.flush();

            return os.toByteArray();
        }
    }

    public static BufferedImage getBufferImage(){
        return image;
    }

    public static byte[] getBytesArray(){
        return bytes;
    }

    public static String getStringFromInputStreamReader(InputStreamReader is) throws IOException {
        StringBuilder builder = new StringBuilder();
        int i;
        while ((i=is.read())!=-1){
            builder.append((char) i);
        }
        return builder.toString();
    }
}
