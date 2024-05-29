package org.durmiendo.sap;

import arc.util.Log;
import arc.util.Strings;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.Arrays;
import java.util.Set;

// annotation processor
public class SuenoSettingsProcessor extends BaseProc {
    String outputPackage = "org.durmiendo.sueno.settings", className = "SettingsBuilder";

    public SuenoSettingsProcessor() {
        supportedAnnotations.add(SuenoSettings.class);
    }

    public void process() throws Throwable {
        if (round == 0) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(SuenoSettings.class);

            TypeSpec.Builder builderClass = TypeSpec.classBuilder(ClassName.get(outputPackage, className)).addModifiers(Modifier.PUBLIC);
            MethodSpec.Builder uiBuildSpec = MethodSpec.methodBuilder("uiBuild").addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                    .returns(ClassName.get("arc.scene.ui.layout", "Table"));
            MethodSpec.Builder loadSpec = MethodSpec.methodBuilder("load").addModifiers(Modifier.STATIC, Modifier.PUBLIC);

            uiBuildSpec.addCode("Table out = new Table();\n");
            uiBuildSpec.addCode("out.defaults().fillY();\n");

            for (Element element : elements) {
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
                switch (kind) {
                    case BYTE:
                    case SHORT:
                    case INT:
                    case LONG:
                    case FLOAT:
                    case DOUBLE: {
                        uiBuildSpec.addCode(Strings.format("out.slider(@f, @f, @f, @f, v -> {@ = v;});\n",
                                anno.min(), anno.max(), anno.steep(), anno.def(), fullVarName));
                    }
                }
            }

            uiBuildSpec.addCode("return out;");

            builderClass.addMethod(uiBuildSpec.build());
            builderClass.addMethod(loadSpec.build());

            JavaFile.builder(outputPackage, builderClass.build()).build().writeTo(filer);
        }
    }
}
