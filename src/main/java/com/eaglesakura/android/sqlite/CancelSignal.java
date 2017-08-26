package com.eaglesakura.android.sqlite;

public interface CancelSignal {
    /**
     * 処理がキャンセルされている場合はtrueを返却する。
     * 処理を継続する場合はfalseを返却する
     */
    boolean isCanceled();
}
