package org.durmiendo.sueno.graphics;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.ForkJoinHolder;
import arc.graphics.g2d.SpriteBatch;
import arc.graphics.g2d.TextureRegion;
import arc.graphics.gl.Shader;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.struct.IntIntMap;
import arc.util.Log;
import arc.util.Structs;
import org.durmiendo.sueno.core.SVars;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;

/** Fast sorting implementation written by zxtej. Don't ask me how it works. */
public class SSortedSpriteBatch extends SpriteBatch {
    static ForkJoinHolder commonPool;

    boolean multithreaded = (Core.app.getVersion() >= 21 && !Core.app.isIOS()) || Core.app.isDesktop();
    int[] contiguous = new int[2048], contiguousCopy = new int[2048];
    DrawRequest[] copy = new DrawRequest[0];
    int[] locs = new int[contiguous.length];
    
    protected DrawRequest[] requests = new DrawRequest[10000];
    protected boolean sort;
    protected boolean flushing;
    protected float[] requestZ = new float[10000];
    protected int numRequests = 0;

    //new xy + color + uv + mix_color + rotation + uv2
    public static final int VERTEX_SIZEN = 2 + 1 + 2 + 1 + 1 + 2;
    public static final int SPRITE_SIZEN = 4 * VERTEX_SIZEN;

    //old xy + color + uv + mix_color
    public static final int VERTEX_SIZEO = 2 + 1 + 2 + 1;
    public static final int SPRITE_SIZEO = 4 * VERTEX_SIZEO;

    public static VertexAttribute rot = new VertexAttribute(1, "a_rotation");
    public static VertexAttribute uvn = new VertexAttribute(2, Shader.texcoordAttribute + "1");

    protected void set(String name, Object value) {
        try {
            Field tmpVertices = getClass().getField(name);
            tmpVertices.setAccessible(true);
            tmpVertices.set(this, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SSortedSpriteBatch() {
        int size = 4096;
        projectionMatrix.setOrtho(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());

        //xy + color + uv + mix_color + rotation + uv2
        mesh = new Mesh(true, false, size * 4, size * 6,
                VertexAttribute.position,
                VertexAttribute.color,
                VertexAttribute.texCoords,
                VertexAttribute.mixColor,
                rot,
                uvn
        );

        set("vertices", new float[SPRITE_SIZEN*4096]);

        int len = size * 6;
        short[] indices = new short[len];
        short j = 0;
        for(int i = 0; i < len; i += 6, j += 4){
            indices[i] = j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = j;
        }
        mesh.setIndices(indices);
        shader = SShaders.normalShader;
//        setShader(SShaders.normalShader);
    }

    {
        for(int i = 0; i < requests.length; i++){
            requests[i] = new DrawRequest();
        }

        if(multithreaded){
            try{
                commonPool = new ForkJoinHolder();
            }catch(Throwable t){
                multithreaded = false;
            }
        }
    }

    @Override
    protected void setSort(boolean sort){
        if(this.sort != sort){
            flush();
        }
        this.sort = sort;
    }

    @Override
    protected void setShader(Shader shader, boolean apply){
        if(!flushing && sort){
            throw new IllegalArgumentException("Shaders cannot be set while sorting is enabled. Set shaders inside Draw.run(...).");
        }
        super.setShader(shader, apply);
    }

    @Override
    protected void setBlending(Blending blending){
        this.blending = blending;
    }

    @Override
    protected void draw(Texture texture, float[] spriteVertices, int offset, int count){
        if(sort && !flushing){
            if(numRequests + count - offset >= this.requests.length) expandRequests();
            float[] requestZ = this.requestZ;
            DrawRequest[] requests = this.requests;

            for(int i = offset; i < count; i += SPRITE_SIZE){
                final DrawRequest req = requests[numRequests];
                requestZ[numRequests] = req.z = z;
                System.arraycopy(spriteVertices, i, req.vertices, 0, req.vertices.length);
                req.texture = texture;
                req.blending = blending;
                req.run = null;
                numRequests ++;
            }
        }else{
            offset = offset/SPRITE_SIZEO*SPRITE_SIZEN;
            float[] v = reconstruct(spriteVertices);
            int verticesLength = vertices.length;
            spriteVertices = v;

            int remainingVertices = verticesLength;
            if (texture != lastTexture) {
                switchTexture(texture);
            } else {
                remainingVertices -= idx;
                if (remainingVertices == 0) {
                    flush();
                    remainingVertices = verticesLength;
                }
            }
            count = count/SPRITE_SIZEO*SPRITE_SIZEN;
            int copyCount = Math.min(remainingVertices, count);
//            SLog.dInfo("copyCount: " + copyCount + " " + v.length);
            System.arraycopy(spriteVertices, offset, vertices, idx, copyCount);
            idx += copyCount;
            if(idx%36!=0) {
                float[] ff = new float[idx];
                System.arraycopy(vertices, 0, ff, 0, idx);
                Log.info(Arrays.toString(ff));
                Log.info(Arrays.toString(Thread.currentThread().getStackTrace()));
            }

            count -= copyCount;
            while (count > 0) {
                offset += copyCount;
                flush();
                copyCount = Math.min(verticesLength, count);
                System.arraycopy(spriteVertices, offset, vertices, 0, copyCount);
                idx += copyCount;
                if(idx%36!=0) {
                    float[] ff = new float[idx];
                    System.arraycopy(vertices, 0, ff, 0, idx);
                    Log.info(Arrays.toString(ff));
                    Log.info(Arrays.toString(Thread.currentThread().getStackTrace()));
                }
                count -= copyCount;
            }
        }
    }


    public float[] reconstruct(float[] verticesl){
        int iter = verticesl.length/SPRITE_SIZEO;
        float[] tmpv = new float[SPRITE_SIZEN*iter];
        for (int i = 0; i < iter; i++) {
            int i2 = i*SPRITE_SIZEN;
            int i1 = i*SPRITE_SIZEO;


            tmpv[i2     ] = verticesl[i1];
            tmpv[i2 + 1 ] = verticesl[i1 + 1];
            tmpv[i2 + 2 ] = verticesl[i1 + 2];
            tmpv[i2 + 3 ] = verticesl[i1 + 3];
            tmpv[i2 + 4 ] = verticesl[i1 + 4];
            tmpv[i2 + 5 ] = verticesl[i1 + 5];
            tmpv[i2 + 6 ] = 0;
            tmpv[i2 + 7 ] = 0;
            tmpv[i2 + 8 ] = 0;

            tmpv[i2 + 9 ] = verticesl[i1 + 6];
            tmpv[i2 + 10] = verticesl[i1 + 7];
            tmpv[i2 + 11] = verticesl[i1 + 8];
            tmpv[i2 + 12] = verticesl[i1 + 9];
            tmpv[i2 + 13] = verticesl[i1 + 10];
            tmpv[i2 + 14] = verticesl[i1 + 11];
            tmpv[i2 + 15] = 0;
            tmpv[i2 + 16] = 0;
            tmpv[i2 + 17] = 0;

            tmpv[i2 + 18] = verticesl[i1 + 12];
            tmpv[i2 + 19] = verticesl[i1 + 13];
            tmpv[i2 + 20] = verticesl[i1 + 14];
            tmpv[i2 + 21] = verticesl[i1 + 15];
            tmpv[i2 + 22] = verticesl[i1 + 16];
            tmpv[i2 + 23] = verticesl[i1 + 17];
            tmpv[i2 + 24] = 0;
            tmpv[i2 + 25] = 0;
            tmpv[i2 + 26] = 0;

            tmpv[i2 + 27] = verticesl[i1 + 18];
            tmpv[i2 + 28] = verticesl[i1 + 19];
            tmpv[i2 + 29] = verticesl[i1 + 20];
            tmpv[i2 + 30] = verticesl[i1 + 21];
            tmpv[i2 + 31] = verticesl[i1 + 22];
            tmpv[i2 + 32] = verticesl[i1 + 23];
            tmpv[i2 + 33] = 0;
            tmpv[i2 + 34] = 0;
            tmpv[i2 + 35] = 0;
        }
        return tmpv;
    }

    @Override
    protected void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float rotation){
        if(sort && !flushing){
            if(numRequests >= requests.length) expandRequests();
            final DrawRequest req = requests[numRequests];
            req.x = x;
            req.y = y;
            requestZ[numRequests] = req.z = z;
            req.originX = originX;
            req.originY = originY;
            req.width = width;
            req.height = height;
            req.color = colorPacked;
            req.rotation = rotation;
            req.region.set(region);
            req.mixColor = mixColorPacked;
            req.blending = blending;
            req.texture = null;
            req.run = null;
            numRequests ++;
        }else{
            if (region.texture != lastTexture) {
                switchTexture(region.texture);
            } else if (idx == vertices.length) {
                flush();
            }

            float[] vertices = this.vertices;
            int idx = this.idx;
            this.idx += SPRITE_SIZEN;
            if(idx%36!=0) {
                float[] ff = new float[idx];
                System.arraycopy(vertices, 0, ff, 0, idx);
                Log.info(Arrays.toString(ff));
                Log.info(Arrays.toString(Thread.currentThread().getStackTrace()));
            }

            if (!Mathf.zero(rotation)) {
                //bottom left and top right corner points relative to origin
                float worldOriginX = x + originX;
                float worldOriginY = y + originY;
                float fx = -originX;
                float fy = -originY;
                float fx2 = width - originX;
                float fy2 = height - originY;

                // rotate
                float cos = Mathf.cosDeg(rotation);
                float sin = Mathf.sinDeg(rotation);

                float x1 = cos * fx - sin * fy + worldOriginX;
                float y1 = sin * fx + cos * fy + worldOriginY;
                float x2 = cos * fx - sin * fy2 + worldOriginX;
                float y2 = sin * fx + cos * fy2 + worldOriginY;
                float x3 = cos * fx2 - sin * fy2 + worldOriginX;
                float y3 = sin * fx2 + cos * fy2 + worldOriginY;
                float x4 = x1 + (x3 - x2);
                float y4 = y3 - (y2 - y1);

                float u = region.u;
                float v = region.v2;
                float u2 = region.u2;
                float v2 = region.v;

                float[] uvns = SVars.regionToUV.get(region); //uv u2v2
                float un = uvns != null ? uvns[0] : 0;
                float v2n = uvns != null ? uvns[1]: 0;
                float u2n = uvns != null ? uvns[2] : 0;
                float vn = uvns != null ? uvns[3] : 0;

                float color = this.colorPacked;
                float mixColor = this.mixColorPacked;

                vertices[idx] = x1;
                vertices[idx + 1] = y1;
                vertices[idx + 2] = color;
                vertices[idx + 3] = u;
                vertices[idx + 4] = v;
                vertices[idx + 5] = mixColor;
                vertices[idx + 6] = rotation;
                vertices[idx + 7] = un;
                vertices[idx + 8] = vn;

                vertices[idx + 9] = x2;
                vertices[idx + 10] = y2;
                vertices[idx + 11] = color;
                vertices[idx + 12] = u;
                vertices[idx + 13] = v2;
                vertices[idx + 14] = mixColor;
                vertices[idx + 15] = rotation;
                vertices[idx + 16] = un;
                vertices[idx + 17] = v2n;

                vertices[idx + 18] = x3;
                vertices[idx + 19] = y3;
                vertices[idx + 20] = color;
                vertices[idx + 21] = u2;
                vertices[idx + 22] = v2;
                vertices[idx + 23] = mixColor;
                vertices[idx + 24] = rotation;
                vertices[idx + 25] = u2n;
                vertices[idx + 26] = v2n;

                vertices[idx + 27] = x4;
                vertices[idx + 28] = y4;
                vertices[idx + 29] = color;
                vertices[idx + 30] = u2;
                vertices[idx + 31] = v;
                vertices[idx + 32] = mixColor;
                vertices[idx + 33] = rotation;
                vertices[idx + 34] = u2n;
                vertices[idx + 35] = vn;
//                SLog.tInfo(idx + " vs: " + Arrays.toString(), 100);
            } else {
                float fx2 = x + width;
                float fy2 = y + height;
                float u = region.u;
                float v = region.v2;
                float u2 = region.u2;
                float v2 = region.v;

                float[] uvns = SVars.regionToUV.get(region);
                float un = uvns != null ? uvns[0] : 0;
                float v2n = uvns != null ? uvns[1]: 0;
                float u2n = uvns != null ? uvns[2] : 0;
                float vn = uvns != null ? uvns[3] : 0;

                float color = this.colorPacked;
                float mixColor = this.mixColorPacked;

                vertices[idx] = x;
                vertices[idx + 1] = y;
                vertices[idx + 2] = color;
                vertices[idx + 3] = u;
                vertices[idx + 4] = v;
                vertices[idx + 5] = mixColor;
                vertices[idx + 6] = 0;
                vertices[idx + 7] = un;
                vertices[idx + 8] = vn;

                vertices[idx + 9] = x;
                vertices[idx + 10] = fy2;
                vertices[idx + 11] = color;
                vertices[idx + 12] = u;
                vertices[idx + 13] = v2;
                vertices[idx + 14] = mixColor;
                vertices[idx + 15] = 0;
                vertices[idx + 16] = un;
                vertices[idx + 17] = v2n;

                vertices[idx + 18] = fx2;
                vertices[idx + 19] = fy2;
                vertices[idx + 20] = color;
                vertices[idx + 21] = u2;
                vertices[idx + 22] = v2;
                vertices[idx + 23] = mixColor;
                vertices[idx + 24] = 0;
                vertices[idx + 25] = u2n;
                vertices[idx + 26] = v2n;

                vertices[idx + 27] = fx2;
                vertices[idx + 28] = y;
                vertices[idx + 29] = color;
                vertices[idx + 30] = u2;
                vertices[idx + 31] = v;
                vertices[idx + 32] = mixColor;
                vertices[idx + 33] = 0;
                vertices[idx + 34] = u2n;
                vertices[idx + 35] = vn;
            }
        }
    }

    @Override
    protected void draw(Runnable request){
        if(sort && !flushing){
            if(numRequests >= requests.length) expandRequests();
            final DrawRequest req = requests[numRequests];
            req.run = request;
            req.blending = blending;
            req.mixColor = mixColorPacked;
            req.color = colorPacked;
            requestZ[numRequests] = req.z = z;
            req.texture = null;
            numRequests ++;
        }else{
            super.draw(request);
        }
    }

    protected void expandRequests(){
        final DrawRequest[] requests = this.requests, newRequests = new DrawRequest[requests.length * 7 / 4];
        System.arraycopy(requests, 0, newRequests, 0, Math.min(newRequests.length, requests.length));
        for(int i = requests.length; i < newRequests.length; i++){
            newRequests[i] = new DrawRequest();
        }
        this.requests = newRequests;
        this.requestZ = Arrays.copyOf(requestZ, newRequests.length);
    }

    @Override
    protected void flush(){
        flushRequests();

        if (lastTexture instanceof NTexture n) {
            getShader().bind();
            setupMatrices();

            getShader().apply();

            Gl.depthMask(false);

            int spritesInBatch = idx / SPRITE_SIZEN;
            int count = spritesInBatch * 6;

            blending.apply();

            n.normal.bind(3);
            n.base.bind();

            Mesh mesh = this.mesh;
            mesh.setVertices(vertices, 0, idx);
            mesh.getIndicesBuffer().position(0);
            mesh.getIndicesBuffer().limit(count);
            mesh.render(getShader(), Gl.triangles, 0, count);

//            customShader = b;
        } else {
            getShader().bind();
            setupMatrices();

            if(customShader != null && apply){
                getShader().apply();
            }

            Gl.depthMask(false);

            int spritesInBatch = idx / SPRITE_SIZEN;
            int count = spritesInBatch * 6;

            blending.apply();

            lastTexture.bind();

//            deconstruct();

            Mesh mesh = this.mesh;
            mesh.setVertices(vertices, 0, idx);
            mesh.getIndicesBuffer().position(0);
            mesh.getIndicesBuffer().limit(count);
            mesh.render(getShader(), Gl.triangles, 0, count);
        }
        idx = 0;
    }

    protected void flushRequests(){
        if(!flushing && numRequests > 0){
            flushing = true;
            sortRequests();
            float preColor = colorPacked, preMixColor = mixColorPacked;
            Blending preBlending = blending;

            DrawRequest[] r = requests;
            int num = numRequests;
            for(int j = 0; j < num; j++){
                final DrawRequest req = r[j];

                colorPacked = req.color;
                mixColorPacked = req.mixColor;

                super.setBlending(req.blending);

                if(req.run != null){
                    req.run.run();
                }else if(req.texture != null){
                    draw(req.texture, req.vertices, 0, req.vertices.length);
                }else{
                    draw(req.region, req.x, req.y, req.originX, req.originY, req.width, req.height, req.rotation);
                }
            }

            colorPacked = preColor;
            mixColorPacked = preMixColor;
            color.abgr8888(colorPacked);
            mixColor.abgr8888(mixColorPacked);
            blending = preBlending;

            numRequests = 0;

            flushing = false;
        }
    }

    protected void sortRequests(){
        if(multithreaded){
            sortRequestsThreaded();
        }else{
            sortRequestsStandard();
        }
    }

    protected void sortRequestsThreaded(){
        final int numRequests = this.numRequests;
        if(copy.length < numRequests) copy = new DrawRequest[numRequests + (numRequests >> 3)];
        final DrawRequest[] items = requests, itemCopy = copy;
        final float[] itemZ = requestZ;
        final Future<?> initTask = commonPool.pool.submit(() -> System.arraycopy(items, 0, itemCopy, 0, numRequests));

        int[] contiguous = this.contiguous;
        int ci = 0, cl = contiguous.length;
        float z = itemZ[0];
        int startI = 0;
        // Point3: <z, index, length>
        for(int i = 1; i < numRequests; i++){
            if(itemZ[i] != z){ // if contiguous section should end
                contiguous[ci] = Float.floatToRawIntBits(z + 16f);
                contiguous[ci + 1] = startI;
                contiguous[ci + 2] = i - startI;
                ci += 3;
                if(ci + 3 > cl){
                    contiguous = Arrays.copyOf(contiguous, cl <<= 1);
                }
                z = itemZ[startI = i];
            }
        }
        contiguous[ci] = Float.floatToRawIntBits(z + 16f);
        contiguous[ci + 1] = startI;
        contiguous[ci + 2] = numRequests - startI;
        this.contiguous = contiguous;

        final int L = (ci / 3) + 1;

        if(contiguousCopy.length < contiguous.length) this.contiguousCopy = new int[contiguous.length];

        final int[] sorted = CountingSort.countingSortMapMT(contiguous, contiguousCopy, L);

        if(locs.length < L + 1) locs = new int[L + L / 10];
        final int[] locs = this.locs;
        for(int i = 0; i < L; i++){
            locs[i + 1] = locs[i] + sorted[i * 3 + 2];
        }
        try{
            initTask.get();
        }catch(Exception ignored){
            System.arraycopy(items, 0, itemCopy, 0, numRequests);
        }
        PopulateTask.tasks = sorted;
        PopulateTask.src = itemCopy;
        PopulateTask.dest = items;
        PopulateTask.locs = locs;
        commonPool.pool.invoke(new PopulateTask(0, L));
    }

    protected void sortRequestsStandard(){ // Non-threaded implementation for weak devices
        final int numRequests = this.numRequests;
        if(copy.length < numRequests) copy = new DrawRequest[numRequests + (numRequests >> 3)];
        final DrawRequest[] items = copy;
        final float[] itemZ = requestZ;
        System.arraycopy(requests, 0, items, 0, numRequests);
        int[] contiguous = this.contiguous;
        int ci = 0, cl = contiguous.length;
        float z = itemZ[0];
        int startI = 0;
        // Point3: <z, index, length>
        for(int i = 1; i < numRequests; i++){
            if(itemZ[i] != z){ // if contiguous section should end
                contiguous[ci] = Float.floatToRawIntBits(z + 16f);
                contiguous[ci + 1] = startI;
                contiguous[ci + 2] = i - startI;
                ci += 3;
                if(ci + 3 > cl){
                    contiguous = Arrays.copyOf(contiguous, cl <<= 1);
                }
                z = itemZ[startI = i];
            }
        }
        contiguous[ci] = Float.floatToRawIntBits(z + 16f);
        contiguous[ci + 1] = startI;
        contiguous[ci + 2] = numRequests - startI;
        this.contiguous = contiguous;

        final int L = (ci / 3) + 1;

        if(contiguousCopy.length < contiguous.length) contiguousCopy = new int[contiguous.length];

        final int[] sorted = CountingSort.countingSortMap(contiguous, contiguousCopy, L);

        int ptr = 0;
        final DrawRequest[] dest = requests;
        for(int i = 0; i < L * 3; i += 3){
            final int pos = sorted[i + 1], length = sorted[i + 2];
            if(length < 10){
                final int end = pos + length;
                for(int sj = pos, dj = ptr; sj < end; sj++, dj++){
                    dest[dj] = items[sj];
                }
            }else System.arraycopy(items, pos, dest, ptr, Math.min(length, dest.length - ptr));
            ptr += length;
        }
    }

    static class CountingSort{
        private static final int processors = Runtime.getRuntime().availableProcessors() * 8;

        static int[] locs = new int[100];
        static final int[][] locses = new int[processors][100];

        static final IntIntMap[] countses = new IntIntMap[processors];

        private static Point2[] entries = new Point2[100];

        private static int[] entries3 = new int[300], entries3a = new int[300];
        private static Integer[] entriesBacking = new Integer[100];

        private static final CountingSortTask[] tasks = new CountingSortTask[processors];
        private static final CountingSortTask2[] task2s = new CountingSortTask2[processors];
        private static final Future<?>[] futures = new Future<?>[processors];

        static{
            for(int i = 0; i < countses.length; i++) countses[i] = new IntIntMap();
            for(int i = 0; i < entries.length; i++) entries[i] = new Point2();

            for(int i = 0; i < processors; i++){
                tasks[i] = new CountingSortTask();
                task2s[i] = new CountingSortTask2();
            }
        }

        static class CountingSortTask implements Runnable{
            static int[] arr;
            int start, end, id;

            public void set(int start, int end, int id){
                this.start = start;
                this.end = end;
                this.id = id;
            }

            @Override
            public void run(){
                final int id = this.id, start = this.start, end = this.end;
                int[] locs = locses[id];
                final int[] arr = CountingSortTask.arr;
                final IntIntMap counts = countses[id];
                counts.clear();
                int unique = 0;
                for(int i = start; i < end; i++){
                    int loc = counts.getOrPut(arr[i * 3], unique);
                    arr[i * 3] = loc;
                    if(loc == unique){
                        if(unique >= locs.length){
                            locs = Arrays.copyOf(locs, unique * 3 / 2);
                        }
                        locs[unique++] = 1;
                    }else{
                        locs[loc]++;
                    }
                }
                locses[id] = locs;
            }
        }

        static class CountingSortTask2 implements Runnable{
            static int[] src, dest;
            int start, end, id;

            public void set(int start, int end, int id){
                this.start = start;
                this.end = end;
                this.id = id;
            }

            @Override
            public void run(){
                final int start = this.start, end = this.end;
                final int[] locs = locses[id];
                final int[] src = CountingSortTask2.src, dest = CountingSortTask2.dest;
                for(int i = end - 1, i3 = i * 3; i >= start; i--, i3 -= 3){
                    final int destPos = --locs[src[i3]] * 3;
                    dest[destPos] = src[i3];
                    dest[destPos + 1] = src[i3 + 1];
                    dest[destPos + 2] = src[i3 + 2];
                }
            }
        }

        static int[] countingSortMapMT(final int[] arr, final int[] swap, final int end){
            final IntIntMap[] countses = CountingSort.countses;
            final int[][] locs = CountingSort.locses;
            final int threads = Math.min(processors, (end + 4095) / 4096); // 4096 Point3s to process per thread
            final int thread_size = end / threads + 1;
            final CountingSortTask[] tasks = CountingSort.tasks;
            final CountingSortTask2[] task2s = CountingSort.task2s;
            final Future<?>[] futures = CountingSort.futures;
            CountingSortTask.arr = CountingSortTask2.src = arr;
            CountingSortTask2.dest = swap;

            for(int s = 0, thread = 0; thread < threads; thread++, s += thread_size){
                CountingSortTask task = tasks[thread];
                final int stop = Math.min(s + thread_size, end);
                task.set(s, stop, thread);
                task2s[thread].set(s, stop, thread);
                futures[thread] = commonPool.pool.submit(task);
            }

            int unique = 0;
            for(int i = 0; i < threads; i++){
                try{
                    futures[i].get();
                }catch(ExecutionException | InterruptedException e){
                    commonPool.pool.execute(tasks[i]);
                }
                unique += countses[i].size;
            }

            final int L = unique;
            if(entriesBacking.length < L){
                entriesBacking = new Integer[L * 3 / 2];
                entries3 = new int[L * 3 * 3 / 2];
                entries3a = new int[L * 3 * 3 / 2];
            }
            final int[] entries = CountingSort.entries3, entries3a = CountingSort.entries3a;
            final Integer[] entriesBacking = CountingSort.entriesBacking;
            int j = 0;
            for(int i = 0; i < threads; i++){
                if(countses[i].size == 0) continue;
                final IntIntMap.Entries countEntries = countses[i].entries();
                final IntIntMap.Entry entry = countEntries.next();
                entries[j] = entry.key;
                entries[j + 1] = entry.value;
                entries[j + 2] = i;
                j += 3;
                while(countEntries.hasNext){
                    countEntries.next();
                    entries[j] = entry.key;
                    entries[j + 1] = entry.value;
                    entries[j + 2] = i;
                    j += 3;
                }
            }

            for(int i = 0; i < L; i++){
                entriesBacking[i] = i;
            }
            Arrays.sort(entriesBacking, 0, L, Structs.comparingInt(i -> entries[i * 3]));
            for(int i = 0; i < L; i++){
                int from = entriesBacking[i] * 3, to = i * 3;
                entries3a[to] = entries[from];
                entries3a[to + 1] = entries[from + 1];
                entries3a[to + 2] = entries[from + 2];
            }

            for(int i = 0, pos = 0; i < L * 3; i += 3){
                pos = (locs[entries3a[i + 2]][entries3a[i + 1]] += pos);
            }

            for(int thread = 0; thread < threads; thread++){
                futures[thread] = commonPool.pool.submit(task2s[thread]);
            }
            for(int i = 0; i < threads; i++){
                try{
                    futures[i].get();
                }catch(ExecutionException | InterruptedException e){
                    commonPool.pool.execute(task2s[i]);
                }
            }
            return swap;
        }

        static int[] countingSortMap(final int[] arr, final int[] swap, final int end){
            int[] locs = CountingSort.locs;
            final IntIntMap counts = CountingSort.countses[0];
            counts.clear();

            int unique = 0;
            final int end3 = end * 3;
            for(int i = 0; i < end3; i += 3){
                int loc = counts.getOrPut(arr[i], unique);
                arr[i] = loc;
                if(loc == unique){
                    if(unique >= locs.length){
                        locs = Arrays.copyOf(locs, unique * 3 / 2);
                    }
                    locs[unique++] = 1;
                }else{
                    locs[loc]++;
                }
            }
            CountingSort.locs = locs;

            if(entries.length < unique){
                final int prevLength = entries.length;
                entries = Arrays.copyOf(entries, unique * 3 / 2);
                final Point2[] entries = CountingSort.entries;
                for(int i = prevLength; i < entries.length; i++) entries[i] = new Point2();
            }
            final Point2[] entries = CountingSort.entries;

            final IntIntMap.Entries countEntries = counts.entries();
            final IntIntMap.Entry entry = countEntries.next();
            entries[0].set(entry.key, entry.value);
            int j = 1;
            while(countEntries.hasNext){
                countEntries.next(); // it returns the same entry over and over again.
                entries[j++].set(entry.key, entry.value);
            }
            Arrays.sort(entries, 0, unique, Structs.comparingInt(p -> p.x));

            int prev = entries[0].y, next;
            for(int i = 1; i < unique; i++){
                locs[next = entries[i].y] += locs[prev];
                prev = next;
            }
            for(int i = end - 1, i3 = i * 3; i >= 0; i--, i3 -= 3){
                final int destPos = --locs[arr[i3]] * 3;
                swap[destPos] = arr[i3];
                swap[destPos + 1] = arr[i3 + 1];
                swap[destPos + 2] = arr[i3 + 2];
            }
            return swap;
        }
    }

    static class PopulateTask extends RecursiveAction{
        int from, to;
        static int[] tasks;
        static DrawRequest[] src;
        static DrawRequest[] dest;
        static int[] locs;

        //private static final int threshold = 256;
        PopulateTask(int from, int to){
            this.from = from;
            this.to = to;
        }

        @Override
        protected void compute(){
            final int[] locs = PopulateTask.locs;
            if(to - from > 1 && locs[to] - locs[from] > 2048){
                final int half = (locs[to] + locs[from]) >> 1;
                int mid = Arrays.binarySearch(locs, from, to, half);
                if(mid < 0) mid = -mid - 1;
                if(mid != from && mid != to){
                    invokeAll(new PopulateTask(from, mid), new PopulateTask(mid, to));
                    return;
                }
            }
            final DrawRequest[] src = PopulateTask.src, dest = PopulateTask.dest;
            final int[] tasks = PopulateTask.tasks;
            for(int i = from; i < to; i++){
                final int point = i * 3, pos = tasks[point + 1], length = tasks[point + 2];
                if(length < 10){
                    final int end = pos + length;
                    for(int sj = pos, dj = locs[i]; sj < end; sj++, dj++){
                        dest[dj] = src[sj];
                    }
                }else{
                    System.arraycopy(src, pos, dest, locs[i], Math.min(length, dest.length - locs[i]));
                }
            }
        }
    }
}