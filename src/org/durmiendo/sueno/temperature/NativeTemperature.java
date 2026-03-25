package org.durmiendo.sueno.temperature;

import arc.util.OS;
import arc.util.io.Streams;
import org.durmiendo.sueno.utils.SLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class NativeTemperature {
    public static boolean isLoaded = false;
    
    public static FloatBuffer dataBuffer;
    private static int width, height;
    
    public static void load() {
        if (isLoaded) return;
        
        try {
            String libName;
            if (OS.isWindows) libName = "sueno-native.dll";
            else if (OS.isLinux) libName = "libsueno-native.so";
            else if (OS.isMac) libName = "libsueno-native.dylib";
            else {
                SLog.err("Unsupported OS for native optimization.");
                return;
            }
            
            String pathInJar = "/natives/" + libName;
            InputStream in = NativeTemperature.class.getResourceAsStream(pathInJar);
            
            if (in == null) {
                SLog.err("Native library file not found inside JAR at: " + pathInJar);
                return;
            }
            
            File tempFile = File.createTempFile("sueno-native-", OS.isWindows ? ".dll" : ".so");
            tempFile.deleteOnExit();
            
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                Streams.copy(in, out);
            }
            
            System.load(tempFile.getAbsolutePath());
            isLoaded = true;
            SLog.info("Native C++ optimization LOADED.");
            
        } catch (Throwable e) {
            SLog.err("Failed to load native library! Fallback to Java.", e);
            isLoaded = false;
        }
    }
    
    public static void init(int w, int h) {
        if (!isLoaded) return;
        width = w;
        height = h;
        
        ByteBuffer bb = ByteBuffer.allocateDirect(w * h * 4);
        bb.order(ByteOrder.nativeOrder());
        dataBuffer = bb.asFloatBuffer();
        
        for (int i = 0; i < w * h; i++) {
            dataBuffer.put(i, -1f);
        }
        
        initNative(w, h, dataBuffer);
    }
    
    private static native void initNative(int width, int height, FloatBuffer buffer);
    
    public static native void update(float transferCoeff, float ambientChange);
    
    
    public static float getTemperature(int x, int y) {
        // if (x < 0 || x >= width || y < 0 || y >= height) return -1f;
        try {
            return dataBuffer.get(y * width + x);
        } catch (IndexOutOfBoundsException e) {
            return -1f;
        }
    }
    
    public static void setTemperature(int x, int y, float value) {
        // if (x < 0 || x >= width || y < 0 || y >= height) return;
        try {
            dataBuffer.put(y * width + x, value);
        } catch (IndexOutOfBoundsException e) {
            // ignore
        }
    }
}