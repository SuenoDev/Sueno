package org.durmiendo.minedit.ui.dialogs;

import arc.Core;
import arc.func.Boolp;
import arc.func.Prov;
import arc.scene.event.Touchable;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;
import org.durmiendo.minedit.core.MVars;

public class ChoiceDialog extends BaseDialog {
    Table t1 = new Table(){{
        label(() -> "Выберите тип контента: \n");
        row();
        button("Предметы", () -> {

        }).minSize(120, 20);
        button("Блоки", () -> {
            MVars.ui.contentEditorDialog.show();
            MVars.ui.contentEditorDialog.cont("mindustry.world.blocks.defense.Wall", "distributionor", "minedit");
        }).minSize(120, 20);
        row();
        button("Юниты", () -> {

        }).minSize(120, 20);
        button("Планеты", () -> {
        }).minSize(120, 20);
    }};
    Table t2 = new Table(){{
        label(() -> "2   ");
    }};
    Table t3 = new Table(){{
        label(() -> "3   ");
    }};

    Cell<ScrollPane> sp;
    public ChoiceDialog(String title) {
        super(title);
        cont.left().table(t -> {
            t.row();
            t.buttonRow("Создать", Icon.add, () -> {
                sp.get().setScrollY(0f);
            }).minSize(130, 20);
            t.row();
            t.buttonRow("Изменить", Icon.edit, () -> {
                sp.get().setScrollY(Core.graphics.getHeight());
            }).minSize(130, 20);
            t.row();
            t.buttonRow("Сохранить", Icon.save, () -> {
                sp.get().setScrollY(Core.graphics.getHeight()*2f);
            }).minSize(130, 20);
            t.row();
        });

        cont.left().table(t -> {
            sp = t.pane(p -> {
                p.add(t1).minSize(800, Core.graphics.getHeight());
                p.row();
                p.add(t2).minSize(800, Core.graphics.getHeight());
                p.row();
                p.add(t3).minSize(800, Core.graphics.getHeight());
                p.row();

            }).minWidth(Core.graphics.getWidth()-135f).scrollX(false).scrollY(true).disabled(true);
        });
        addCloseButton();

    }
}
