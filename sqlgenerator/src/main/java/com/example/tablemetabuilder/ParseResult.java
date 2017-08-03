package com.example.tablemetabuilder;

import com.example.tablemeta.TableInfo;

import java.util.List;

/**
 * Created by RamenBird on 2017/8/2.
 */

public class ParseResult {
    private List<TableInfo> mTableInfos;
    private boolean useForeignKey;

    public List<TableInfo> getTableInfos() {
        return mTableInfos;
    }

    public void setTableInfos(List<TableInfo> tableInfos) {
        mTableInfos = tableInfos;
    }

    public boolean isUseForeignKey() {
        return useForeignKey;
    }

    public void setUseForeignKey(boolean useForeignKey) {
        this.useForeignKey = useForeignKey;
    }
}
