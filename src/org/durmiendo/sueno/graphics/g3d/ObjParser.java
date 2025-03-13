package org.durmiendo.sueno.graphics.g3d;

import arc.graphics.Color;
import arc.graphics.Mesh;
import arc.graphics.Texture;
import arc.graphics.VertexAttribute;
import arc.math.geom.Vec3;
import arc.struct.*;
import arc.util.Log;
import org.durmiendo.sueno.core.SVars;
import org.durmiendo.sueno.utils.SLog;

import java.io.BufferedReader;
import java.io.StringReader;

public class ObjParser {
    public static ObjectMap<String, Obj> loaded = new ObjectMap<>();
    public static float[] vertices;
    public static short[] indices;
    public static Obj.Mtl cur = null;
    public static Seq<String> on = new Seq<>();

    public static int v;
    public static int i;

    public static Obj load(String s, float scl) {
        if (loaded.containsKey(s)) return loaded.get(s);

        Obj o = load(s);
        o.scl = scl;
        loaded.put(s, o);

        return o;
    }

    public static Obj load(String s) {
        if (loaded.containsKey(s)) return loaded.get(s);

        Obj o =  loadObj(
                SVars.internalFileTree.child("models/" + s +".obj").readString(),
                SVars.internalFileTree.child("models/" + s +".mtl").readString()
        );
        loaded.put(s, o);

        return o;
    }

    private static Obj loadObj(String obj, String mtl) {
        Obj result = new Obj();

        v = 0;
        i = 0;

        ObjectMap<String, Obj.Mtl> mtls = new ObjectMap<>();

        FloatSeq positions = new FloatSeq();
        FloatSeq normals = new FloatSeq();
        FloatSeq texCoords = new FloatSeq();

        ObjectMap<String, Integer> vertexIndexMap = new ObjectMap<>(); // Ключ - строка "v/vt/vn", значение - индекс вершины в vertices
        FloatSeq vertexList = new FloatSeq(); // Содержит уникальные данные вершин в формате x, y, z, nx, ny, nz, u, v
        ShortSeq indexList = new ShortSeq(); // Содержит индексы вершин для отрисовки

        try (BufferedReader reader = new BufferedReader(new StringReader(mtl))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "newmtl":
                        cur = new Obj.Mtl();
                        cur.name = parts[1];
                        mtls.put(parts[1], cur);
                        break;
                    case "Ka":
                        cur.ambient = new Color(Float.parseFloat(parts[1]), Float.parseFloat(parts[1]), Float.parseFloat(parts[1]));
                        break;
                    case "Kd":
                        cur.diffuse = new Color(Float.parseFloat(parts[1]), Float.parseFloat(parts[1]), Float.parseFloat(parts[1]));
                        break;
                    case "Ks":
                        cur.specular = new Color(Float.parseFloat(parts[1]), Float.parseFloat(parts[1]), Float.parseFloat(parts[1]));
                        break;
                    case "Ns":
                        cur.shininess = Float.parseFloat(parts[1]);
                        break;
                    case "illum":
                        cur.illum = Integer.parseInt(parts[1]);
                        break;
                    case "map_Kd":
                        Texture t = new Texture(SVars.internalFileTree.child( "models/" + parts[1]));
                        t.setFilter(Texture.TextureFilter.linear);
                        cur.texture = t;
                        break;
                }
            }
        } catch (Exception e) {
            Log.err(e);
            return Obj.zero;
        }


        try (BufferedReader reader = new BufferedReader(new StringReader(obj))) {
            String line;
            Vec3 tmp = new Vec3();
            Runnable runnable = () -> {};
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                switch (parts[0]) {
                    case "v":
                        positions.add(Float.parseFloat(parts[1]));
                        positions.add(Float.parseFloat(parts[2]));
                        positions.add(Float.parseFloat(parts[3]));
                        break;
                    case "vn":
                        tmp.x = Float.parseFloat(parts[1]);
                        tmp.y = Float.parseFloat(parts[2]);
                        tmp.z = Float.parseFloat(parts[3]);
                        tmp.nor();
                        normals.add(tmp.x);
                        normals.add(tmp.y);
                        normals.add(tmp.z);
                        break;
                    case "vt":
                        texCoords.add(Float.parseFloat(parts[1]));
                        texCoords.add(1f-Float.parseFloat(parts[2]));
                        break;
                    case "f":
                        parseFace(parts, positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
                        break;
                    case "o":
                        on.add(parts[1]);
                        break;
                    case "usemtl":
                        runnable.run();
                        runnable = () -> {
                            vertices = new float[vertexList.size];
                            indices = new short[indexList.size];

                            v+=vertices.length;
                            i+=indices.length;

                            System.arraycopy(vertexList.items, 0, vertices, 0, vertexList.size);
                            System.arraycopy(indexList.items, 0, indices, 0, indexList.size);


                            Mesh mesh = new Mesh(false, true, vertices.length, indices.length,
                                    VertexAttribute.position3,
                                    VertexAttribute.normal,
                                    VertexAttribute.texCoords);;

                            mesh.setVertices(vertices);
                            mesh.setIndices(indices);

                            mesh.getIndicesBuffer().position(0);
                            mesh.getIndicesBuffer().limit(indices.length);

                            vertexList.clear();
                            indexList.clear();

                            Obj.Material mtlo = new Obj.Material();

                            mtlo.mesh = mesh;
                            mtlo.mtl = mtls.get(parts[1]);
                            mtlo.on = on.get(on.size-2);
                            result.materials.add(mtlo);
                        };
                        break;
                }
            }
            runnable.run();
            SLog.load("loaded " + result.materials.size + " parts of 3d model, " + v + " vertices, " + i + " indices");
            return result;
        } catch (Exception e) {
            Log.err(e);
            return Obj.zero;
        }
    }


    private static void parseFace(String[] parts, FloatSeq positions, FloatSeq normals, FloatSeq texCoords,
                                  FloatSeq vertexList, ShortSeq indexList, ObjectMap<String, Integer> vertexIndexMap) {
        if (parts.length == 4) { // Треугольник
            processFaceVertex(parts[1], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[2], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[3], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
        } else if (parts.length == 5) { // Четырехугольник
            processFaceVertex(parts[1], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[2], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[3], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);

            processFaceVertex(parts[1], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[3], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
            processFaceVertex(parts[4], positions, normals, texCoords, vertexList, indexList, vertexIndexMap);
        } else {
            System.err.println("Ошибка: Поддерживаются только треугольники и четырехугольники.");
        }
    }

    private static void processFaceVertex(String vertexStr, FloatSeq positions, FloatSeq normals, FloatSeq texCoords,
                                          FloatSeq vertexList, ShortSeq indexList, ObjectMap<String, Integer> vertexIndexMap) {
        String[] indices = vertexStr.split("/");
        int positionIndex = Integer.parseInt(indices[0]) - 1;
        int texCoordIndex = (indices.length > 1 && !indices[1].isEmpty()) ? Integer.parseInt(indices[1]) - 1 : -1;
        int normalIndex = (indices.length > 2 && !indices[2].isEmpty()) ? Integer.parseInt(indices[2]) - 1 : -1;

        // Создаем ключ для поиска вершины в Map
        String vertexKey = positionIndex + "/" + texCoordIndex + "/" + normalIndex;

        if (vertexIndexMap.containsKey(vertexKey)) {
            indexList.add(vertexIndexMap.get(vertexKey));
        } else {
            float x = positions.get(positionIndex * 3);
            float y = positions.get(positionIndex * 3 + 1);
            float z = positions.get(positionIndex * 3 + 2);

            float nx = (normalIndex != -1) ? normals.get(normalIndex * 3) : 0;
            float ny = (normalIndex != -1) ? normals.get(normalIndex * 3 + 1) : 0;
            float nz = (normalIndex != -1) ? normals.get(normalIndex * 3 + 2) : 0;

            float u = (texCoordIndex != -1) ? texCoords.get(texCoordIndex * 2) : 0;
            float v = (texCoordIndex != -1) ? texCoords.get(texCoordIndex * 2 + 1) : 0;

            // Добавляем данные вершины в список
            int newIndex = vertexList.size / 8; // Индекс новой вершины
            vertexList.add(x);
            vertexList.add(y);
            vertexList.add(z);
            vertexList.add(nx);
            vertexList.add(ny);
            vertexList.add(nz);
            vertexList.add(u);
            vertexList.add(v);


            // Добавляем вершину в Map
            vertexIndexMap.put(vertexKey, newIndex);
            indexList.add(newIndex);
        }
    }
}