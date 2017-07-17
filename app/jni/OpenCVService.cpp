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

JNIEXPORT jbyteArray JNICALL Java_com_xqs_cvdemo_OpenCVService_rotateImg
        (JNIEnv *env, jclass thiz, jbyteArray imageBuff, jint width, jint height){

    jbyteArray result = NULL;

    jbyte* yuvData = env -> GetByteArrayElements(imageBuff,0);

    Mat yuvImg(height, width, CV_8UC1, (uchar *)yuvData);

//    Mat _BGRImg_1;
    Mat _BGRImg_2;
//    cvtColor(yuvImg, _BGRImg_1, CV_YUV2BGR_NV21);
    cv::flip(yuvImg,_BGRImg_2, -1);

    result = env->NewByteArray(_BGRImg_2.cols * _BGRImg_2.rows);
    env->SetByteArrayRegion(result, 0, _BGRImg_2.cols * _BGRImg_2.rows, (jbyte *) _BGRImg_2.data);


    return result;
}


int toGray(Mat img,Mat& gray){
    cvtColor(img,gray,CV_RGBA2GRAY);
    if(img.rows == gray.rows && img.cols == gray.cols){
        return 1;
    }
    return 0;
}