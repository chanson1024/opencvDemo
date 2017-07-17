package com.xqs.cvdemo.utils;

import android.graphics.Bitmap;

/**
 * Created by xiaoqingsong
 * Date: 17/07/2017
 * Time: 11:52 PM
 */

public class ImageUtil {

    public static byte[] getNV21(int inputWidth, int inputHeight, Bitmap srcBitmap) {
        int[] argb = new int[inputWidth * inputHeight];
        if (null != srcBitmap) {
            try {
                srcBitmap.getPixels(argb, 0, inputWidth, 0, 0, inputWidth, inputHeight);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            // byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
            // encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
//            if (null != srcBitmap && !srcBitmap.isRecycled()) {
//                srcBitmap.recycle();
//                srcBitmap = null;
//            }
            return colorconvertRGB_IYUV_I420(argb, inputWidth, inputHeight);
        } else
            return null;
    }

    public static byte[] colorconvertRGB_IYUV_I420(int[] aRGB, int width, int height) {
        final int frameSize = width * height;
        final int chromasize = frameSize / 4;

        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize + chromasize;
        byte[] yuv = new byte[width * height * 3 / 2];

        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                //a = (aRGB[index] & 0xff000000) >> 24; //not using it right now
                R = (aRGB[index] & 0xff0000) >> 16;
                G = (aRGB[index] & 0xff00) >> 8;
                B = (aRGB[index] & 0xff) >> 0;

                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));

                if (j % 2 == 0 && index % 2 == 0) {
                    yuv[vIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    yuv[uIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                }
                index++;
            }
        }
        return yuv;
    }
}
