package org.durmiendo.sap;

import arc.func.Cons;
import arc.util.Log;
import arc.util.Strings;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// annotation processor

public class SuenoSettingsProcessor extends AbstractProcessor {
    public SuenoSettingsProcessor() {
        supportedAnnotations.add(SuenoSettings.class);
    }

    public static ProcLogHandler logHandler = new ProcLogHandler();

    public Set<String> supportedOptions = new HashSet<>();
    public Set<Class<? extends Annotation>> supportedAnnotations = new HashSet<>();
    public SourceVersion supportedSourceVersion = SourceVersion.RELEASE_8;

    public Types types;
    //public Set<? extends TypeElement> typeElements;
    public Elements elements;
    public Map<String, String> options;
    public Messager messager;
    public Filer filer;

    public RoundEnvironment roundEnv;
    public int round = -1;

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
        //Time.mark();
        this.roundEnv = roundEnv;
        //typeElements = annotations;
        round++;
        process();
        //Log.info("@ round @ @ms", getClass().getCanonicalName(), round, Time.elapsed());
        return true;
    }

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
        public SuenoSettingsProcessor currentInstance;

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

    public Set<? extends Element> ee;

    public void process() {
        if (round == 0) {
            ee = roundEnv.getElementsAnnotatedWith(SuenoSettings.class);
            Log.warn("elements: " + ee.size());
        }
        if (round == 2) {
            //Log.info("@ processing @ (start)", getClass().getCanonicalName(), round);

            String packagee = "org.durmiendo.sueno.gen";

            TypeSpec.Builder classBuilder = TypeSpec.classBuilder("SettingsUI");
            classBuilder.modifiers.add(Modifier.PUBLIC);
            classBuilder.modifiers.add(Modifier.FINAL);

            MethodSpec.Builder build = MethodSpec.methodBuilder("build")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class);

            MethodSpec.Builder load = MethodSpec.methodBuilder("load")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class);

            StringBuilder codeBuild = new StringBuilder("mindustry.Vars.ui.settings.addCategory(\"@settings\", arc.Core.atlas.drawable(\"sueno-sueno-white\"), s -> {");
            StringBuilder codeLoad = new StringBuilder();

            StringBuilder c1 = new StringBuilder();
            c1.append("s.sliderPref(@, @, @, @.@, c -> {");
            c1.append("    @.@ = (@) c;");
            c1.append("    arc.Core.settings.put(\"sueno-@-@\", c);");
            c1.append("});");
            String code11 = c1.toString();
            String code21 = "@.@ = (@) arc.Core.settings.get(\"sueno-@\", @.@)";

            StringBuilder c2 = new StringBuilder();
            c2.append("s.checkPref(@.@, c -> {");
            c2.append("    @.@ = c;");
            c2.append("    arc.Core.settings.put(\"sueno-@-@\", c); ");
            c2.append("});");
            String code12 = c2.toString();
            String code22 = "@.@ = (boolean) arc.Core.settings.get(\"sueno-@-@\", @.@)";
            for (Element e : ee) {
                Log.warn("element: " + e);
                if (isNumber(e.asType())) {
                    SuenoSettings a = e.getAnnotation(SuenoSettings.class);
                    codeBuild.append(Strings.format(code11,
                            a.min(),
                            a.max(),
                            a.steep(),
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.getClass().toString(),
                            e.getKind().toString(),
                            e.getSimpleName().toString()
                    ));
                    codeLoad.append(Strings.format(code21,
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.asType().toString(),
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.getClass().toString(),
                            e.getSimpleName().toString()
                    ));
                } else if (e.asType().toString().equals("boolean")) {
                    SuenoSettings a = e.getAnnotation(SuenoSettings.class);
                    codeBuild.append(Strings.format(code12,
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.getClass().toString(),
                            e.getSimpleName().toString()
                    ));
                    codeLoad.append(Strings.format(code22,
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.getClass().toString(),
                            e.getSimpleName().toString(),
                            e.getClass().toString(),
                            e.getSimpleName().toString()
                    ));
                }
            }
            codeBuild.append("});");

            build.addCode(codeBuild.toString());
            load.addCode(codeLoad.toString());

            classBuilder.addMethod(build.build());
            classBuilder.addMethod(load.build());


            try {
                JavaFile.builder(packagee, classBuilder.build()).build().writeTo(filer);
            } catch (Exception e) {
                Log.err("error of sap.SuenoSettingsProc...", e);
            }
            //Log.info("@ processing @ (end)", getClass().getCanonicalName(), round);
        }

    }

    private boolean isNumber(TypeMirror asType) {
        String s = asType.toString();
        return Arrays.asList("int", "float", "double", "long", "byte", "short").contains(s);
    }
}
