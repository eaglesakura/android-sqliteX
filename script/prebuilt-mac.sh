#! /bin/bash -eu
# build Ubuntu
if [[ $(uname) != *Darwin* ]]; then
    exit 1
fi

rm -rf .mac.dylib/
mkdir .mac.dylib
cd .mac.dylib

for file in `find ../src/main/jni/ -name "*.cpp" -type f`; do
  echo $file
  g++ \
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
     -U__APPLE__ \
     -Wno-unused-parameter \
     -Wno-int-to-pointer-cast \
     -Wno-parentheses \
     -Wno-maybe-uninitialized \
     -Wno-conversion-null \
     -DNDEBUG \
     -c $file
done

for file in `find ../src/main/jni/ -name "*.c" -type f`; do
  echo $file
  gcc \
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
     -U__APPLE__ \
     -Wno-unused-parameter \
     -Wno-int-to-pointer-cast \
     -Wno-parentheses \
     -Wno-maybe-uninitialized \
     -DNDEBUG \
     -c $file
done

# Link library
g++ \
    --shared -o libsqliteX.dylib \
    android_database_SQLiteCommon.o \
    android_database_SQLiteConnection.o \
    android_database_SQLiteDebug.o \
    android_database_SQLiteGlobal.o \
    JNIHelp.o \
    JniConstants.o \
    sqlite3.o

objdump -p libsqliteX.dylib | grep ".dylib"

cp -f libsqliteX.dylib ../
echo "Build completed!"
