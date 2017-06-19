package com.example.tablemetabuilder.impl;

import com.example.tablemetabuilder.TableParser;
import com.example.tablemetabuilder.TableParserFactory;

import javax.annotation.processing.ProcessingEnvironment;

/**
 * Created by RamenBird on 2017/1/3.
 */

public class TableParserFactoryImpl extends TableParserFactory {
    public static final TableParserFactoryImpl INSTANCE = new TableParserFactoryImpl();

    @Override
    public TableParser createDbClassParser(ProcessingEnvironment processingEnv) {
        return new TableParserImpl(processingEnv);
    }
}
