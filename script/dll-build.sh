#! /bin/sh -eu
# build by cygwin
if [[ $(uname) != CYGWIN* ]]; then
    exit 1
fi

# mkdir .dll
cd .dll

rm -f *.o
rm -f *.dll

for file in `find ../src/main/jni/ -name "*.cpp" -type f`; do
     echo $file
     i686-w64-mingw32-g++ \
        -I../src/main/jni/ \
        -I../src/main/jni/sqlite \
        -I../src/main/jni/sqlite/nativehelper \
        -DSQLITE_ENABLE_FTS3 \
        -DSQLITE_ENABLE_FTS5 \
        -DSQLITE_ENABLE_RTREE \
        -DSQLITE_ENABLE_JSON1 \
        -DSQLITE_TEMP_STORE=3 \
        -DHAVE_CONFIG_H \
        -DKHTML_NO_EXCEPTIONS \
        -DGKWQ_NO_JAVA \
        -DNO_SUPPORT_JS_BINDING \
        -DQT_NO_WHEELEVENT \
        -DKHTML_NO_XBL \
        -DHAVE_STRCHRNUL=0 \
        -DLOG_NDEBUG \
        -Wno-unused-parameter \
        -Wno-int-to-pointer-cast \
        -Wno-parentheses \
        -Wno-maybe-uninitialized \
        -Wno-conversion-null \
        -c $file
done

for file in `find ../src/main/jni/ -name "*.c" -type f`; do
     echo $file
     i686-w64-mingw32-gcc \
        -I../src/main/jni/ \
        -I../src/main/jni/sqlite \
        -I../src/main/jni/sqlite/nativehelper \
        -DSQLITE_ENABLE_FTS3 \
        -DSQLITE_ENABLE_FTS5 \
        -DSQLITE_ENABLE_RTREE \
        -DSQLITE_ENABLE_JSON1 \
        -DSQLITE_TEMP_STORE=3 \
        -DHAVE_CONFIG_H \
        -DKHTML_NO_EXCEPTIONS \
        -DGKWQ_NO_JAVA \
        -DNO_SUPPORT_JS_BINDING \
        -DQT_NO_WHEELEVENT \
        -DKHTML_NO_XBL \
        -DHAVE_STRCHRNUL=0 \
        -DLOG_NDEBUG \
        -Wno-unused-parameter \
        -Wno-int-to-pointer-cast \
        -Wno-parentheses \
        -Wno-maybe-uninitialized \
        -c $file
done

# Link library
i686-w64-mingw32-g++ \
    --shared -o sqliteX.dll   \
    android_database_SQLiteCommon.o \
    android_database_SQLiteConnection.o \
    android_database_SQLiteDebug.o \
    android_database_SQLiteGlobal.o \
    JniConstants.o \
    JNIHelp.o \
    sqlite3.o

echo "Build completed!"
