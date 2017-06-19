package com.ramenbird.example.bigfireball;


import com.example.annotation.Constraint;
import com.example.annotation.Ramen;

/**
 * Created by RamenBird on 2016/12/28.
 */

@Ramen
public class Test {
    @Constraint(primary = true)
    private String a;
    private int c2;

    public int getC2() {
        return c2;
    }

    public void setC2(int c2) {
        this.c2 = c2;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
