package com.justinfrasier.robot.setup.webcam;

import org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_videoio.*;


public class WebCam{

    public static void main(String[] args){
        CvCapture capture = cam();
        IplImage frame;
        while(true) {
            frame = cvQueryFrame(capture);
            cvShowImage("Video",frame);
            char c = (char) cvWaitKey(5);
            if(c=='q') break;
        }
    }

    public static CvCapture cam() {
        //CvCapture capture = cvCreateFileCapture("../../test.mp4");
        CvCapture capture = cvCreateCameraCapture(CV_CAP_ANY);
        cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_HEIGHT,1280);
        cvSetCaptureProperty(capture, CV_CAP_PROP_FRAME_WIDTH, 720);
        return capture;
    }

}
