package org.durmiendo.minedit.core;

import arc.Core;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.PixmapPacker;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.Content;
import mindustry.mod.Mods;

import mindustry.type.Category;
import org.durmiendo.minedit.ui.MUI;

import java.lang.reflect.Field;

public class MVars {
    public MVars() {}

    /** Mod core object. **/
    public static MCore core;
    /** Mod data container. **/
    public static Mods.LoadedMod sueno;
    /** All mod UI there. **/
    public static MUI ui = new MUI();
    public static ContentController cc;

    public static void init() {
        cc = new ContentController();
    }
}