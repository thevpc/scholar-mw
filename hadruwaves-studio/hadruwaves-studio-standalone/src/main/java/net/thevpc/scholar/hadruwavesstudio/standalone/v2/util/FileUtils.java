/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author vpc
 */
public class FileUtils {

    private static String _indexedName(String name, int index) {
        if (index == 0) {
            return name;
        }
        int i = name.indexOf('.');
        if (i < 0) {
            return name + "-" + index;
        }
        return name.substring(0, i) + "-" + i + name.substring(i);
    }

    public static String generateFileName(String path, String referencePath) {
        String prefix = new File(referencePath).getPath();
        File f = new File(referencePath, path);
        if (f.exists() && f.isFile()) {
            File p = f.getParentFile();
            int i = 2;
            while (true) {
                String n2 = _indexedName(f.getName(), i);
                File f2 = new File(p, n2);
                if (!f2.exists()) {
                    return f2.getPath().substring(prefix.length());
                }
                i++;
            }
        } else {
            return f.getPath().substring(prefix.length());
        }
    }
}
