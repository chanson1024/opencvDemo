package com.xqs.cvdemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by xiaoqingsong
 * Date: 14/07/2017
 * Time: 9:14 PM
 */

public class Test01Activity extends Activity {
    public static final String TAG = "Test01Activity";

    public static final int MEAN_BLUR = 1;
    public static final int MEDIAN_BLUR = 2;
    public static final int GAUSSIAN_BLUR = 3;
    public static final int SHARPEN = 4;
    public static final int DILATE = 5;
    public static final int ERODE = 6;
    public static final int THRESHOLD = 7;
    public static final int ADAPTIVE_THRESHOLD = 8;

    private final int SELECT_PHOTO = 1;
    private ImageView ivImage, ivImageProcessed;
    private Button mChoose;
    Mat src;

    int ACTION_MODE = 7; //值为上面的常量

    static {
        System.loadLibrary("MyOpenCVLibs");
    }

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    //DO YOUR WORK/STUFF HERE
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test01);

        ivImage = (ImageView)findViewById(R.id.ivImage);
        ivImageProcessed = (ImageView)findViewById(R.id.ivImageProcessed);
        mChoose = (Button)findViewById(R.id.btn_choose);

        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.w(TAG,"opencv loaded successfully");
            mOpenCVCallBack.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }else{
            Log.w(TAG,"opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,mOpenCVCallBack);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        src = new Mat(selectedImage.getHeight(), selectedImage.getWidth(), CvType.CV_8UC4);
                        Utils.bitmapToMat(selectedImage, src);
                        switch (ACTION_MODE){
                            case  GAUSSIAN_BLUR:
                                Imgproc.GaussianBlur(src, src, new Size(3,3), 0);
                                break;
                            case MEAN_BLUR:
                                Imgproc.blur(src, src, new Size(3,3));
                                break;
                            case MEDIAN_BLUR:
                                Imgproc.medianBlur(src, src, 3);
                                break;
                            case SHARPEN:
                                Mat kernel = new Mat(3,3,CvType.CV_16SC1);
                                //int[] values = {0, -1, 0, -1, 5, -1, 0, -1, 0};
                                Log.w(TAG, CvType.typeToString(src.type())+"");
                                kernel.put(0, 0, 0, -1, 0, -1, 5, -1, 0, -1, 0);
                                Imgproc.filter2D(src, src, src.depth(), kernel);
                                break;
                            case DILATE:
                                Mat kernelDilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
                                Imgproc.dilate(src, src, kernelDilate);
                                break;
                            case ERODE:
                                Mat kernelErode = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
                                Imgproc.erode(src, src, kernelErode);
                                break;
                            case THRESHOLD:
                                Imgproc.threshold(src, src, 100, 255, Imgproc.THRESH_BINARY);
                                break;
                            case ADAPTIVE_THRESHOLD:
                                Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2GRAY);
                                Imgproc.adaptiveThreshold(src, src, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 3, 0);
                                break;
                        }
                        Bitmap processedImage = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
                        Log.w(TAG, CvType.typeToString(src.type())+"");
                        Utils.matToBitmap(src, processedImage);
                        ivImage.setImageBitmap(selectedImage);
                        ivImageProcessed.setImageBitmap(processedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
