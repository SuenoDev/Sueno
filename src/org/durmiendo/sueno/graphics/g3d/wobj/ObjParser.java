package org.durmiendo.sueno.graphics.g3d.wobj;

import arc.graphics.Color;
import arc.graphics.Mesh;
import arc.graphics.Texture;
import arc.graphics.VertexAttribute;
import arc.math.geom.Vec3;
import arc.struct.FloatSeq;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.struct.ShortSeq;
import arc.util.Log;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.utils.SLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class ObjParser {
    public static ObjectMap<String, Obj> loaded = new ObjectMap<>();
    private static float[] vertices;
    private static short[] indices;
    private static Obj.Mtl cur = null;
    private static Seq<String> on = new Seq<>();
    
    private static int v;
    private static int i;
    
    public static Obj loadObj(String s, Vec3 scl) {
        if (loaded.containsKey(s)) return loaded.get(s);
        
        Obj o = loadObj(s);
        o.scl = scl;
        loaded.put(s, o);
        
        return o;
    }
    
    public static Obj loadObj(String s) {
        if (loaded.containsKey(s)) return loaded.get(s);
        try {
            Obj o = loadObj(
                    SVars.internalFileTree.child("models/" + s + ".obj").readString(),
                    SVars.internalFileTree.child("models/" + s + ".mtl").readString()
            );
            loaded.put(s, o);
            return o;
        } catch (Exception e) {
            Log.err("Failed to load model: " + s, e);
            return Obj.zero;
        }
    }
    
    private static ObjectMap<String, Obj.Mtl> loadMTL(String mtl) throws IOException {
        ObjectMap<String, Obj.Mtl> mtls = new ObjectMap<>();
        
        try (BufferedReader reader = new BufferedReader(new StringReader(mtl))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0 || parts[0].startsWith("#")) continue;
                
                try {
                    switch (parts[0]) {
                        case "newmtl":
                            cur = new Obj.Mtl();
                            cur.name = parts[1];
                            mtls.put(parts[1], cur);
                            break;
                        case "Ka":
                            cur.ambient = new Color(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), 1f);
                            break;
                        case "Kd":
                            cur.diffuse = new Color(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), 1f);
                            break;
                        case "Ks":
                            cur.specular = new Color(Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3]), 1f);
                            break;
                        case "Ns":
                            cur.shininess = Float.parseFloat(parts[1]);
                            break;
                        case "illum":
                            cur.illum = Integer.parseInt(parts[1]);
                            break;
                        case "map_Kd":
                            Texture t = new Texture(SVars.internalFileTree.child("models/" + parts[1]));
                            t.setFilter(Texture.TextureFilter.linear, Texture.TextureFilter.linear);
                            t.setWrap(Texture.TextureWrap.repeat, Texture.TextureWrap.repeat);
                            cur.texture = t;
                            break;
                    }
                } catch (NumberFormatException e) {
                    Log.warn("Could not parse number in MTL file: " + line);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.warn("Invalid number of arguments in MTL file: " + line);
                }
            }
        }
        return mtls;
    }
    
    static String line;
    static String currentMtl = "";
    static String currentObject = "default";
    
    private static Obj loadObj(String obj, String mtl) throws IOException {
        Obj result = new Obj();
        v = 0;
        i = 0;
        on.clear();
        
        ObjectMap<String, Obj.Mtl> mtls = loadMTL(mtl);
        
        FloatSeq positions = new FloatSeq();
        FloatSeq normals = new FloatSeq();
        FloatSeq texCoords = new FloatSeq();
        ObjectMap<String, Short> vertexIndexMap = new ObjectMap<>();
        FloatSeq vertexList = new FloatSeq();
        ShortSeq indexList = new ShortSeq();
        
        try (BufferedReader reader = new BufferedReader(new StringReader(obj))) {
            
            
            Runnable flushMesh = () -> {
                if (vertexList.size == 0) return;
                
                vertices = vertexList.toArray();
                indices = indexList.toArray();
                
                v += vertexList.size / 8;
                i += indices.length;
                
                Mesh mesh = new Mesh(true, vertices.length, indices.length,
                        VertexAttribute.position3,
                        VertexAttribute.normal,
                        VertexAttribute.texCoords);
                
                mesh.setVertices(vertices);
                mesh.setIndices(indices);
                
                Obj.Material mat = new Obj.Material();
                mat.mesh = mesh;
                mat.mtl = mtls.get(currentMtl, new Obj.Mtl());
                mat.on = currentObject;
                result.materials.add(mat);
                
                vertexList.clear();
                indexList.clear();
                vertexIndexMap.clear();
            };
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 0 || parts[0].startsWith("#")) continue;
                
                try {
                    switch (parts[0]) {
                        case "v":
                            positions.add(Float.parseFloat(parts[1]));
                            positions.add(Float.parseFloat(parts[2]));
                            positions.add(Float.parseFloat(parts[3]));
                            break;
                        case "vn":
                            normals.add(Float.parseFloat(parts[1]));
                            normals.add(Float.parseFloat(parts[2]));
                            normals.add(Float.parseFloat(parts[3]));
                            break;
                        case "vt":
                            texCoords.add(Float.parseFloat(parts[1]));
                            texCoords.add(1f - Float.parseFloat(parts[2])); // OBJ Y is often inverted
                            break;
                        case "f":
                            parseFace(parts, positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
                            break;
                        case "o":
                            flushMesh.run();
                            currentObject = parts.length > 1 ? parts[1] : "default";
                            break;
                        case "usemtl":
                            flushMesh.run();
                            currentMtl = parts.length > 1 ? parts[1] : "";
                            break;
                    }
                } catch (NumberFormatException e) {
                    Log.warn("Could not parse number in OBJ file: " + line);
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.warn("Invalid number of arguments in OBJ file: " + line);
                }
            }
            flushMesh.run();
            SLog.load("loaded " + result.materials.size + " parts of 3d model, " + v + " vertices, " + i + " indices");
            return result;
        }
    }
    
    private static void parseFace(String[] parts, FloatSeq positions, FloatSeq normals, FloatSeq texCoords,
                                  FloatSeq vertexList, ShortSeq indexList, ObjectMap<String, Short> vertexIndexMap) {
        for (int i = 2; i < parts.length - 1; i++) {
            processFaceVertex(parts[1], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[i], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[i + 1], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
        }
    }
    
    private static void processFaceVertex(String vertexStr, FloatSeq positions, FloatSeq normals, FloatSeq texCoords,
                                          FloatSeq vertexList, ShortSeq indexList, ObjectMap<String, Short> vertexIndexMap) {
        if (vertexIndexMap.containsKey(vertexStr)) {
            indexList.add(vertexIndexMap.get(vertexStr));
            return;
        }
        
        String[] indicesStr = vertexStr.split("/");
        int posIndex = Integer.parseInt(indicesStr[0]) - 1;
        int texIndex = indicesStr.length > 1 && !indicesStr[1].isEmpty() ? Integer.parseInt(indicesStr[1]) - 1 : -1;
        int normIndex = indicesStr.length > 2 && !indicesStr[2].isEmpty() ? Integer.parseInt(indicesStr[2]) - 1 : -1;
        
        vertexList.add(positions.get(posIndex * 3));
        vertexList.add(positions.get(posIndex * 3 + 1));
        vertexList.add(positions.get(posIndex * 3 + 2));
        
        if (normIndex != -1) {
            vertexList.add(normals.get(normIndex * 3));
            vertexList.add(normals.get(normIndex * 3 + 1));
            vertexList.add(normals.get(normIndex * 3 + 2));
        } else {
            vertexList.add(0, 0, 0);
        }
        
        if (texIndex != -1) {
            vertexList.add(texCoords.get(texIndex * 2));
            vertexList.add(texCoords.get(texIndex * 2 + 1));
        } else {
            vertexList.add(0, 0);
        }
        
        short newIndex = (short) (vertexList.size / 8 - 1);
        indexList.add(newIndex);
        vertexIndexMap.put(vertexStr, newIndex);
    }
}