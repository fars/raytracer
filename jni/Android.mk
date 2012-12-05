LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

APP_ABI := all
LOCAL_MODULE    := plasma
LOCAL_SRC_FILES := plasma.cpp
APP_OPTIM       := release
LOCAL_CFLAGS =  -march=armv7 \
                -mfloat-abi=softfp \
                -mfpu=vfp \
                -ffast-math
APP_CFLAGS +=   -Ofast
LOCAL_LDLIBS    := -lm -llog -ljnigraphics

include $(BUILD_SHARED_LIBRARY)
