package com.example.codemaker;

import java.util.List;
import java.util.Stack;

/**
 * Created by RamenBird on 2017/1/17.
 */

class FunctionContent {
    private static final String INDENT = "    ";
    private StringBuilder stringBuilder = new StringBuilder();
    private int mIndentCount = 2;
    private String mReturnType;
    private List<String> mParamTypes;
    private Stack<Boolean> mBlockStack = new Stack<>();

    public FunctionContent addOneLineExpression(String format, Object... content) {
        if (content.length == 0)
            addLine(format + ";");
        else
            addLine(String.format(format, content) + ";");

        return this;
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    public void clear() {
        stringBuilder = new StringBuilder();
    }

    public void enterBlock(String s) {
        enterBlock(s, false);
    }

    public void enterBlock(String s, boolean singleLine) {
        String l = s;
        if (!singleLine)
            l += " {";

        mBlockStack.push(singleLine);
        addLine(l);
        mIndentCount++;
    }

    public void exitBlock() {
        mIndentCount--;
        boolean singleLine = mBlockStack.pop();
        if (!singleLine)
            addLine("}");
    }

    private void addLine(String s) {
        for (int i = 0; i < mIndentCount; i++) {
            stringBuilder.append(INDENT);
        }

        stringBuilder.append(s);
        stringBuilder.append("\n");
    }
}
