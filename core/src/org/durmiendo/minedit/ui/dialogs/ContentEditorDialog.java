package org.durmiendo.minedit.ui.dialogs;

import arc.scene.ui.CheckBox;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Log;
import mindustry.ctype.Content;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import org.durmiendo.minedit.R;
import org.durmiendo.minedit.core.MVars;


import java.lang.reflect.*;


public class ContentEditorDialog extends BaseDialog {

    public ContentEditorDialog(String title) {
        super(title);
        super.setStyle(Styles.fullDialog);
    }

    public void cont(String type, String name, String modName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(type);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Content content = MVars.cc.generate(clazz, name, modName);
        content.init();
        showFields(cont, clazz, content);
        table(t -> {
            addCloseButton();
            button("Сохранить", Icon.save,  () -> {
                //TODO: сохранение контента в виде файла
            }).minSize(200, 60);
            button("Добавить", Icon.add,  () -> {
                Log.info("add content");
                MVars.cc.loadCotent(content);
            }).minSize(200, 60);
        });
    }

    public void showFields(Table cont, Class<?> c, Object obj) {
        cont.pane(p -> {
            p.table(s -> {
                for (Field fi : R.getFields(c)) {
                    if (R.getType(fi).isPrimitive()) {
                        primitiveField(s, fi, obj);
                    } else if (R.getType(fi).isAssignableFrom(Seq.class)) {
                        seqField(s, fi, obj);
                    }
                    s.row();
                }
            });
        });
    }

    private void seqField(Table l, Field fi, Object obj) {
        l.table(c -> {
            Type t = getSeqType(fi);
            if (t == null) c.label(() -> "[orange]Seq[white]<[yellow]null[white]> " + R.getName(fi) + "     ");
            else c.label(() -> {
                try {
                    return "[orange]Seq[white]<" + Class.forName(t.getTypeName()).getSimpleName()+ "> " + R.getName(fi);
                } catch (ClassNotFoundException e) {
                    Log.warn("seq get type error: " + e);
                }
                return null;
            }).left();
            c.button(Icon.settings, () -> {
                MVars.ui.seqEditorDialog.showSeq(c, fi, obj, t);
            }).right();
        });

    }

    private Type getSeqType(Field fi) {
        if (fi != null) {
            Type fieldGenericType = fi.getGenericType();
            if (fieldGenericType instanceof ParameterizedType pt) {
                Type[] actualTypeArguments = pt.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    return actualTypeArguments[0];
                }
            }
        }
        return null;
    }

    private void primitiveField(Table p, Field fi, Object obj) {
        p.table(c -> {
            fi.setAccessible(true);
            c.label(() -> "[orange]" +  fi.getType().getName() + "[white] " + fi.getName() + ":      ").left();

            if (fi.getType().isAssignableFrom(boolean.class)) {
                CheckBox cb = new CheckBox("");
                cb.right();
                try {
                    cb.setChecked(fi.getBoolean(obj));
                } catch (IllegalAccessException e) {
                    Log.warn("error get boolean var: " + e);
                }
                cb.changed(() -> {
                    try {
                        fi.setAccessible(true);
                        fi.setBoolean(obj, cb.isChecked());
                    } catch (Exception e) {
                        Log.warn("error change boll var: " + e);
                    }
                });
                c.left().add(cb);
                return;
            }

            TextField tf = new TextField("");
            tf.setAlignment(Align.left);
            try {
                fi.setAccessible(true);
                tf.setText(fi.get(obj).toString());
            } catch (IllegalAccessException e) {
                Log.warn("error get primitive var: " + e);
            }
            if (fi.getType().isAssignableFrom(char.class)) {
                tf.setMaxLength(1);
            }

            tf.changed(() -> {
                try {
                    if (fi.getType().isAssignableFrom(float.class)) {
                        float f = Float.parseFloat(tf.getText());
                        fi.setAccessible(true);
                        fi.setFloat(obj, f);
                    } else if (fi.getType().isAssignableFrom(int.class)) {
                        int f = Integer.parseInt(tf.getText());
                        fi.setAccessible(true);
                        fi.setInt(obj, f);
                    } else if (fi.getType().isAssignableFrom(double.class)) {
                        double f = Double.parseDouble(tf.getText());
                        fi.setAccessible(true);
                        fi.setDouble(obj, f);
                    } else if (fi.getType().isAssignableFrom(long.class)) {
                        long f = Long.parseLong(tf.getText());
                        fi.setAccessible(true);
                        fi.setLong(obj, f);
                    } else if (fi.getType().isAssignableFrom(short.class)) {
                        short f = Short.parseShort(tf.getText());
                        fi.setAccessible(true);
                        fi.setShort(obj, f);
                    } else if (fi.getType().isAssignableFrom(char.class)) {
                        char f = tf.getText().charAt(0);
                        fi.setAccessible(true);
                        fi.setChar(obj, f);
                    } else if (fi.getType().isAssignableFrom(byte.class)) {
                        byte f = Byte.parseByte(tf.getText());
                        fi.setAccessible(true);
                        fi.setByte(obj, f);
                    } else if (fi.getType().isAssignableFrom(String.class)) {
                        String f = tf.getText();
                        fi.setAccessible(true);
                        fi.set(obj, f);
                    }
                } catch (Exception e) {
                    Log.warn("error primitives: " + e);
                }
            });
            c.left().add(tf);
        });
    }
}