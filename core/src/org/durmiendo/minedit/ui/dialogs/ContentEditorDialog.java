package org.durmiendo.minedit.ui.dialogs;

import arc.Core;
import arc.func.Prov;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.scene.ui.CheckBox;
import arc.scene.ui.ColorImage;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Log;
import com.google.common.collect.Tables;
import mindustry.ctype.Content;
import mindustry.gen.Icon;
import mindustry.graphics.Drawf;
import mindustry.type.Category;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.minedit.R;
import org.durmiendo.minedit.core.MVars;


import java.lang.reflect.*;


public class ContentEditorDialog extends BaseDialog {

    public ContentEditorDialog(String title) {
        super(title);
        super.setStyle(Styles.fullDialog);
    }

    public void clazz(Class<?> clazz, Object obj) {
        showFields(cont, clazz, obj);
        row();
        table(t -> {
            addCloseButton();
            t.button("Сохранить", Icon.save, () -> {
                //TODO: сохранение контента в файл
            }).minSize(200, 60);
            t.button("Загрузить", Icon.upload, () -> {
                //TODO: загрузка контента из файла
            }).minSize(200, 60);
        });
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
        row();
        table(t -> {
            addCloseButton();
            t.button("Сохранить", Icon.save,  () -> {
                //TODO: сохранение контента файла
            }).minSize(200, 60);
            t.button("Загрузить", Icon.upload,  () -> {
                //TODO: загрузка контента из файла
            }).minSize(200, 60);
            t.button("Добавить", Icon.add,  () -> {
                Log.info("add content");
                MVars.cc.loadCotent(content);
                cont.clear();
                hide();
            }).minSize(200, 60);
        });
    }

    public TextField search;

    public void showFields(Table cont, Class<?> c, Object obj) {
        cont.pane(p -> {
            p.table(s -> {
                s.table(se -> {
                    se.label(() -> "Поиск: ");
                    search = new TextField();
                    se.add(search);
                });
                s.row();
                Table f = s.table(t -> {
                    viewFields(t, c, obj, null);
                }).get();
                search.changed(() -> {
                    f.clear();
                    viewFields(f, c, obj, search.getText());
                });
            });
        }).scrollX(false).style(Styles.noBarPane);
    }

    public void viewFields(Table t, Class<?> c, Object obj, String s) {
        for (Field fi : R.getFields(c)) {
            if (s != null && !fi.getName().contains(s)) continue;
            if (R.getType(fi).isPrimitive() || R.getType(fi).isAssignableFrom(String.class)) {
                primitiveField(t, fi, obj);
            } else if (R.getType(fi).isAssignableFrom(Seq.class)) {
                seqField(t, fi, obj);
            } else if (R.getType(fi).isArray()) {
                arrayField(t, fi, obj);
            } else if (R.getType(fi).isAssignableFrom(Color.class)) {
                colorField(t, fi, obj);
            } else if (R.getType(fi).isAssignableFrom(Enum.class)) {
                enumField(t, fi, obj);
            } else {
                classField(t, fi, obj);
            }
            t.row();
        }
    }

    private void enumField(Table t, Field fi, Object obj) {
    }

    private void colorField(Table t, Field fi, Object obj) {
        t.table(c -> {
            Color col = (Color) R.getField(fi, obj);
            c.table(k -> {
                k.label(() -> "[orange]" + fi.getType().getSimpleName() + "[white] " + R.getName(fi) + ": [#" + col + "]" + col + "[white]      ");
            });
            c.table(cs -> {
                cs.button(Icon.settings, () -> {
                    BaseDialog bd = new BaseDialog("Изменение цвета");
                    bd.cont.label(() -> "[#" + col + "]" + col);
                    bd.cont.row();
                    bd.cont.table(k -> {
                        k.label(() -> "r:  ");
                        k.slider(0, 1, 1f / 256f, col.r, r -> {
                            col.r = r;
                        });
                        k.label(() -> "g:  ");
                        k.slider(0, 1, 1f / 256f, col.g, g -> {
                            col.g = g;
                        });
                    });

                    bd.cont.table(k -> {
                        k.label(() -> "b:  ");
                        k.slider(0, 1, 1f / 256f, col.b, b -> {
                            col.b = b;
                        });
                        k.label(() -> "a:  ");
                        k.slider(0, 1, 1f / 256f, col.a, a -> {
                            col.a = a;
                        });
                    });
                    bd.addCloseButton();
                    bd.show();
                });
            }).right();
        }).left();
    }

    private void arrayField(Table t, Field fi, Object obj) {
    }

    private void classField(Table t, Field fi, Object obj) {
        t.table(c -> {
            if (R.getField(fi, obj) != null) c.label(() -> "[orange]" + fi.getType().getSimpleName() + "[white] " + R.getName(fi) + ": " + R.getField(fi, obj).toString() + "     ");
            else c.label(() -> "[orange]" + fi.getType().getSimpleName() + "[white] " +R.getName(fi) + ": [yellow]null[white]" + "     ");
            c.button(Icon.settings, () -> {
                ContentEditorDialog bd = new ContentEditorDialog("Изменение класса");
                bd.clazz(R.getType(fi), R.getField(fi, obj));
                bd.show();
            });
        }).left();
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
            });
            c.button(Icon.settings, () -> {
                MVars.ui.seqEditorDialog.showSeq(c, fi, obj, t);
            });
        }).left();
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
            c.label(() -> "[orange]" +  fi.getType().getSimpleName() + "[white] " + fi.getName() + ":      ");

            if (fi.getType().isAssignableFrom(boolean.class)) {
                CheckBox cb = new CheckBox("");
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
                c.add(cb);
                return;
            }

            TextField tf = new TextField("");
            try {
                fi.setAccessible(true);
                if (fi.get(obj) != null) tf.setText(fi.get(obj).toString());
                else tf.setText("");
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
            c.add(tf);
        }).left();
    }
}