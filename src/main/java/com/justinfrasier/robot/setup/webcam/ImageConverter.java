package com.justinfrasier.robot.setup.webcam;


import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageConverter {

    public String getStringFromInputStreamReader(InputStreamReader is) {
        try {
            StringBuilder builder = new StringBuilder();
            int i;
            while ((i = is.read()) != -1) {
                builder.append((char) i);
            }
            return builder.toString();
        }catch (IOException e){
            return null;
        }
    }

    public byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        try {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[0xFFFF];

                for (int len; (len = is.read(buffer)) != -1; )
                    os.write(buffer, 0, len);

                os.flush();

                return os.toByteArray();
            }
        }catch (IOException e){
            return null;
        }
    }

    public BufferedImage FlipImage(BufferedImage src){
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1.0, 1.0);
        affineTransform.translate(-src.getWidth(),0);
        AffineTransformOp transformOp = new AffineTransformOp(affineTransform,null);
        return transformOp.filter(src,null);
    }

    public BufferedImage IplImageToBufferedImage(opencv_core.IplImage src){
        OpenCVFrameConverter.ToIplImage iplImage = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        Frame frame = iplImage.convert(src);
        BufferedImage image = bimConverter.convert(frame);
        //return FlipImage(image);
        return image;
    }

    public  byte[] IplImageToByteArray(opencv_core.IplImage src) throws IOException {
        BufferedImage image = IplImageToBufferedImage(src);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);
        outputStream.flush();
        byte[] imageBytes = outputStream.toByteArray();
        outputStream.close();
        return imageBytes;
    }
    public opencv_core.IplImage ByteArrayToIplImage(byte[] bytes) throws IOException {
        return BufferedImageToIplImage(BytesToBufferedImage(bytes));
    }

    public  BufferedImage BytesToBufferedImage(byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return ImageIO.read(stream);
    }

    public opencv_core.IplImage BufferedImageToIplImage(BufferedImage bufImage){
        OpenCVFrameConverter.ToIplImage toIplImage = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
        opencv_core.IplImage iplImage = toIplImage.convert(java2DFrameConverter.convert(bufImage));
        return iplImage;
    }

}
