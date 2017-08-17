#! /bin/sh -eu

cd src/main/jni
$ANDROID_NDK_HOME/ndk-build -B -j4
