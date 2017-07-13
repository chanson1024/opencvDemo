#include <OpenCVService.h>


JNIEXPORT jint JNICALL Java_com_xqs_cvdemo_OpenCVService_convertGray
  (JNIEnv *, jclass, jlong addrRgba, jlong addrGray){

    Mat & mRgb = *(Mat *)addrRgba;
    Mat & mGray = *(Mat *)addrGray;

    int conv;
    jint retVal;
    conv = toGray(mRgb,mGray);
    retVal = (jint)conv;
    return retVal;
}


int toGray(Mat img,Mat& gray){
    cvtColor(img,gray,CV_RGBA2GRAY);
    if(img.rows == gray.rows && img.cols == gray.cols){
        return 1;
    }
    return 0;
}