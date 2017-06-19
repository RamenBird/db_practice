package com.example.tablemetabuilder;

import com.example.tablemetabuilder.impl.TableParserFactoryImpl;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by RamenBird on 2017/1/3.
 */

public abstract class TableParserFactory {
    public static final TableParserFactory getDefault() {
        return TableParserFactoryImpl.INSTANCE;
    }

    public abstract TableParser createDbClassParser(ProcessingEnvironment processingEnv);
}
