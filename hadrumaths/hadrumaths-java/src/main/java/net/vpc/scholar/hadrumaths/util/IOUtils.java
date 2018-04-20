package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.RuntimeIOException;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Classe utilitaire pour la manipulation des E/S
 *
 * @author tbensalah (Taha Ben Salah)
 * @creation_date date 27/01/2004
 * @last_modification_date date 28/01/2004
 */
public final class IOUtils {
    /**
     * taille par defaut du buffer de transfert
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    public static final String WRITE_TEMP_EXT = ".temp";
    private static StringSerializer DEFAULT_STRING_SERIALIZER = new DefaultStringSerializer();

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, File out, int bufferSize) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(in);
            fos = new FileOutputStream(out);
            copy(fis, fos, bufferSize);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }


    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(File in, OutputStream out, int bufferSize) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(in);
            copy(fis, out, bufferSize);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out) throws IOException {
        copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    public static void copy(URL url, File out) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            IOUtils.copy(in, out);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * copy le flux d'entree dans le lux de sortie
     *
     * @param in  entree
     * @param out sortie
     * @throws IOException when IO error
     */
    public static void copy(InputStream in, File out, int bufferSize) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(out);
            copy(in, fos, bufferSize);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * retourne le nom du fichier (sans l'extension)
     *
     * @param f fichier
     * @return file name
     */
    public static String getFileName(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i == 0) {
            return "";
        } else if (i > 0) {
            return s.substring(0, i);
        } else {
            return s;
        }
    }

    /**
     * retourne l'extension d'un fichier
     *
     * @param f fichier
     * @return file extension
     */
    public static String getFileExtension(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i == 0) {
            return s.substring(1);
        } else if (i > 0) {
            if (i < (s.length() - 1)) {
                return s.substring(i + 1);
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public static String getFilePath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return file.getAbsolutePath();
        }
    }

    public static File canonize(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            try {
                return file.getAbsoluteFile();
            } catch (Exception e1) {
                return file;
            }
        }
    }

    /**
     * retourne le path relatif
     *
     * @param parent
     * @param son
     * @return relative path
     */
    public static String getRelativePath(File parent, File son) {
        String parentPath;
        String sonPath;
        try {
            parentPath = parent.getCanonicalPath();
            sonPath = son.getCanonicalPath();
        } catch (IOException e) {
            parentPath = parent.getAbsolutePath();
            sonPath = son.getAbsolutePath();
        }
        if (sonPath.startsWith(parentPath)) {
            String p = sonPath.substring(parentPath.length());
            if (p.startsWith("/") || p.startsWith("\\")) {
                p = p.substring(1);
            }
            return p;
        }
        return null;
    }

    public static byte[] loadStreamAsByteArray(URL url) throws IOException {
        InputStream r = null;
        try {
            return loadStreamAsByteArray(url);
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    public static byte[] loadStreamAsByteArray(InputStream r) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            copy(r, out);
            out.flush();
            return out.toByteArray();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static void storeXMLProperties(File file, Properties p, String comments) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            p.storeToXML(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static void storeProperties(File file, Properties p, String comments) throws IOException {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            p.store(os, comments);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    public static Properties loadXMLProperties(File file) throws IOException {
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            p.loadFromXML(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return p;
    }

    public static Properties loadProperties(File file) throws IOException {
        Properties p = new Properties();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            p.load(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return p;
    }

    public static String loadStreamAsString(URL url) throws IOException {
        return new String(loadStreamAsByteArray(url));
    }

    public static String loadStreamAsString(InputStream r) throws IOException {
        return new String(loadStreamAsByteArray(r));
    }

    public static void replaceInFolder(File folder, FileFilter fileFilter, boolean recurse, String oldContent, String newContent) throws IOException {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
                    replaceInFile(file, oldContent, newContent);
                } else if (recurse && file.isDirectory()) {
                    replaceInFolder(file, fileFilter, recurse, oldContent, newContent);
                }
            }
        }
    }

    public static boolean existsOrWaitIfStillWritingInto(File file) {
        return existsOrWaitIfStillWritingInto(file, 180);
    }

    public static boolean existsOrWaitIfStillWritingInto(File file, int secondsToWait) {
        if (file.exists()) {
            return true;
        }
        File file1 = new File(file.getPath() + WRITE_TEMP_EXT);
        if (file1.exists()) {
            for (int i = 0; i < secondsToWait * 2; i++) {
                if (!file1.exists()) {
                    return file.exists();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeIOException("File does not exist but an associated temp file failed to finish writing to " + file);
        }
        return false;
    }

    public static boolean deleteFolderTree(File folder, FileFilter fileFilter) {
        if (!folder.exists()) {
            return true;
        }
        File[] files = folder.listFiles(fileFilter);
        boolean ok = true;
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (fileFilter == null || fileFilter.accept(file))) {
                    if (!file.delete()) {
                        ok = false;
                    }
                } else if (file.isDirectory()) {
                    deleteFolderTree(file, fileFilter);
                } else {
                    ok = false;
                }
            }
        }
        return ok && folder.delete();
    }

    public static void replaceInFile(File file, String oldContent, String newContent) throws IOException {
        byte[] bytes = loadStreamAsByteArray(file.toURI().toURL());
        String str = new String(bytes);
        copy(new ByteArrayInputStream(str.replace(oldContent, newContent).getBytes()), file);
    }

    public static void replaceAllInFile(File file, String oldContent, String newContent) throws IOException {
        byte[] bytes = loadStreamAsByteArray(file.toURI().toURL());
        String str = new String(bytes);
        copy(new ByteArrayInputStream(str.replaceAll(oldContent, newContent).getBytes()), file);
    }

    public static String serializeObjectToString(Object object) throws IOException {
        return DEFAULT_STRING_SERIALIZER.serialize(object);
    }

    public static Object deserializeObjectToString(String str) throws IOException {
        return DEFAULT_STRING_SERIALIZER.deserialize(str);
    }

    public static void saveObject(String physicalName, Object object) throws IOException {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(physicalName));
            oos.writeObject(object);
            oos.close();
        } finally {
            if (oos != null) oos.close();
        }
    }

    public static Object loadObject(String physicalName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(physicalName));
            return ois.readObject();
        } finally {
            if (ois != null) ois.close();
        }
    }


    public static void saveZippedObject(String physicalName, Object object) throws IOException {
        saveZippedObject(physicalName, object, null, null);
    }

    public static void saveZippedObject(HFile physicalName, Object object, ProgressMonitor monitor, String messagePrefix) throws IOException {
        if (monitor == null) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new GZIPOutputStream(physicalName.getOutputStream()));
                oos.writeObject(object);
                oos.close();
            } finally {
                if (oos != null) oos.close();
            }
        } else {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new GZIPOutputStream(new ProgressMonitorOutputStream(physicalName.getOutputStream(), monitor, messagePrefix)));
                oos.writeObject(object);
                oos.close();
            } finally {
                if (oos != null) oos.close();
            }
        }
    }

    public static void saveZippedObject(String physicalName, Object object, ProgressMonitor monitor, String messagePrefix) throws IOException {
        String physicalNameTemp = physicalName + WRITE_TEMP_EXT;
        if (monitor == null) {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(physicalNameTemp)));
                oos.writeObject(object);
                oos.close();
                if (!new File(physicalNameTemp).renameTo(new File(physicalName))) {
                    throw new IOException("Unable to rename " + physicalNameTemp);
                }
            } finally {
                if (oos != null) oos.close();
            }
        } else {
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new GZIPOutputStream(new ProgressMonitorOutputStream(new FileOutputStream(physicalNameTemp), monitor, messagePrefix)));
                oos.writeObject(object);
                oos.close();
                if (!new File(physicalNameTemp).renameTo(new File(physicalName))) {
                    throw new IOException("Unable to rename " + physicalNameTemp);
                }
            } finally {
                if (oos != null) oos.close();
            }
        }
    }

    public static Object loadZippedObject(String physicalName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(physicalName)));
            return ois.readObject();
        } finally {
            if (ois != null) ois.close();
        }
    }


//    public static void main(String[] args) {
//        try {
//            replaceInFolder(new File("/home/vpc/xprojects/dbclient/src"),new FileFilter() {
//                public boolean accept(File pathname) {
//                    return pathname.getName().toLowerCase().endsWith(".java");
//                }
//            },      true,
//                            "package org.vpc.dbclient",
//                    "/**\n" +
//                            " * ====================================================================\n" +
//                            " *             DBCLient yet another Jdbc client tool\n" +
//                            " *\n" +
//                            " * DBClient is a new Open Source Tool for connecting to jdbc\n" +
//                            " * compliant relational databases. Specific extensions will take care of\n" +
//                            " * each RDBMS implementation.\n" +
//                            " *\n" +
//                            " * Copyright (C) 2006-2007 Taha BEN SALAH\n" +
//                            " *\n" +
//                            " * This program is free software; you can redistribute it and/or modify\n" +
//                            " * it under the terms of the GNU General Public License as published by\n" +
//                            " * the Free Software Foundation; either version 2 of the License, or\n" +
//                            " * (at your option) any later version.\n" +
//                            " *\n" +
//                            " * This program is distributed in the hope that it will be useful,\n" +
//                            " * but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
//                            " * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
//                            " * GNU General Public License for more details.\n" +
//                            " *\n" +
//                            " * You should have received a copy of the GNU General Public License along\n" +
//                            " * with this program; if not, write to the Free Software Foundation, Inc.,\n" +
//                            " * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.\n" +
//                            " * ====================================================================\n" +
//                            " */\n" +
//                            "\n" +
//                            "package org.vpc.dbclient"
//                    );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    public static boolean isFileURL(URL repositoryURL) {
        return "file".equalsIgnoreCase(repositoryURL.getProtocol());
    }

    public static String getURLPath(URL repositoryURL) {
        if ("file".equalsIgnoreCase(repositoryURL.getProtocol())) {
            File folder = new File(repositoryURL.getFile());
            try {
                return (folder.getCanonicalPath());
            } catch (IOException e) {
                return (folder.getAbsolutePath());
            }
        } else {
            return (repositoryURL.toString());
        }
    }

    /**
     * path expansion replaces ~ with ${user.home} property value
     *
     * @param path to expand
     * @return expanded path
     */
    public static String expandPath(String path) {
        if (path == null) {
            return path;
        }
        if (path.equals("~")) {
            return System.getProperty("user.home");
        }
        if (path.startsWith("~") && path.length() > 1 && (path.charAt(1) == '/' || path.charAt(1) == '/')) {
            return System.getProperty("user.home") + path.substring(1);
        }
        return path;
    }

    public static HFile createHFile(String absolutePath) {
        return new FolderHFileSystem(new File(absolutePath)).get("/");
    }

    public static HFile createHFile(File absolutePath) {
        return new FolderHFileSystem(absolutePath).get("/");
    }

    public static void writeToFile(String str, File file) throws IOException {
        if (str == null) {
            str = "";
        }
        try (PrintStream printStream = new PrintStream(file)) {
            printStream.print(str);
        }
    }

    public static String getArtifactVersion(String groupId, String artifactId) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
        if(url==null){
            throw new IOException("Not Such artifact : "+groupId + ":" + artifactId);
        }
        Properties p = new Properties();
        p.load(url.openStream());
        return p.getProperty("version");
    }

    public static String getArtifactVersionOrDev(String groupId, String artifactId) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");

        if (url != null) {
            Properties p = new Properties();
            try {
                p.load(url.openStream());
            } catch (IOException e) {
                //
            }
            String version = p.getProperty("version");
            if(!StringUtils.isEmpty(version)) {
                return version;
            }
        }
        return "DEV";
    }
}
