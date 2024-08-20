package org.durmiendo.sueno.ui.elements;

import arc.func.Boolc;
import arc.scene.style.Drawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;

import static arc.Core.scene;

public class Switch extends ImageButton {
    private Image image;
    private Cell imageCell;
    private SwitchStyle style;
    private boolean enabled = false;
    public Boolc listener;

    public Switch(Drawable icon){
        this(icon, scene.getStyle(SwitchStyle.class));
    }

    public Switch(Drawable icon, SwitchStyle style){
        super(icon);
        clearChildren();
        image(icon).pad(4);
        setSize(getPrefWidth(), getPrefHeight());
    }

    public void click(Boolc l){
        listener = l;
    }

    @Override
    public void fireClick() {
        super.fireClick();
        enabled = !enabled;
        listener.get(enabled);
    }

    @Override
    public SwitchStyle getStyle(){
        return style;
    }

    @Override
    public void setStyle(ButtonStyle style){
        if(!(style instanceof SwitchStyle)) throw new IllegalArgumentException("style must be a CheckBoxStyle.");
        super.setStyle(style);
        this.style = (SwitchStyle) style;
    }

    @Override
    public void draw(){
        Drawable checkbox = null;
        if(isDisabled()){
            if(enabled && style.checkboxOnDisabled != null)
                checkbox = style.checkboxOnDisabled;
            else
                checkbox = style.checkboxOffDisabled;
        }
        if(checkbox == null){
            if(enabled && isOver() && style.checkboxOnOver != null)
                checkbox = style.checkboxOnOver;
            else if(enabled && style.checkboxOn != null)
                checkbox = style.checkboxOn;
            else if(isOver() && style.checkboxOver != null && !isDisabled())
                checkbox = style.checkboxOver;
            else
                checkbox = style.checkboxOff;
        }
        image.setDrawable(checkbox);
        super.draw();
    }

    public Image getImage(){
        return image;
    }

    public Cell getImageCell(){
        return imageCell;
    }

    public static class SwitchStyle extends ImageButton.ImageButtonStyle{
        public Drawable checkboxOn, checkboxOff;
        /** Optional. */
        public Drawable checkboxOver, checkboxOnDisabled, checkboxOffDisabled, checkboxOnOver;
    }
}