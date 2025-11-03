package org.durmiendo.sueno.ui.dialogs;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.GL20;
import arc.graphics.Gl;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.graphics.g2d.Lines;
import arc.graphics.gl.Shader;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.ObjectFloatMap;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Buffers;
import arc.util.Log;
import arc.util.Reflect;
import arc.util.pooling.Pools;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.BorderImage;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import org.checkerframework.checker.units.qual.min;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.graphics.DynamicTexture;
import org.durmiendo.sueno.utils.SLog;
import org.durmiendo.sueno.utils.SStrings;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DynTexDebugger extends BaseDialog {
    public DynamicTexture dynTex;
    
    private TextField area;
    private int[] errLine = new int[]{};

    public DynTexDebugger() {
        super("DynTexDebugger");
        
        titleTable.clear();
        titleTable.table(t -> {
            t.table(l -> {
                l.add(new BorderImage(){{
                    setDrawable(Icon.settingsSmall);
                    border(Pal.gray);
                    thickness = 0f;
                }}).size(32f);
                l.label(() -> "DynTexDebugger").padLeft(12f).growX().center();
            }).right().growX().padLeft(6f);
            t.table(b -> {
                b.button(Icon.playSmall, Styles.flati, () -> {
                    try {
                        Log.info("recompile " + area.getText().length() + " chars of shader");
                        
                        dynTex.shader = new Shader(
                                dynTex.shader.getVertexShaderSource(),
                                area.getText()
                        ){@Override
                            public void apply() {
                                dynTex.apply.get(dynTex.shader);
                            }
                        };
                        
                        errLine = new int[]{};
                    } catch (Exception e) {
//                        errLine = errLines(e.getMessage());
//                        Log.info("error compiling shader, line " + Arrays.toString(errLine));
                        Log.err(e);
                    }
                }).size(32f);
                b.button("@back", Styles.flatt, this::hide)
                        .size(120f, 32f).right().padRight(4f).padLeft(4f);
            }).right();
        }).growX();
        
        titleTable.row();
        titleTable.image(Tex.whiteui, Pal.gray).growX().height(3f).pad(0f);
        
        addCloseListener();

        hidden(this::destroy);
        shown(() -> {
            build();
        });
//        resized(this::build);
    }
    

        

    
    private void destroy() {
    
    }
    
    
    private void build() {
        cont.clear();
        cont.table(base -> {
            base.table(left -> {
                float hy = dynTex.width / 150f;
                left.table(t -> {
                    Image image = new Image(
                            new TextureRegionDrawable(dynTex.getTextureRegion())
                    );
                    image.update(() -> {
                        dynTex.forceDraw();
                    });
                    image.clicked(() -> {
                       SVars.ui.dynTexVisible.show(dynTex);
                    });
                    image.setSize(dynTex.height/hy, 150f);
                    t.add(image).size(dynTex.height/hy, 150f);
                }).top();
                left.row();
                Slider slider = new Slider(1f, 6f, 0.1f, false);
                slider.setValue(SVars.quality);
                slider.moved(v -> {
                    SVars.quality = v;
                });
                
                Table table = new Table(tt -> {
                    tt.left().label(() -> "Quality: " + SStrings.fixed(SVars.quality, 1)).touchable(Touchable.disabled).padLeft(5f).growX();
                });

                left.stack(
                        slider, table
                ).growX();
                
                left.row();
                
                left.row();
                left.table(t -> {}).grow();
            }).growY().width(150f).left();
            base.image(Tex.whiteui, Pal.gray).growY().width(3f).pad(0f).left();
            base.table(editor -> {
                ScrollPane[] pp = new ScrollPane[]{
                        new ScrollPane(editor),
                };
                
                String source = dynTex.shader.getFragmentShaderSource();
                String real = source.split("\n", 9)[8];
                area = new TextArea(real, SVars.ui.baseEditorStyle){
                    @Override
                    public float getPrefHeight() {
                        int count = 5;
                        for(int i = 0; i < text.length(); i++){
                            if(text.charAt(i) == '\n'){
                                count++;
                            }
                        }
                        setHeight(count * Fonts.def.getLineHeight());
                        return height;
                    }
                    {
                        renderOffset = 30f;
                        setTextFieldListener((field, charter) -> {
                            if (charter == '\n' || charter == '\r') {
                                Reflect.set(pp[0], "areaHeight", getPrefHeight()-1f);
                                pp[0].layout();
                                pp[0].setScrollYForce(pp[0].getScrollY()+Fonts.def.getLineHeight());
                            }
                        });
                    }
                    
                    @Override
                    protected void calculateOffsets(){
                        
                        if(!this.text.equals(lastText)){
                            this.lastText = text;
                            Font font = style.font;
                            float maxWidthLine = this.getWidth()
                                    - (style.background != null ? style.background.getLeftWidth() + style.background.getRightWidth() : 0);
                            linesBreak.clear();
                            int lineStart = 0;
                            int lastSpace = 0;
                            char lastCharacter;
                            GlyphLayout layout = Pools.obtain(GlyphLayout.class, GlyphLayout::new);
                            layout.ignoreMarkup = true;
                            for(int i = 0; i < text.length(); i++){
                                lastCharacter = text.charAt(i);
                                if(lastCharacter == '\n' || lastCharacter == '\r'){
                                    linesBreak.add(lineStart);
                                    linesBreak.add(i);
                                    lineStart = i + 1;
                                }else{
//                                    lastSpace = (continueCursor(i, 0) ? lastSpace : i);
//                                    layout.setText(font, text.subSequence(lineStart, i + 1));
//                                    if(layout.width > maxWidthLine){
//                                        if(lineStart >= lastSpace){
//                                            lastSpace = i - 1;
//                                        }
//                                        linesBreak.add(lineStart);
//                                        linesBreak.add(lastSpace + 1);
//                                        lineStart = lastSpace + 1;
//                                        lastSpace = lineStart;
//                                    }
                                }
                            }
                            Pools.free(layout);
                            // Add last line
                            if(lineStart < text.length()){
                                linesBreak.add(lineStart);
                                linesBreak.add(text.length());
                            }
                            showCursor();
                        }
                    }
                    
                    void showCursor(){
                        updateCurrentLine();
                        if(cursorLine != firstLineShowing){
                            int step = cursorLine >= firstLineShowing ? 1 : -1;
                            while(firstLineShowing > cursorLine || firstLineShowing + linesShowing - 1 < cursorLine){
                                firstLineShowing += step;
                            }
                        }
                    }
                    
                    void updateCurrentLine(){
                        int index = calculateCurrentLineIndex(cursor);
                        int line = index / 2;
                        // Special case when cursor moves to the beginning of the line from the end of another and a word
                        // wider than the box
                        if(index % 2 == 0 || index + 1 >= linesBreak.size || cursor != linesBreak.items[index]
                                || linesBreak.items[index + 1] != linesBreak.items[index]){
                            if(line < linesBreak.size / 2 || text.length() == 0 || text.charAt(text.length() - 1) == '\r'
                                    || text.charAt(text.length() - 1) == '\n'){
                                cursorLine = line;
                            }
                        }
                    }
                    
                    private int calculateCurrentLineIndex(int cursor){
                        int index = 0;
                        while(index < linesBreak.size && cursor > linesBreak.items[index]){
                            index++;
                        }
                        return index;
                    }
                    
                    
                    
                    @Override
                    protected void drawText(Font font, float x, float y){
                        boolean had = font.getData().markupEnabled;
                        font.getData().markupEnabled = false;
                        Color def = font.getColor();
                        float offsetY = 0;
                        for(int i = firstLineShowing * 2; i < (firstLineShowing + linesShowing) * 2 && i < linesBreak.size; i += 2){
                            
                            
                            font.setColor(Color.gray);
                            font.draw(Integer.toString(i/2+1), x-33f, y + offsetY, 0, Align.left, false);
                            font.setColor(Color.white);
                            
                            
                            Lines.stroke(1.5f);
                            
                            Draw.color(Color.gray);
                            Lines.line(x-2f, y + offsetY, x-2f, y + offsetY - font.getLineHeight());
                            Draw.color(Color.white);
                            
//                            for (int k : errLine) {
//                                font.setColor((k-8) * 2 + firstLineShowing * 2 == i ? Color.red : Color.white);
//                            }
                            
                            font.draw(displayText, x, y + offsetY, linesBreak.items[i], linesBreak.items[i + 1], 0, Align.left, false);
                            
                            font.setColor(Color.white);
                            
                            offsetY -= font.getLineHeight();
                        }
                        font.setColor(def);
                        font.getData().markupEnabled = had;
                    }
                    
                    @Override
                    protected void drawCursor(Drawable cursorPatch, Font font, float x, float y){
                        float textOffset = cursor >= glyphPositions.size || cursorLine * 2 >= linesBreak.size ? 0
                                : glyphPositions.get(cursor) - glyphPositions.get(linesBreak.items[cursorLine * 2]);
                        cursorPatch.draw(x + textOffset + fontOffset + font.getData().cursorX - 1f,
                                y - font.getDescent() / 2 - (cursorLine - firstLineShowing + 1) * font.getLineHeight(), cursorPatch.getMinWidth()/1.5f,
                                font.getLineHeight());
                    }
                };
                
                pp[0].setWidget(area);
                
                editor.add(pp[0]).grow().left();
            }).grow();
        }).grow();
    }
    
    public int[] errLines(String text) {
        IntSeq numbersList = new IntSeq();
        
        if (text != null && !text.isEmpty()) {
            Pattern pattern = Pattern.compile("0\\((\\d+)\\)");
            Matcher matcher = pattern.matcher(text);
            
            while (matcher.find()) {
                try {
                    String numberStr = matcher.group(1);
                    numbersList.add(Integer.parseInt(numberStr));
                } catch (NumberFormatException e) {}
            }
        }
        
        int[] numbersArray = new int[numbersList.size];
        for (int i = 0; i < numbersList.size; i++) {
            numbersArray[i] = numbersList.get(i);
        }
        
        return numbersArray;
    }
}
