package org.durmiendo.sueno.ui.elements;

import arc.func.Boolc;
import arc.graphics.Color;
import arc.scene.style.Drawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.util.Scaling;

import static mindustry.ui.Styles.*;

public class Switch extends ImageButton {
    private SwitchStyle style;
    private boolean enabled = false;
    public Boolc listener;
    public Image image;

    public Switch(Drawable icon){
        this(icon, styleDefault);
    }

    public Switch(Drawable icon, boolean enabled){
        this(icon, styleDefault);
        this.enabled = enabled;
    }

    public boolean isPressed(){
        return enabled;
    }

    public Switch(Drawable icon, SwitchStyle style){
        super(icon, style);
        clearChildren();
        table(i -> {

        });
        add(image = new Image(icon, Scaling.stretch)).pad(4f);
        setSize(getPrefWidth(), getPrefHeight());
        clicked(() -> {
            enabled = !enabled;
            listener.get(enabled);
        });
    }

    public void click(Boolc l){
        listener = l;
    }

    @Override
    public SwitchStyle getStyle(){
        return style;
    }

    @Override
    public void setStyle(ButtonStyle style){
        if(!(style instanceof SwitchStyle)) style = styleDefault;
        super.setStyle(style);
        this.style = (SwitchStyle) style;
    }

    @Override
    public void draw() {
        super.draw();

    }

    public static class SwitchStyle extends ImageButton.ImageButtonStyle{

    }

    public static SwitchStyle styleDefault = new SwitchStyle(){{
        down = flatDown;
        up = none;
        over = flatOver;
        disabled = none;
        imageDisabledColor = Color.gray;
        imageUpColor = Color.white;
    }};
}