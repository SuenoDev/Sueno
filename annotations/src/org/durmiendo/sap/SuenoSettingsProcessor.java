package org.durmiendo.sap;

import arc.util.Log;
import arc.util.Strings;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Set;
import java.util.stream.Stream;

// annotation processor
public class SuenoSettingsProcessor extends BaseProc {
    String outputPackage = "org.durmiendo.sueno.settings", className = "SettingsBuilder";
    int[] order = {
            TypeKind.FLOAT.ordinal(),
            TypeKind.INT.ordinal(),
            TypeKind.SHORT.ordinal(),
            TypeKind.BYTE.ordinal(),
            TypeKind.BOOLEAN.ordinal(),
    };

    public SuenoSettingsProcessor() {
        supportedAnnotations.add(SuenoSettings.class);
    }

    public void process() throws Throwable {
        if (round == 0) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SuenoSettings.class);

            TypeSpec.Builder builderClass = TypeSpec.classBuilder(ClassName.get(outputPackage, className)).addModifiers(Modifier.PUBLIC);
            MethodSpec.Builder uiBuildSpec = MethodSpec.methodBuilder("uiBuild").addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                    .returns(void.class);
            MethodSpec.Builder loadSpec = MethodSpec.methodBuilder("load").addModifiers(Modifier.STATIC, Modifier.PUBLIC);

            uiBuildSpec.addCode("mindustry.Vars.ui.settings.addCategory(\"sueno-settings\", \"sueno-sueno-white\", s -> {\n");


            Stream<? extends Element> sorted = elements.stream().sorted((a, b) -> {
                int aa = ((VariableElement) as(a)).asType().getKind().ordinal();
                int bb = ((VariableElement) as(b)).asType().getKind().ordinal();
                if (aa > 4) aa = 4;
                if (bb > 4) bb = 4;
                return order[aa] - order[bb];
            });

            for (Object e : sorted.toArray()) {
                Element element = (Element) e;
                SuenoSettings anno = element.getAnnotation(SuenoSettings.class);

                VariableElement var = as(element);
                TypeMirror varTypeMirror = var.asType();
                String fullVarName = this.<TypeElement>as(var.getEnclosingElement()).getQualifiedName().toString() + "." + var.getSimpleName();

                if (!var.getModifiers().contains(Modifier.STATIC) ||
                        !var.getModifiers().contains(Modifier.PUBLIC) ||
                        var.getModifiers().contains(Modifier.FINAL))
                    Log.err(new IllegalArgumentException(
                            Strings.format("Fields annotated with @ must be static, public and not final. Problem field @.",
                                    SuenoSettings.class, fullVarName)));

                TypeKind kind = varTypeMirror.getKind();
                String[] n = fullVarName.split("\\.");
                String varName = "-";
                if (n.length >= 2) varName = n[n.length-2] + "." + n[n.length-1];

                Log.info("@ @", fullVarName, kind);
                switch (kind) {
                    case BYTE: {
                        uiBuildSpec.addCode(Strings.format(
                                "  s.sliderPref(arc.Core.bundle.get(\"@\"), @, @, @, @, v -> {\n" +
                                        "    @ = v;\n" +
                                        "    arc.Core.settings.put(\"@\", v);\n" +
                                        "    return v;\n" +
                                        "  });\n",
                                varName, anno.def(), anno.min(), anno.max(), anno.steep() , fullVarName, varName));
                        loadSpec.addCode(Strings.format(
                                "  @ = arc.Core.settings.getByte(\"@\", @);\n", fullVarName, anno.def()
                        ));
                    } break;
                    case SHORT: {
                        uiBuildSpec.addCode(Strings.format(
                                "  s.sliderPref(arc.Core.bundle.get(\"@\"), @, @, @, @, v -> {\n" +
                                        "    @ = v;\n" +
                                        "    arc.Core.settings.put(\"@\", v);\n" +
                                        "    return v;\n" +
                                        "  });\n",
                                varName, anno.def(), anno.min(), anno.max(), anno.steep() , fullVarName, varName));
                        loadSpec.addCode(Strings.format(
                                "  @ = arc.Core.settings.getShort(\"@\", @);\n", fullVarName, anno.def()
                        ));
                    } break;
                    case INT: {
                        uiBuildSpec.addCode(Strings.format(
                                "  s.sliderPref(arc.Core.bundle.get(\"@\"), @, @, @, @, v -> {\n" +
                                        "    @ = v;\n" +
                                        "    arc.Core.settings.put(\"@\", v);\n" +
                                        "    return v;\n" +
                                        "  });\n",
                                varName, anno.def(), anno.min(), anno.max(), anno.steep() , fullVarName, varName));
                        loadSpec.addCode(Strings.format(
                                "  @ = arc.Core.settings.getInt(\"@\", @);\n", fullVarName, anno.def()
                        ));
                    } break;
                    case BOOLEAN:{
                        uiBuildSpec.addCode(Strings.format(
                                "  s.checkPref(arc.Core.bundle.get(\"@\"), @, v -> {\n" +
                                        "    @ = v;\n" +
                                        "    arc.Core.settings.put(\"@\", v);\n" +
                                        "  });\n",
                                varName, anno.def() == 1, fullVarName, varName));
                        loadSpec.addCode(Strings.format(
                                "  @ = arc.Core.settings.getBool(\"@\", @);\n", fullVarName, varName, anno.def() == 1
                        ));
                    } break;
                    case FLOAT:{
                        uiBuildSpec.addCode(Strings.format(
                                "  s.pref(new org.durmiendo.sueno.ui.dialogs.SliderSetting(arc.Core.bundle.get(\"@\"), @f, @f, @f, @f, v -> {\n" +
                                "    @ = v;\n" +
                                "    arc.Core.settings.put(\"@\", v);\n" +
                                "    return org.durmiendo.sueno.utils.SStrings.fixed(v, @).toString();\n" +
                                "  }));\n",
                                varName, anno.def(), anno.min(), anno.max(), anno.steep() , fullVarName, varName, anno.accuracy()));
                        loadSpec.addCode(Strings.format(
                                "  @ = arc.Core.settings.getFloat(\"@\", @f);\n", fullVarName, varName, anno.def()
                        ));
                    } break;
                    default: {
                        uiBuildSpec.addCode(Strings.format("arc.util.Log.warn(\"generating settings for @ is not supported (@)\");\n", kind, varName));
                    } break;
                }
                uiBuildSpec.addCode(Strings.format("org.durmiendo.sueno.utils.SLog.einfo(\"created \" + arc.Core.bundle.get(\"@\") + \" setting\");\n", varName));
            }
            uiBuildSpec.addCode("});");



            builderClass.addMethod(uiBuildSpec.build());
            builderClass.addMethod(loadSpec.build());

            JavaFile.builder(outputPackage, builderClass.build()).build().writeTo(filer);
        }
    }
}
