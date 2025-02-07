package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.SpriteCache;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mat;
import arc.math.Mathf;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.ArcRuntimeException;

import java.nio.FloatBuffer;
import java.util.Arrays;

public class SspriteCache extends SpriteCache {
    //xy + color + uv + mix_color + rotation
    static final int VERTEX_SIZE = 2 + 1 + 2 + 1;

    private static final float[] tempVertices = new float[VERTEX_SIZE * 6];

    private final Mesh mesh;
    private final Mat transformMatrix = new Mat();
    private final Mat projectionMatrix = new Mat();
    private final Mat combinedMatrix = new Mat();
    private final Shader shader;
    private final Seq<Texture> textures = new Seq<>(8);
    private final IntSeq counts = new IntSeq(8);
    private final Color color = new Color(1, 1, 1, 1);
    /** Number of render calls since the last {@link #begin()}. **/
    public int renderCalls = 0;
    /** Number of rendering calls, ever. Will not be reset unless set manually. **/
    public int totalRenderCalls = 0;
    private boolean drawing;
    private Seq<SCache> caches;
    private SCache currentCache;
    private float colorPacked = Color.whiteFloatBits;
    private Shader customShader = null;

    /** Creates a cache that uses indexed geometry and can contain up to 1000 images. */
    public SspriteCache(){
        this(1000, false);
    }

    /**
     * Creates a cache with the specified size, using a default shader.
     * @param size The maximum number of images this cache can hold. The memory required to hold the images is allocated up front.
     * Max of 8191 if indices are used.
     * @param useIndices If true, indexed geometry will be used.
     */
    public SspriteCache(int size, boolean useIndices){
        this(size, 16, createDefaultShader(), useIndices);
    }

    public SspriteCache(int size, int cacheSize, boolean useIndices){
        this(size, cacheSize, createDefaultShader(), useIndices);
    }


    public SspriteCache(int size, int cacheSize, Shader shader, boolean useIndices){
        this.shader = shader;

        if(useIndices && size > 8191)
            throw new IllegalArgumentException("Can't have more than 8191 sprites per batch: " + size);

        mesh = new Mesh(true, size * (useIndices ? 4 : 6), useIndices ? size * 6 : 0,
                VertexAttribute.position,
                VertexAttribute.color,
                VertexAttribute.texCoords,
                SBatch.rot
        );
        mesh.setAutoBind(false);
        caches = new Seq<>(cacheSize);

        if(useIndices){
            int length = size * 6;
            short[] indices = new short[length];
            short j = 0;
            for(int i = 0; i < length; i += 6, j += 4){
                indices[i] = j;
                indices[i + 1] = (short)(j + 1);
                indices[i + 2] = (short)(j + 2);
                indices[i + 3] = (short)(j + 2);
                indices[i + 4] = (short)(j + 3);
                indices[i + 5] = j;
            }
            mesh.setIndices(indices);
        }

        projectionMatrix.setOrtho(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
    }

    public static Shader createDefaultShader(){
        return SBatch.cs();
    }


    public void setColor(float r, float g, float b, float a){
        color.set(r, g, b, a);
        colorPacked = color.toFloatBits();
    }

    public Color getColor(){
        return color;
    }

    /** Sets the color used to tint images when they are added to the SpriteCache. Default is {@link Color#white}. */
    public void setColor(Color tint){
        color.set(tint);
        colorPacked = tint.toFloatBits();
    }

    public float getPackedColor(){
        return colorPacked;
    }

    /**
     * Sets the color of this sprite cache, expanding the alpha from 0-254 to 0-255.
     * @see Color#toFloatBits()
     */
    public void setPackedColor(float packedColor){
        color.abgr8888(packedColor);
        colorPacked = packedColor;
    }

    /** Starts the definition of a new cache, allowing the add and {@link #endCache()} methods to be called. */
    public void beginCache(){
        if(drawing) throw new IllegalStateException("end must be called before beginCache");
        if(currentCache != null) throw new IllegalStateException("endCache must be called before begin.");
        mesh.getVerticesBuffer().position(caches.isEmpty() ? 0 : caches.peek().offset + caches.peek().maxCount);
        currentCache = new SCache(caches.size, mesh.getVerticesBuffer().position());
        caches.add(currentCache);
        mesh.getVerticesBuffer().limit(mesh.getVerticesBuffer().capacity());
    }

    /**
     * Starts the redefinition of an existing cache, allowing the add and {@link #endCache()} methods to be called. It cannot have more entries added to it than when it was first created.
     * To do that, use {@link #clear()} and then {@link #begin()}.
     */
    public void beginCache(int cacheID){
        if(drawing) throw new IllegalStateException("end must be called before beginCache");
        if(currentCache != null) throw new IllegalStateException("endCache must be called before begin.");
        currentCache = caches.get(cacheID);
        Arrays.fill(currentCache.counts, 0);
        mesh.getVerticesBuffer().position(currentCache.offset);
    }

    /** Ends the definition of a cache, returning the cache ID to be used with {@link #draw(int)}. */
    public int endCache(){
        if(currentCache == null) throw new IllegalStateException("beginCache must be called before endCache.");
        SCache cache = currentCache;
        int cacheCount = mesh.getVerticesBuffer().position() - cache.offset;
        if(cache.textures == null){
            // New cache.
            cache.maxCount = cacheCount;
            cache.textureCount = textures.size;
            cache.textures = textures.toArray(Texture.class);
            cache.counts = new int[cache.textureCount];
            for(int i = 0, n = counts.size; i < n; i++)
                cache.counts[i] = counts.get(i);
        }else{
            // Redefine existing cache.
            if(cacheCount > cache.maxCount){
                throw new ArcRuntimeException(
                        "If a cache is not the last created, it cannot be redefined with more entries than when it was first created: "
                                + cacheCount + " (" + cache.maxCount + " max)");
            }

            cache.textureCount = textures.size;

            if(cache.textures.length < cache.textureCount) cache.textures = new Texture[cache.textureCount];
            for(int i = 0, n = cache.textureCount; i < n; i++)
                cache.textures[i] = textures.get(i);

            if(cache.counts.length < cache.textureCount) cache.counts = new int[cache.textureCount];
            for(int i = 0, n = cache.textureCount; i < n; i++)
                cache.counts[i] = counts.get(i);

            FloatBuffer vertices = mesh.getVerticesBuffer();
            vertices.position(0);
            SCache lastCache = caches.get(caches.size - 1);
            vertices.limit(lastCache.offset + lastCache.maxCount);
        }

        currentCache = null;
        textures.clear();
        counts.clear();

        //fixes teaVM bug, since it draws based on offset apparently
        if(Core.app.isWeb()){
            mesh.getVerticesBuffer().position(0);
        }

        return cache.id;
    }

    /** Invalidates all cache IDs and resets the SpriteCache so new caches can be added. */
    public void clear(){
        caches.clear();
        mesh.getVerticesBuffer().clear().flip();
    }

    /** Ensures that this cache can hold this amount of sprites. Only call at the end of cache.
     * @return number of new sprites actually reserved. */
    public int reserve(int sprites){
        if(currentCache == null) throw new IllegalStateException("beginCache must be called before ensureSize.");

        //size of each sprite
        int spriteSize = VERTEX_SIZE * (mesh.getNumIndices() > 0 ? 4 : 6);
        //currently used vertices
        int currentUsed = currentCache.maxCount;
        //vertices that need to be guaranteed
        int required = sprites * spriteSize;
        //number of extra vertices to reserve
        int toAdd = required - currentUsed;
        if(toAdd > 0){
            currentCache.maxCount += toAdd;
            mesh.getVerticesBuffer().position(currentCache.offset + currentCache.maxCount);
            return toAdd / spriteSize;
        }
        return 0;
    }

    /**
     * Adds the specified vertices to the cache. Each vertex should have 5 elements, one for each of the attributes: x, y, color,
     * u, and v. If indexed geometry is used, each image should be specified as 4 vertices, otherwise each image should be
     * specified as 6 vertices.
     */
    public void add(Texture texture, float[] vertices, int offset, int length){
        if(currentCache == null) throw new IllegalStateException("beginCache must be called before add.");
        if(mesh.getVerticesBuffer().position() + length >= mesh.getVerticesBuffer().limit())
            throw new IllegalStateException("Out of vertex space! Size: " + mesh.getVerticesBuffer().capacity() + " Required: " + (mesh.getVerticesBuffer().position() + length));

        int verticesPerImage = mesh.getNumIndices() > 0 ? 4 : 6;
        int count = length / (verticesPerImage * VERTEX_SIZE) * 6;
        int lastIndex = textures.size - 1;
        if(lastIndex < 0 || textures.get(lastIndex) != texture){
            textures.add(texture);
            counts.add(count);
        }else
            counts.incr(lastIndex, count);

        mesh.getVerticesBuffer().put(vertices, offset, length);
    }

    /** Adds the specified region to the cache. */
    public void add(TextureRegion region, float x, float y){
        add(region, x, y, region.width, region.height);
    }

    /** Adds the specified region to the cache. */
    public void add(TextureRegion region, float x, float y, float width, float height){
        final float fx2 = x + width;
        final float fy2 = y + height;
        final float u = region.u;
        final float v = region.v2;
        final float u2 = region.u2;
        final float v2 = region.v;

        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = colorPacked;
        tempVertices[3] = u;
        tempVertices[4] = v;

        tempVertices[5] = x;
        tempVertices[6] = fy2;
        tempVertices[7] = colorPacked;
        tempVertices[8] = u;
        tempVertices[9] = v2;

        tempVertices[10] = fx2;
        tempVertices[11] = fy2;
        tempVertices[12] = colorPacked;
        tempVertices[13] = u2;
        tempVertices[14] = v2;

        if(mesh.getNumIndices() > 0){
            tempVertices[15] = fx2;
            tempVertices[16] = y;
            tempVertices[17] = colorPacked;
            tempVertices[18] = u2;
            tempVertices[19] = v;
            add(region.texture, tempVertices, 0, 20);
        }else{
            tempVertices[15] = fx2;
            tempVertices[16] = fy2;
            tempVertices[17] = colorPacked;
            tempVertices[18] = u2;
            tempVertices[19] = v2;

            tempVertices[20] = fx2;
            tempVertices[21] = y;
            tempVertices[22] = colorPacked;
            tempVertices[23] = u2;
            tempVertices[24] = v;

            tempVertices[25] = x;
            tempVertices[26] = y;
            tempVertices[27] = colorPacked;
            tempVertices[28] = u;
            tempVertices[29] = v;
            add(region.texture, tempVertices, 0, 30);
        }
    }

    /** Adds the specified region to the cache. */
    public void add(TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                    float scaleX, float scaleY, float rotation){

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if(scaleX != 1 || scaleY != 1){
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counter clockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if(rotation != 0){
            final float cos = Mathf.cosDeg(rotation);
            final float sin = Mathf.sinDeg(rotation);

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        }else{
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        final float u = region.u;
        final float v = region.v2;
        final float u2 = region.u2;
        final float v2 = region.v;

        tempVertices[0] = x1;
        tempVertices[1] = y1;
        tempVertices[2] = colorPacked;
        tempVertices[3] = u;
        tempVertices[4] = v;
        tempVertices[5] = rotation;

        tempVertices[6] = x2;
        tempVertices[7] = y2;
        tempVertices[8] = colorPacked;
        tempVertices[9] = u;
        tempVertices[10] = v2;
        tempVertices[11] = rotation;

        tempVertices[12] = x3;
        tempVertices[13] = y3;
        tempVertices[14] = colorPacked;
        tempVertices[15] = u2;
        tempVertices[16] = v2;
        tempVertices[17] = rotation;

        if(mesh.getNumIndices() > 0){
            tempVertices[18] = x4;
            tempVertices[19] = y4;
            tempVertices[20] = colorPacked;
            tempVertices[21] = u2;
            tempVertices[22] = v;
            tempVertices[23] = rotation;
            add(region.texture, tempVertices, 0, 20);
        }else{
            tempVertices[18] = x3;
            tempVertices[19] = y3;
            tempVertices[20] = colorPacked;
            tempVertices[21] = u2;
            tempVertices[22] = v2;
            tempVertices[23] = rotation;

            tempVertices[24] = x4;
            tempVertices[25] = y4;
            tempVertices[26] = colorPacked;
            tempVertices[27] = u2;
            tempVertices[28] = v;
            tempVertices[29] = rotation;

            tempVertices[30] = x1;
            tempVertices[31] = y1;
            tempVertices[32] = colorPacked;
            tempVertices[33] = u;
            tempVertices[34] = v;
            tempVertices[35] = rotation;
            add(region.texture, tempVertices, 0, 36);
        }
    }

    /** Prepares the OpenGL state for SpriteCache rendering. */
    public void begin(){
        if(drawing) throw new IllegalStateException("end must be called before begin.");
        if(currentCache != null) throw new IllegalStateException("endCache must be called before begin");
        renderCalls = 0;
        combinedMatrix.set(projectionMatrix).mul(transformMatrix);
        Shader shader = customShader != null ? customShader : this.shader;
        shader.bind();
        shader.setUniformMatrix4("u_projectionViewMatrix", combinedMatrix);
        mesh.bind(shader);
        drawing = true;
    }

    /** Completes rendering for this SpriteCache. */
    public void end(){
        if(!drawing) throw new IllegalStateException("begin must be called before end.");
        drawing = false;

        Gl.depthMask(true);
        if(customShader != null)
            mesh.unbind(customShader);
        else
            mesh.unbind(shader);
    }

    /** Draws all the images defined for the specified cache ID. */
    public void draw(int cacheID){
        if(!drawing) throw new IllegalStateException("SpriteCache.begin must be called before draw.");

        SCache cache = caches.get(cacheID);
        int verticesPerImage = mesh.getNumIndices() > 0 ? 4 : 6;
        int offset = cache.offset / (verticesPerImage * VERTEX_SIZE) * 6;
        Texture[] textures = cache.textures;
        int[] counts = cache.counts;
        int textureCount = cache.textureCount;
        Shader shader = customShader != null ? customShader : this.shader;
        for(int i = 0; i < textureCount; i++){
            int count = counts[i];
            textures[i].bind();

            mesh.render(shader, Gl.triangles, offset, count);
            offset += count;
        }
        renderCalls += textureCount;
        totalRenderCalls += textureCount;
    }

    /**
     * Draws a subset of images defined for the specified cache ID.
     * @param offset The first image to render.
     * @param length The number of images from the first image (inclusive) to render.
     */
    public void draw(int cacheID, int offset, int length){
        if(!drawing) throw new IllegalStateException("SpriteCache.begin must be called before draw.");

        SCache cache = caches.get(cacheID);
        offset = offset * 6 + cache.offset;
        length *= 6;
        Texture[] textures = cache.textures;
        int[] counts = cache.counts;
        int textureCount = cache.textureCount;
        for(int i = 0; i < textureCount; i++){
            textures[i].bind();
            int count = counts[i];
            if(count > length){
                i = textureCount;
                count = length;
            }else
                length -= count;
            if(customShader != null)
                mesh.render(customShader, Gl.triangles, offset, count);
            else
                mesh.render(shader, Gl.triangles, offset, count);
            offset += count;
        }
        renderCalls += cache.textureCount;
        totalRenderCalls += textureCount;
    }

    /** Releases all resources held by this SpriteCache. */
    @Override
    public void dispose(){
        mesh.dispose();
        if(shader != null) shader.dispose();
    }

    public Mat getProjectionMatrix(){
        return projectionMatrix;
    }

    public void setProjectionMatrix(Mat projection){
        if(drawing) throw new IllegalStateException("Can't set the matrix within begin/end.");
        projectionMatrix.set(projection);
    }

    public Mat getTransformMatrix(){
        return transformMatrix;
    }

    public void setTransformMatrix(Mat transform){
        if(drawing) throw new IllegalStateException("Can't set the matrix within begin/end.");
        transformMatrix.set(transform);
    }

    /**
     * Sets the shader to be used in a GLES 2.0 environment. Vertex position attribute is called "a_position", the texture
     * coordinates attribute is called called "a_texCoords", the color attribute is called "a_color". The projection matrix is
     * uploaded via a mat4 uniform called "u_proj", the transform matrix is uploaded via a uniform called "u_trans", the combined
     * transform and projection matrx is is uploaded via a mat4 uniform called "u_projTrans". The texture sampler is passed via a
     * uniform called "u_texture".
     * <p>
     * Call this method with a null argument to use the default shader.
     * @param shader the {@link Shader} or null to use the default shader.
     */
    public void setShader(Shader shader){
        customShader = shader;
    }

    public boolean isDrawing(){
        return drawing;
    }

    private static class SCache {
        final int id;
        final int offset;
        int maxCount;
        int textureCount;
        Texture[] textures;
        int[] counts;

        public SCache(int id, int offset){
            this.id = id;
            this.offset = offset;
        }
    }
}
