package com.justinfrasier.robot.setup.webcam;


import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.bytedeco.javacpp.opencv_core.*;

public class TestForOpenCV {

    public TestForOpenCV() {

        System.out.println("Welcome to OpenCV " + Core.VERSION);
        Mat m = new Mat(5,10, CvType.CV_8UC1, new Scalar4i(0));
        System.out.println("OpenCV Mat: " + m);

    }
}
