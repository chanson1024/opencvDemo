#APP_ABI := armeabi-v7a  arm64-v8a  x86 x86_64
APP_ABI = armeabi-v7a
#APP_ABI = armeabi
APP_STL := stlport_static
APP_STL := gnustl_static
#NDK_TOOLCHAIN = x86_64-4.9
#NDK_TOOLCHAIN = x86-4.9
NDK_TOOLCHAIN_VERSION = 4.9
APP_CPPFLAGS := -fexceptions -frtti -fpermissive
APP_CPPFLAGS +=-std=c++11
APP_OPTIM := debug
APP_PLATFORM:=android-19