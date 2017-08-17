#! /bin/sh -eu
# build by cygwin
if [[ $(uname) != CYGWIN* ]]; then
    exit 1
fi

# mkdir .dll
cd .dll

for file in `find ../src/main/jni/ -name "*.c*" -type f`; do
     echo $file
     i686-w64-mingw32-gcc \
        -I ../src/main/jni/ \
        -I ../src/main/jni/sqlite \
        -I ../src/main/jni/sqlite/nativehelper \
        -D LOG_NDEBUG \
        -c $file
done
