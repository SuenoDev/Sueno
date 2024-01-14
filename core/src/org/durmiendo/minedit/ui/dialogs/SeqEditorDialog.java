package org.durmiendo.minedit.ui.dialogs;

import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class SeqEditorDialog extends BaseDialog {
    public SeqEditorDialog(String title) {
        super(title);
    }

    public void showSeq(Table p, Field fi, Object obj, Type t) {




        addCloseButton();
    }
}
