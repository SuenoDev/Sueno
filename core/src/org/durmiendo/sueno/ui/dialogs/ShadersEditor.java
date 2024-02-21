package org.durmiendo.sueno.ui.dialogs;

import arc.graphics.Gl;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.scene.ui.Dialog;
import arc.scene.ui.TextField;
import arc.util.Reflect;
import mindustry.ui.Styles;
import org.durmiendo.sueno.graphics.SShaders;

import java.lang.reflect.Field;

// TODO fuck1пg ui
public class ShadersEditor extends Dialog {
    int level = 0;
    Field editing;

    public ShadersEditor() {
        super("@shader-editor");
    }

    @Override
    public Dialog show() {
        rebuild();
        return super.show();
    }

    public void rebuild() {
        cont.clearChildren();

        cont.fill(t -> {
            t.top();
            t.button("back", Styles.cleart, () -> {
                if (level == 0)
                    hide();
                else {
                    level--;
                    rebuild();
                }
            }).fillX();
        });
        cont.row();
        cont.pane(t -> {
            if (level == 0) {
                for (Field field : SShaders.class.getDeclaredFields()) {
                    t.button(field.getName(), () -> {
                        editing = field;
                        level = 1;
                        rebuild();
                    }).fillX();
                    t.row();
                }
            } else if (level == 1) {
                TextField frag = t.field("frag", str -> {}).fillX().get();
                TextField vert = t.field("vert", str -> {}).fillX().get();

                t.row();

                t.button("compile", () -> {
                    Shader shader = Reflect.get(editing);

                    Gl.useProgram(0);
                    Gl.deleteShader(Reflect.get(shader, "vertexShaderHandle"));
                    Gl.deleteShader(Reflect.get(shader, "fragmentShaderHandle"));
                    Gl.deleteProgram(Reflect.get(shader, "program"));

                    compile(shader, vert.getText(), frag.getText());
                }).fillX();
            }
        }).fillX();
    }

    void compile(Shader shader, String vert, String frag) {
        if(vert == null) throw new IllegalArgumentException("vertex shader must not be null");
        if(frag == null) throw new IllegalArgumentException("fragment shader must not be null");

        vert = Reflect.invoke(shader, "preprocess", new Object[]{vert, false}, String.class, boolean.class);
        frag = Reflect.invoke(shader, "preprocess", new Object[]{frag, true}, String.class, boolean.class);

        if(Shader.prependVertexCode != null && Shader.prependVertexCode.length() > 0) vert = Shader.prependVertexCode + vert;
        if(Shader.prependFragmentCode != null && Shader.prependFragmentCode.length() > 0) frag = Shader.prependFragmentCode + frag;

        Reflect.set(shader, "vertexShaderSource", vert);
        Reflect.set(shader, "fragmentShaderSource", frag);

        Reflect.invoke(shader, "compileShaders", new Object[]{vert, frag}, String.class, String.class);
        if(Reflect.invoke(shader, "isCompiled")){
            Reflect.invoke(shader, "fetchAttributes");
            Reflect.invoke(shader, "fetchUniforms");
        }else{
            throw new IllegalArgumentException("Failed to compile shader: " + shader);
        }
    }
}
