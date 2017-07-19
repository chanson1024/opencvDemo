package com.xqs.cvdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xqs.cvdemo.utils.ImageUtil;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xiaoqingsong
 * Date: 17/07/2017
 * Time: 10:24 PM
 */

public class Test04Activity extends Activity {
    public static final String TAG = "Test03Activity";
    private final int SELECT_PHOTO = 1;

    private ImageView ivImage, ivImageProcessed;
    private Button mChoose;

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
        setContentView(R.layout.activity_test04);
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
        if (OpenCVLoader.initDebug()) {
            Log.w(TAG, "opencv loaded successfully");
            mOpenCVCallBack.onManagerConnected(BaseLoaderCallback.SUCCESS);
        } else {
            Log.w(TAG, "opencv not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mOpenCVCallBack);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    // String picturePath contains the path of selected Image

                    //To speed up loading of image
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;

                    Bitmap temp = BitmapFactory.decodeFile(picturePath, options);
                    int orientation = 0;
                    try {
                        ExifInterface imgParams = new ExifInterface(picturePath);
                        orientation = imgParams.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Rotating the image to get the correct orientation
                    Matrix rotate90 = new Matrix();
                    rotate90.postRotate(orientation);
                    Bitmap originalBitmap = ImageUtil.rotateBitmap(temp, orientation);
                    Bitmap tempBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

                    Mat originalMat = new Mat(tempBitmap.getHeight(), tempBitmap.getWidth(), CvType.CV_8U);
                    Utils.bitmapToMat(originalBitmap,originalMat);
                    Mat outMat = ImageUtil.contours(originalMat);

                    Bitmap processedBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Utils.matToBitmap(outMat, processedBitmap);

                    ivImage.setImageBitmap(originalBitmap);
                    ivImageProcessed.setImageBitmap(processedBitmap);

                }
        }
    }
}
