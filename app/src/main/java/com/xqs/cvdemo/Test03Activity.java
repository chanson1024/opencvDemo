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

import com.xqs.cvdemo.utils.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by xiaoqingsong
 * Date: 17/07/2017
 * Time: 10:24 PM
 */

public class Test03Activity extends Activity {
    public static final String TAG = "Test03Activity";
    private final int SELECT_PHOTO = 1;

    private ImageView ivImage, ivImageProcessed;
    private Button mChoose;

    static {
        System.loadLibrary("MyOpenCVLibs");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test03);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    int width = selectedImage.getWidth();
                    int height = selectedImage.getHeight();
                    Log.w(TAG,"width-->"+width+"  height-->"+height);


                    byte[] byteArray = ImageUtil.getNV21(width,height,selectedImage);
                    byte[] resultArray = OpenCVService.rotateImg(byteArray,width, height);

                    Bitmap processedBitmap = BitmapFactory.decodeByteArray(resultArray, 0, resultArray.length);

                    ivImage.setImageBitmap(selectedImage);
                    ivImageProcessed.setImageBitmap(processedBitmap);

                }
        }
    }
}
