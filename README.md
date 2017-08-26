# Android SQLiteX

Androidバージョンに依存しないSQLite3ライブラリです。
ビルド済み `libsqliteX.so` ファイルと Android Framerowkと同じインターフェースのクラス群で構成されています。

基本的には[SQLite/Android Binding](https://sqlite.org/android/doc/trunk/www/index.wiki)で提供されているプロジェクトを最新版環境でビルドし、配布可能なAARに構築しただけになります。

UnitTestはJUnit4準拠に移植されています。

## extensions

次のExtensionが有効化されています。

```
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS5
LOCAL_CFLAGS += -DSQLITE_ENABLE_RTREE
LOCAL_CFLAGS += -DSQLITE_ENABLE_JSON1
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS3
```

## Replace

package名を次のように変更してください。
package名以外は基本的に同じインターフェースが提供されています。

|  | Android Framerowk SQLite3 | SQLiteX |
|---|---|---|
|package名変更 | android.database.sqlite | org.sqlite.database.sqlite |
| 初期化 | - | org.sqlite.database.SQLiteX.install(context); |

## UnitTest(PC, JavaVM)

PCでUnitTestを行う場合は、別途PC用にビルドしたNative用ライブラリが必要となる。
下記のGradleタスクでダウンロードが行える。

```
task installSQLiteX {
    def SQLITE_X_VERSION = "v1.0.x"
    def downloadURL
    def fileName

    if (Os.isFamily(Os.FAMILY_MAC)) {
        downloadURL = "https://raw.githubusercontent.com/eaglesakura/android-sqliteX/${SQLITE_X_VERSION}/prebuilt/"
        fileName = "libsqliteX.dylib"
    } else if (Os.isFamily(Os.FAMILY_WINDOWS)) {
        downloadURL = "https://raw.githubusercontent.com/eaglesakura/android-sqliteX/${SQLITE_X_VERSION}/prebuilt/"
        fileName = "sqliteX.dll"
    } else {
        downloadURL = "https://raw.githubusercontent.com/eaglesakura/android-sqliteX/${SQLITE_X_VERSION}/prebuilt/"
        fileName = "libsqliteX.so"
    }

    def dstFile = new File(fileName)
    if (!dstFile.file) {
        new File(fileName) << new URL("${downloadURL}${fileName}").openStream()
        println "${fileName} installed"
    }
}
```

## dependencies

```
dependencies {
    compile 'com.eaglesakura:android-sqliteX:${version}'
}
```

## LICENSE

[Apache LICENSE](LICENSE.txt)

リポジトリには `SQLite` `Android Open Source Project` `@eaglesakura` が改変・追記したコードそれぞれが含まれています。
SQLite3はpublic domainで提供されており、AOSPがApacheライセンスで提供されているため、本リポジトリはAOSPライセンスを引き継いでApacheライセンスにて公開します。
