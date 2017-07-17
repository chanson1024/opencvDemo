package com.xqs.cvdemo;

/**
 * Created by xiaoqingsong
 * Date: 13/07/2017
 * Time: 12:00 AM
 */

public class OpenCVService {

    public native static int convertGray(long mRgbaAddr,long mGrayAddr);

    public native static byte[] rotateImg(byte[] data, int width, int height);
}
