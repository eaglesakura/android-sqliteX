package com.eaglesakura.android.sqlite;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.concurrent.CancellationException;

public class CancelableCursor implements Cursor {
    @NonNull
    final Cursor mCursor;

    @NonNull
    final CancelSignal mSignal;

    public CancelableCursor(@NonNull Cursor cursor, @NonNull CancelSignal signal) {
        mCursor = cursor;
        mSignal = signal;
    }

    private void assertNotCanceled() {
        if (mSignal.isCanceled()) {
            throw new CancellationException("Cursor is canceled");
        }
    }

    @Override
    public int getCount() {
        assertNotCanceled();
        return mCursor.getCount();
    }

    @Override
    public int getPosition() {
        assertNotCanceled();
        return mCursor.getPosition();
    }

    @Override
    public boolean move(int i) {
        assertNotCanceled();
        return mCursor.move(i);
    }

    @Override
    public boolean moveToPosition(int i) {
        assertNotCanceled();
        return mCursor.moveToPosition(i);
    }

    @Override
    public boolean moveToFirst() {
        assertNotCanceled();
        return mCursor.moveToFirst();
    }

    @Override
    public boolean moveToLast() {
        assertNotCanceled();
        return mCursor.moveToLast();
    }

    @Override
    public boolean moveToNext() {
        assertNotCanceled();
        return mCursor.moveToNext();
    }

    @Override
    public boolean moveToPrevious() {
        assertNotCanceled();
        return mCursor.moveToPrevious();
    }

    @Override
    public boolean isFirst() {
        assertNotCanceled();
        return mCursor.isFirst();
    }

    @Override
    public boolean isLast() {
        assertNotCanceled();
        return mCursor.isLast();
    }

    @Override
    public boolean isBeforeFirst() {
        assertNotCanceled();
        return mCursor.isBeforeFirst();
    }

    @Override
    public boolean isAfterLast() {
        assertNotCanceled();
        return mCursor.isAfterLast();
    }

    @Override
    public int getColumnIndex(String s) {
        assertNotCanceled();
        return mCursor.getColumnIndex(s);
    }

    @Override
    public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
        assertNotCanceled();
        return mCursor.getColumnIndexOrThrow(s);
    }

    @Override
    public String getColumnName(int i) {
        assertNotCanceled();
        return mCursor.getColumnName(i);
    }

    @Override
    public String[] getColumnNames() {
        assertNotCanceled();
        return mCursor.getColumnNames();
    }

    @Override
    public int getColumnCount() {
        assertNotCanceled();
        return mCursor.getColumnCount();
    }

    @Override
    public byte[] getBlob(int i) {
        assertNotCanceled();
        return mCursor.getBlob(i);
    }

    @Override
    public String getString(int i) {
        assertNotCanceled();
        return mCursor.getString(i);
    }

    @Override
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {
        assertNotCanceled();
        mCursor.copyStringToBuffer(i, charArrayBuffer);
    }

    @Override
    public short getShort(int i) {
        assertNotCanceled();
        return mCursor.getShort(i);
    }

    @Override
    public int getInt(int i) {
        assertNotCanceled();
        return mCursor.getInt(i);
    }

    @Override
    public long getLong(int i) {
        assertNotCanceled();
        return mCursor.getLong(i);
    }

    @Override
    public float getFloat(int i) {
        assertNotCanceled();
        return mCursor.getFloat(i);
    }

    @Override
    public double getDouble(int i) {
        assertNotCanceled();
        return mCursor.getDouble(i);
    }

    @Override
    public int getType(int i) {
        assertNotCanceled();
        return mCursor.getType(i);
    }

    @Override
    public boolean isNull(int i) {
        assertNotCanceled();
        return mCursor.isNull(i);
    }

    @Override
    public void deactivate() {
        assertNotCanceled();
        mCursor.deactivate();
    }

    @Override
    public boolean requery() {
        assertNotCanceled();
        return mCursor.requery();
    }

    @Override
    public void close() {
        mCursor.close();
    }

    @Override
    public boolean isClosed() {
        return mCursor.isClosed();
    }

    @Override
    public void registerContentObserver(ContentObserver contentObserver) {
        mCursor.registerContentObserver(contentObserver);
    }

    @Override
    public void unregisterContentObserver(ContentObserver contentObserver) {
        mCursor.unregisterContentObserver(contentObserver);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        mCursor.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        mCursor.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
        mCursor.setNotificationUri(contentResolver, uri);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public Uri getNotificationUri() {
        return mCursor.getNotificationUri();
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return mCursor.getWantsAllOnMoveCalls();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void setExtras(Bundle bundle) {
        mCursor.setExtras(bundle);
    }

    @Override
    public Bundle getExtras() {
        return mCursor.getExtras();
    }

    @Override
    public Bundle respond(Bundle bundle) {
        return mCursor.respond(bundle);
    }
}
