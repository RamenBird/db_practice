package com.example;

import com.example.annotation.Ramen;
import com.example.codemaker.HardMaker;
import com.example.codemaker.JavaPoetImpl;
import com.example.tablemeta.TableInfo;
import com.example.tablemetabuilder.Context;
import com.example.tablemetabuilder.TableParser;
import com.example.tablemetabuilder.TableParserFactory;
import com.google.auto.service.AutoService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class TheClass extends AbstractProcessor {
    private boolean flag;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(Ramen.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        flag = false;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        if (!flag) {
            flag = true;
            Context context = new CurrentContext();

            Set<? extends Element> tableElements = env.getElementsAnnotatedWith(Ramen.class);
            TableParser dbParser = TableParserFactory.getDefault().createDbClassParser(processingEnv);

            try {
                List<TableInfo> tableInfos = dbParser.parseTableMeta(context, tableElements);
                new JavaPoetImpl().generateSourceContent(context, tableInfos, processingEnv.getFiler());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
