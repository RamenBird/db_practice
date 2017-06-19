package com.example.tablemetabuilder;

import com.example.tablemeta.TableInfo;

import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Element;

/**
 * Created by RamenBird on 2017/1/3.
 */

public interface TableParser {
    List<TableInfo> parseTableMeta(Context context, Collection<? extends Element> element);

    void changeTableName(TableInfo info, String newTableName);
}
