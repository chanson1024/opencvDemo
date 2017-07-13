LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_C_INCLUDES := $(LOCAL_PATH)/include

OPENCV_INSTALL_MODULES:=on
OPENCV_CAMERA_MODULES:=on
OPENCV_LIB_TYPE := SHARED

include /Users/xiaoqingsong/Library/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE    := MyOpenCVLibs
LOCAL_SRC_FILES  := OpenCVService.cpp
LOCAL_LDLIBS :=-llog


include $(BUILD_SHARED_LIBRARY)
