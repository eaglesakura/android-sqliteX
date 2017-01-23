package org.sqlite.database;

import com.eaglesakura.android.devicetest.DeviceTestCase;
import com.eaglesakura.android.devicetest.TestUtil;
import com.eaglesakura.util.LogUtil;
import com.eaglesakura.util.RandomUtil;

import org.junit.Test;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteDatabaseCorruptException;
import org.sqlite.database.sqlite.SQLiteOpenHelper;
import org.sqlite.database.sqlite.SQLiteStatement;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class SQLiteDatabaseTest extends DeviceTestCase {

    File DB_PATH;

    static {
        SQLiteX.install(InstrumentationRegistry.getContext());
    }

    @Override
    public void onSetup() {
        super.onSetup();
        DB_PATH = new File(TestUtil.getCacheDirectory(getContext()), RandomUtil.randShortString() + ".db");
        LogUtil.out("DB", DB_PATH.getAbsolutePath());
    }

    @Test
    public void report_version() {
        SQLiteStatement st;
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
        st = db.compileStatement("SELECT sqlite_version()");
        assertEquals(st.simpleQueryForString(), BuildConfig.SQLITE_VERSION);
    }

    class TestHelper extends SQLiteOpenHelper {
        public TestHelper(Context ctx) {
            super(ctx, DB_PATH.getPath(), null, 1);
        }

        public void onConfigure(SQLiteDatabase db) {
            db.execSQL("PRAGMA key = 'secret'");
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE t1(x)");
        }

        public void onUpgrade(SQLiteDatabase db, int iOld, int iNew) {
        }
    }

    /**
     * * Check that SQLiteOpenHelper works.
     */
    @Test
    public void helper_test_1() throws Exception {
        SQLiteDatabase.deleteDatabase(DB_PATH);

        SQLiteOpenHelper helper = new TestHelper(getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO t1 VALUES ('x'), ('y'), ('z')");

        String res = string_from_t1_x(db);
        assertEquals(res, ".x.y.z");

        helper.close();
    }

    @Test
    public void supp_char_test_1() throws Exception {
        SQLiteDatabase.deleteDatabase(DB_PATH);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        String res = "";
        String smiley = new String(Character.toChars(0x10000));

        db.execSQL("CREATE TABLE t1(x)");
        db.execSQL("INSERT INTO t1 VALUES ('a" + smiley + "b')");

        res = string_from_t1_x(db);

        assertEquals(res, ".a" + smiley + "b");

        db.close();
    }


    /**
     * * If this is a SEE build, check that encrypted databases work.
     */
    @Test
    public void see_test_1() throws Exception {
        if (!SQLiteDatabase.hasCodec()) return;

        SQLiteDatabase.deleteDatabase(DB_PATH);
        String res = "";

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        db.execSQL("PRAGMA key = 'secretkey'");

        db.execSQL("CREATE TABLE t1(x)");
        db.execSQL("INSERT INTO t1 VALUES ('one'), ('two'), ('three')");

        res = string_from_t1_x(db);
        assertEquals(res, ".one.two.three");
        db.close();

        assertEquals(db_is_encrypted(), "encrypted");

        db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        db.execSQL("PRAGMA key = 'secretkey'");
        res = string_from_t1_x(db);
        assertEquals(res, ".one.two.three");
        db.close();

        res = "unencrypted";
        try {
            db = SQLiteDatabase.openOrCreateDatabase(DB_PATH.getPath(), null);
            string_from_t1_x(db);
        } catch (SQLiteDatabaseCorruptException e) {
            res = "encrypted";
        } finally {
            db.close();
        }
        assertEquals(res, "encrypted");

        res = "unencrypted";
        try {
            db = SQLiteDatabase.openOrCreateDatabase(DB_PATH.getPath(), null);
            db.execSQL("PRAGMA key = 'otherkey'");
            string_from_t1_x(db);
        } catch (SQLiteDatabaseCorruptException e) {
            res = "encrypted";
        } finally {
            db.close();
        }
        assertEquals(res, "encrypted");
    }


    /**
     * * If this is a SEE build, check that SQLiteOpenHelper still works.
     */
    @Test
    public void see_test_2() throws Exception {
        if (!SQLiteDatabase.hasCodec()) return;
        SQLiteDatabase.deleteDatabase(DB_PATH);

        SQLiteOpenHelper helper = new TestHelper(getApplication());
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("INSERT INTO t1 VALUES ('x'), ('y'), ('z')");

        String res = string_from_t1_x(db);
        assertEquals(res, ".x.y.z");
        assertEquals(db_is_encrypted(), "encrypted");

        helper.close();
        helper = new TestHelper(getApplication());
        db = helper.getReadableDatabase();
        assertEquals("see_test_2.3", res, ".x.y.z");

        db = helper.getWritableDatabase();
        assertEquals("see_test_2.4", res, ".x.y.z");

        assertEquals("see_test_2.5", db_is_encrypted(), "encrypted");
    }


    @Test
    public void csr_test_1() throws Exception {
        SQLiteDatabase.deleteDatabase(DB_PATH);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        String res = "";

        db.execSQL("CREATE TABLE t1(x)");
        db.execSQL("INSERT INTO t1 VALUES ('one'), ('two'), ('three')");

        res = string_from_t1_x(db);
        assertEquals(res, ".one.two.three");

        db.close();
        assertEquals(db_is_encrypted(), "unencrypted");
    }

    /**
     * * Use a Cursor to loop through the results of a SELECT query.
     */
    @Test
    public void csr_test_2() throws Exception {
        SQLiteDatabase.deleteDatabase(DB_PATH);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        String res = "";
        String expect = "";
        int i;
        int nRow = 0;

        db.execSQL("CREATE TABLE t1(x)");
        db.execSQL("BEGIN");
        for (i = 0; i < 1000; i++) {
            db.execSQL("INSERT INTO t1 VALUES ('one'), ('two'), ('three')");
            expect += ".one.two.three";
        }
        db.execSQL("COMMIT");
        Cursor c = db.rawQuery("SELECT x FROM t1", null);
        if (c != null) {
            boolean bRes;
            for (bRes = c.moveToFirst(); bRes; bRes = c.moveToNext()) {
                String x = c.getString(0);
                res = res + "." + x;
            }
        } else {
            LogUtil.out("csr_test_1", "c==NULL");
        }
        assertEquals(res, expect);

        db.execSQL("BEGIN");
        for (i = 0; i < 1000; i++) {
            db.execSQL("INSERT INTO t1 VALUES (X'123456'), (X'789ABC'), (X'DEF012')");
            db.execSQL("INSERT INTO t1 VALUES (45), (46), (47)");
            db.execSQL("INSERT INTO t1 VALUES (8.1), (8.2), (8.3)");
            db.execSQL("INSERT INTO t1 VALUES (NULL), (NULL), (NULL)");
        }
        db.execSQL("COMMIT");

        c = db.rawQuery("SELECT x FROM t1", null);
        if (c != null) {
            boolean bRes;
            for (bRes = c.moveToFirst(); bRes; bRes = c.moveToNext()) nRow++;
        } else {
            LogUtil.out("csr_test_1", "c==NULL");
        }
        assertEquals("" + nRow, "15000");

        db.close();
    }


    /**
     * * Test that a database connection may be accessed from a second thread.
     */
    @Test
    public void thread_test_1() {
        SQLiteDatabase.deleteDatabase(DB_PATH);
        final SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);

        String db_path2 = DB_PATH.toString() + "2";

        db.execSQL("CREATE TABLE t1(x, y)");
        db.execSQL("INSERT INTO t1 VALUES (1, 2), (3, 4)");

        Thread t = new Thread(new Runnable() {
            public void run() {
                SQLiteStatement st = db.compileStatement("SELECT sum(x+y) FROM t1");
                String res = st.simpleQueryForString();
                assertEquals(res, "10");
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * * Test that a database connection may be accessed from a second thread.
     */
    @Test
    public void thread_test_2() {
        SQLiteDatabase.deleteDatabase(DB_PATH);
        final SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);

        db.execSQL("CREATE TABLE t1(x, y)");
        db.execSQL("INSERT INTO t1 VALUES (1, 2), (3, 4)");

        db.enableWriteAheadLogging();
        db.beginTransactionNonExclusive();
        db.execSQL("INSERT INTO t1 VALUES (5, 6)");

        Thread t = new Thread(new Runnable() {
            public void run() {
                SQLiteStatement st = db.compileStatement("SELECT sum(x+y) FROM t1");
                String res = st.simpleQueryForString();
            }
        });

        t.start();
        String res = "concurrent";

        int i;
        for (i = 0; i < 20 && t.isAlive(); i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        if (t.isAlive()) {
            res = "blocked";
        }

        db.endTransaction();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
        if (SQLiteDatabase.hasCodec()) {
            assertEquals(res, "blocked");
        } else {
            assertEquals(res, "concurrent");
        }
    }

    @Test
    public void stmt_jrnl_test_1() throws Exception {
        SQLiteDatabase.deleteDatabase(DB_PATH);
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH, null);
        String res = "";

        db.execSQL("CREATE TABLE t1(x, y UNIQUE)");
        db.execSQL("BEGIN");
        db.execSQL("INSERT INTO t1 VALUES(1, 1), (2, 2), (3, 3)");
        db.execSQL("UPDATE t1 SET y=y+3");
        db.execSQL("COMMIT");
        db.close();
        assertEquals("did not crash", "did not crash");
    }

    private String string_from_t1_x(SQLiteDatabase db) {
        String res = "";

        Cursor c = db.rawQuery("SELECT x FROM t1", null);
        boolean bRes;
        for (bRes = c.moveToFirst(); bRes; bRes = c.moveToNext()) {
            String x = c.getString(0);
            res = res + "." + x;
        }

        return res;
    }

    /**
     * * Test if the database at DB_PATH is encrypted or not. The db
     * * is assumed to be encrypted if the first 6 bytes are anything
     * * other than "SQLite".
     * *
     * * If the test reveals that the db is encrypted, return the string
     * * "encrypted". Otherwise, "unencrypted".
     */
    private String db_is_encrypted() throws Exception {
        FileInputStream in = new FileInputStream(DB_PATH);

        byte[] buffer = new byte[6];
        in.read(buffer, 0, 6);

        String res = "encrypted";
        if (Arrays.equals(buffer, (new String("SQLite")).getBytes())) {
            res = "unencrypted";
        }
        return res;
    }
}
