package org.durmiendo.minedit.ui;


import mindustry.core.UI;
import org.durmiendo.minedit.ui.dialogs.ContentEditorDialog;
import org.durmiendo.minedit.ui.dialogs.ChoiceDialog;
import org.durmiendo.minedit.ui.dialogs.SeqEditorDialog;

public class MUI extends UI {
    public ChoiceDialog choiceDialog;
    public ContentEditorDialog contentEditorDialog;
    public SeqEditorDialog seqEditorDialog;

    public MUI() {

    }

    public void build() {
        choiceDialog = new ChoiceDialog("Изменить");
        contentEditorDialog = new ContentEditorDialog("Реадактор контента");
        seqEditorDialog = new SeqEditorDialog("Редактор Seq");}
}
