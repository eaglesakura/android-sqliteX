#! /bin/bash -eu
# build Ubuntu
if [[ $(uname) != *Linux* ]]; then
    exit 1
fi

rm -rf .linux.so/
mkdir .linux.so
cd .linux.so

for file in `find ../src/main/jni/ -name "*.cpp" -type f`; do
  echo $file
  g++ \
     -O2 \
     -fPIC \
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
     -DNDEBUG \
     -c $file
done

for file in `find ../src/main/jni/ -name "*.c" -type f`; do
  echo $file
  gcc \
     -O2 \
     -fPIC \
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
     -DNDEBUG \
     -c $file
done

# Link library
g++ \
    -O2 \
    -static-libgcc -static-libstdc++ -fPIC \
    --shared -o libsqliteX.so \
    android_database_SQLiteCommon.o \
    android_database_SQLiteConnection.o \
    android_database_SQLiteDebug.o \
    android_database_SQLiteGlobal.o \
    JNIHelp.o \
    JniConstants.o \
    sqlite3.o

objdump -p libsqliteX.so | grep "so"

cp -f libsqliteX.so ../
echo "Build completed!"
