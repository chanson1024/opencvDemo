# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


LOCAL_C_INCLUDES := $(LOCAL_PATH)/include

OPENCV_INSTALL_MODULES:=on
OPENCV_CAMERA_MODULES:=off
OPENCV_LIB_TYPE := static

ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
include /Users/xiaoqingsong/Library/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
#include D:/android_sdk/opencv-3.2.0-android-sdk/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
#include D:/android_sdk/OpenCV-2.4.9-android-sdk/OpenCV-2.4.9-android-sdk/sdk/native/jni/OpenCV.mk
#include D:/android_sdk/OpenCV-2.4.10-android-sdk/OpenCV-2.4.10-android-sdk/sdk/native/jni/OpenCV.mk
#include D:/android_sdk/OpenCV-2.4.11-android-sdk/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
#include /Users/denny/Downloads/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk
else
include $(OPENCV_MK_PATH)
endif

LOCAL_ALLOW_UNDEFINED_SYMBOLS := true
LOCAL_MODULE    := JniInterfaces

#配置自己的源文件目录和源文件后缀名
MY_FILES_PATH  :=  $(LOCAL_PATH)
MY_FILES_SUFFIX := %.cpp
# 递归遍历目录下的所有的文件
rwildcard=$(wildcard $1$2) $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2))
# 获取相应的源文件
MY_ALL_FILES := $(foreach src_path,$(MY_FILES_PATH), $(call rwildcard,$(src_path),*.*) )
#MY_ALL_FILES := $(MY_ALL_FILES:$(MY_CPP_PATH)/./%=$(MY_CPP_PATH)%)
MY_SRC_LIST  := $(filter $(MY_FILES_SUFFIX),$(MY_ALL_FILES))
MY_SRC_LIST  := $(MY_SRC_LIST:$(LOCAL_PATH)/%=%)



LOCAL_SRC_FILES  := $(MY_SRC_LIST)


LOCAL_LDLIBS:+=-llog -lm
ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_CFLAGS += -msse4.1
endif
LOCAL_ARM_NEON := true
#LOCAL_ARM_NEON := false
#LOCAL_LDFLAGS :+= -O3  -fopenmp
LOCAL_LDFLAGS :+=  -fuse-ld=bfd
LOCAL_CFLAGS :+= -I -g -ggdb -msse2 -mfloat -abi=softfp -mfpu=neon
#LOCAL_CFLAGS :+= -I -g -ggdb -msse2 -mfloat -abi=softfp
include $(BUILD_SHARED_LIBRARY)
