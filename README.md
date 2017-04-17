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

## dependencies

```
// build.gradle
repositories {
    // add maven repository
    maven { url "http://eaglesakura.github.io/maven/" }
}

dependencies {
    // add library, org.sqlite = 526 methods
    compile 'com.eaglesakura:android-sqliteX:3.16.1.+'
}
```

## LICENSE

[Apache LICENSE](LICENSE.txt)

リポジトリには `SQLite` `Android Open Source Project` `@eaglesakura` が改変・追記したコードそれぞれが含まれています。
SQLite3はpublic domainで提供されており、AOSPがApacheライセンスで提供されているため、本リポジトリはAOSPライセンスを引き継いでApacheライセンスにて公開します。
