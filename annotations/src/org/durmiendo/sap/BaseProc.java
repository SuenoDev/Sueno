package org.durmiendo.sap;

import arc.func.Cons;
import arc.util.Log;
import arc.util.Time;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseProc extends AbstractProcessor {
    public static ProcLogHandler logHandler = new ProcLogHandler();

    public Set<String> supportedOptions = new HashSet<>();
    public Set<Class<? extends Annotation>> supportedAnnotations = new HashSet<>();
    public SourceVersion supportedSourceVersion = SourceVersion.RELEASE_8;

    public Types types;
    public Elements elements;
    public Map<String, String> options;
    public Messager messager;
    public Filer filer;

    public RoundEnvironment roundEnv;
    public int round = -1;

    public BaseProc() {

    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elements = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
        options = processingEnv.getOptions();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        logHandler.currentInstance = this;
        Time.mark();
        this.roundEnv = roundEnv;
        round++;
        try {
            process();
        } catch (Throwable t) {
            Log.err(t);
            return false;
        }
        Log.info("@ round @ @ms", getClass().getCanonicalName(), round, Time.elapsed());
        return true;
    }

    public abstract void process() throws Throwable;

    @Override
    public Set<String> getSupportedOptions() {
        return new HashSet<>(supportedOptions);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> out = new HashSet<>();
        supportedAnnotations.forEach(anno -> out.add(anno.getCanonicalName()));
        return out;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return supportedSourceVersion;
    }

    public static class ProcLogHandler implements Log.LogHandler {
        public BaseProc currentInstance;

        @Override
        public void log(Log.LogLevel l, String t) {
            switch (l) {
                case err: currentInstance.messager.printMessage(Diagnostic.Kind.ERROR, t);
                case warn: currentInstance.messager.printMessage(Diagnostic.Kind.WARNING, t);
                case debug:
                case info:
                case none:
                    currentInstance.messager.printMessage(Diagnostic.Kind.NOTE, t);
            }
        }
    }

    public <R, T extends Annotation> R getClassFromAnno(T annotation, Cons<T> cons) {
        try {
            cons.get(annotation);
        } catch (MirroredTypeException e) {
            return (R) e.getTypeMirror();
        } catch (MirroredTypesException e) {
            return (R) e.getTypeMirrors();
        }
        throw new RuntimeException("What the fuck??");
    }

    public <T> T as(Object obj) {
        return (T) obj;
    }
}
