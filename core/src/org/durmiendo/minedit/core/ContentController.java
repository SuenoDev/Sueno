package org.durmiendo.minedit.core;

import arc.Events;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.ctype.Content;
import mindustry.game.EventType;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.fragments.BlockInventoryFragment;
import mindustry.ui.fragments.HudFragment;
import mindustry.ui.fragments.PlacementFragment;
import mindustry.world.Block;
import mindustry.world.Tile;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static mindustry.type.ItemStack.with;

public class ContentController {
    public Field contentMap;
    public Seq<Content>[] newContent;

    public void addCont(int ct, Content c) {
        //newContent[ct].add(c);
        try {
            contentMap.setAccessible(true);
            ((Seq<Content>[]) contentMap.get(Vars.content))[ct].add(c);
            contentMap.setAccessible(false);
        } catch (IllegalAccessException e) {
            Log.warn("add content error: " + e);
        }
    }

    public void removeCont(int ct, Content c) {
        newContent[ct].add(c);
        try {
            contentMap.setAccessible(true);
            ((Seq<Content>[]) contentMap.get(Vars.content))[ct].remove(c);
            contentMap.setAccessible(false);
        } catch (IllegalAccessException e) {
            Log.warn("add remove error: " + e);
        }
    }

    public ContentController() {
        try {
            contentMap = Vars.content.getClass().getDeclaredField("contentMap");
        } catch (NoSuchFieldException ex) {
            return;
        }
        newContent = new Seq[16];
        for (int i = 0; i < newContent.length; i++) {
            newContent[i] = new Seq<>();
        }
    }

    public void loadCotent(Content c){
        if (c instanceof Block){
            loadBlock((Block) c);
        }
    }

    private void loadBlock(Block b){
        if (true/*b.requirements == null || Arrays.equals(b.requirements, ItemStack.empty)*/) {
            b.requirements(Category.distribution, with(Items.sporePod, 69));
        }
        b.load();
        b.loadIcon();
        Log.info("loadBlock " + b);
    }


    public Content generate(Class clazz, String name, String modName) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor(String.class);
            Content b = (Content) constructor.newInstance(modName + "-" + name);
            b.init();
            return b;
        } catch ( NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            Log.warn("generate content " + clazz.getSimpleName() + " " + modName + "-" + name + " error: " + e);
        }
        return null;
    }
}
