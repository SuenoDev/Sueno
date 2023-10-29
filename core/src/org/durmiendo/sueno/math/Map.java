package org.durmiendo.sueno.math;

import arc.func.Cons;
import arc.func.Intc2;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.util.Nullable;

import java.util.Arrays;
import java.util.Iterator;

public class Map implements Iterable<Float> {
    public int width, height;
    public int size = width * height;

    final float[] array;

    public Map(int width, int height) {
        this.array = new float[width * height];
        this.width = width;
        this.height = height;

    }
    public void each(Intc2 cons) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cons.get(x, y);
            }
        }
    }

        /** fills this tile set with empty air tiles. */
        public void fill(float f) {
            Arrays.fill(array, f);
        }

        /**
         * set a tile at a position; does not range-check. use with caution.
         */
        public void set(int x, int y, float v) {
            array[y * width + x] = v;
        }

        /**
         * set a tile at a raw array position; used for fast iteration / 1-D for-loops
         */
        public void seti(int i, float v) {
            array[i] = v;
        }
        public void addi(int i, float v) {
            array[i] += v;
        }
    public void add(int x, int y, float v) {
        array[y * width + x] += v;
    }

        /**
         * @return whether these coordinates are in bounds
         */
        public boolean in(int x, int y) {
            return x >= 0 && x < width && y >= 0 && y < height;
        }

        /**
         * @return a tile at coordinates, or null if out of bounds
         */
        @Nullable
        public float get(int x, int y) {
            if (x < 0 || x >= width || y < 0 || y >= height) return 0;
            else return array[y * width + x];
        }

        /**
         * @return a tile at coordinates; throws an exception if out of bounds
         */
        public float getn(int x, int y) {
            if (x < 0 || x >= width || y < 0 || y >= height)
                throw new IllegalArgumentException(x + ", " + y + " out of bounds: width=" + width + ", height=" + height);
            return array[y * width + x];
        }

        /**
         * @return a tile at coordinates, clamped.
         */
        public float getc(int x, int y) {
            x = Mathf.clamp(x, 0, width - 1);
            y = Mathf.clamp(y, 0, height - 1);
            return array[y * width + x];
        }

        /**
         * @return a tile at an iteration index [0, width * height]
         */
        public float geti(int idx) {
            return array[idx];
        }

        /**
         * @return a tile at an int position (not equivalent to geti)
         */
        public @Nullable float getp(int pos) {
            return get(Point2.x(pos), Point2.y(pos));
        }

        public void eachTile(Cons<Float> cons) {
            for (float tile : array) {
                cons.get(tile);
            }
        }

        @Override
        public Iterator iterator() {
            //iterating through the entire map is expensive anyway, so a new allocation doesn't make much of a difference
            return new IteratorFloats();
        }

        private class IteratorFloats implements Iterator<Float> {
            int index = 0;

            IteratorFloats() {
            }

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public Float next() {
                return array[index++];
            }
        }
    }