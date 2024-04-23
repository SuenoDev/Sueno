package org.durmiendo.sueno.files;

import arc.files.*;
import arc.util.OS;

/**
 * Use for JAR internal navigation
 * @author nekit508
 **/
public class InternalFileTree {
    public Class<?> anchorClass;

    public ZipFi root;

    /**
     * @param owner navigation anchor
     **/
    public InternalFileTree(Class<?> owner) {
        anchorClass = owner;

        String classPath = owner.getResource("").getFile().replaceAll("%20", " ");
        classPath = classPath.substring(classPath.indexOf(":")+2);
        String jarPath = classPath.substring(0, classPath.indexOf("!"));
        if (OS.isLinux) jarPath = "/" + jarPath;

        root = new ZipFi(new Fi(jarPath));
    }

    public Fi child(String childPath) {
        Fi out = root;
        for (String s : childPath.split("/")) {
            if (!"".equals(s))
                out = out.child(s);
        }
        return out;
    }
}
