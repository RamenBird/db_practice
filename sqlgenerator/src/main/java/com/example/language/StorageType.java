package com.example.language;

/**
 * Created by RamenBird on 2017/1/4.
 */

public enum StorageType {
    NULL("NULL"),
    INTEGER("INTEGER"),
    REAL("REAL"),
    TEXT("TEXT"),
    BLOB("BLOB");

    private String s1;

    StorageType(String s) {
        s1 = s;
    }

    @Override
    public String toString() {
        return s1;
    }
}
