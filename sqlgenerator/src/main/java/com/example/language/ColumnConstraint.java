package com.example.language;

/**
 * Created by RamenBird on 2017/1/3.
 */

public enum ColumnConstraint {
    notnull("NOT NULL"),
    defaultvalue(""),
    unique("UNIQUE"),
    primary("PRIMARY KEY"),
    check(""),
    autoincrement("");
    String s1;

    ColumnConstraint(String s) {
        s1 = s;
    }

    @Override
    public String toString() {
        return s1;
    }
}
