package com.xqs.cvdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class MainActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2{
    public static final String TAG = "MainActivity";

    JavaCameraView javaCameraView;

    Mat mRgba,mGray;

    static {
        System.loadLibrary("MyOpenCVLibs");
    }

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        javaCameraView = (JavaCameraView)findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.w(TAG,"opencv loaded successfully");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }else{
            Log.w(TAG,"opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,baseLoaderCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width,height, CvType.CV_8UC4);
        mGray = new Mat(width,height, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        int result = OpenCVService.convertGray(mRgba.getNativeObjAddr(),mGray.getNativeObjAddr());
        if(result == 1){
            return mGray;
        }else{
            return mRgba;
        }
    }
}
