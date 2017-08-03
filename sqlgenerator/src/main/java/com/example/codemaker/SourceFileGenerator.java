package com.example.codemaker;

import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.ParseResult;

import javax.annotation.processing.Filer;

/**
 * Created by RamenBird on 2017/1/13.
 */

public interface SourceFileGenerator {
    void generateSourceContent(Context context, ParseResult parseResult, Filer filer);
}
