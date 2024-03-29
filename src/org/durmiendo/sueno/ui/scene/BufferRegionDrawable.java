package org.durmiendo.sueno.ui.scene;

import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.FrameBuffer;
import arc.scene.style.TextureRegionDrawable;

public class BufferRegionDrawable extends TextureRegionDrawable {
    public FrameBuffer frameBuffer;

    int bw = -1, bh = -1;

    public BufferRegionDrawable(FrameBuffer buffer) {
        frameBuffer = buffer;

        bufferSizeChanged();
    }

    public void bufferSizeChanged() {
        if (region == null) {
            set(new TextureRegion(frameBuffer.getTexture()));
            region.flip(false, true);
        } if (bw != frameBuffer.getWidth()  || bh != frameBuffer.getHeight()) {
            bw = frameBuffer.getWidth();
            bh = frameBuffer.getHeight();

            region.set(frameBuffer.getTexture(), 0, 0, frameBuffer.getWidth(), frameBuffer.getHeight());
            region.flip(false, true);
            set(region);
        }
    }
}
