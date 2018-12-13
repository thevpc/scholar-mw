package net.vpc.scholar.hadrumaths.io;

import net.vpc.common.io.FileUtils;
import net.vpc.common.strings.StringUtils;
import net.vpc.common.util.mon.ProgressMonitorOutputStream;
import net.vpc.common.io.RuntimeIOException;
import net.vpc.common.util.mon.ProgressMonitor;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.zip.GZIPOutputStream;

/**
 * Classe utilitaire pour la manipulation des E/S
 *
 * @author tbensalah (Taha Ben Salah)
 * @creation_date date 27/01/2004
 * @last_modification_date date 28/01/2004
 */
public final class HadrumathsIOUtils {

      private static StringSerializer DEFAULT_STRING_SERIALIZER = new DefaultStringSerializer();
    public static final String WRITE_TEMP_EXT = ".temp";

    

   
   

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



    public static String serializeObjectToString(Object object) throws RuntimeIOException {
        try {
            return DEFAULT_STRING_SERIALIZER.serialize(object);
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static Object deserializeObjectToString(String str) throws RuntimeIOException {
        try {
            return DEFAULT_STRING_SERIALIZER.deserialize(str);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    public static void saveZippedObject(String physicalName, Object object) throws RuntimeIOException {
        saveZippedObject(physicalName, object, null, null);
    }

    public static void saveZippedObject(HFile physicalName, Object object, ProgressMonitor monitor, String messagePrefix) throws RuntimeIOException {
        try {
            if (monitor == null) {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new GZIPOutputStream(physicalName.getOutputStream()));
                    oos.writeObject(object);
                    oos.close();
                } finally {
                    if (oos != null) {
                        oos.close();
                    }
                }
            } else {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new GZIPOutputStream(new ProgressMonitorOutputStream(physicalName.getOutputStream(), monitor, messagePrefix)));
                    oos.writeObject(object);
                    oos.close();
                } finally {
                    if (oos != null) {
                        oos.close();
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
        }
    }

    public static void saveZippedObject(String physicalName, Object object, ProgressMonitor monitor, String messagePrefix) throws RuntimeIOException {
        physicalName = FileUtils.expandPath(physicalName);
        String physicalNameTemp = physicalName + WRITE_TEMP_EXT;
        try {
            if (monitor == null) {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(physicalNameTemp)));
                    oos.writeObject(object);
                    oos.close();
                    if (!new File(physicalNameTemp).renameTo(new File(physicalName))) {
                        throw new RuntimeIOException("Unable to rename " + physicalNameTemp);
                    }
                } finally {
                    if (oos != null) {
                        oos.close();
                    }
                }
            } else {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new GZIPOutputStream(new ProgressMonitorOutputStream(new FileOutputStream(physicalNameTemp), monitor, messagePrefix)));
                    oos.writeObject(object);
                    oos.close();
                    if (!new File(physicalNameTemp).renameTo(new File(physicalName))) {
                        throw new RuntimeIOException("Unable to rename " + physicalNameTemp);
                    }
                } finally {
                    if (oos != null) {
                        oos.close();
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeIOException(ex);
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

    

    public static HFile createHFile(String absolutePath) {
        return new FolderHFileSystem(new File(FileUtils.expandPath(absolutePath))).get("/");
    }

    public static HFile createHFile(File absolutePath) {
        return new FolderHFileSystem(absolutePath).get("/");
    }

//    public static String getArtifactVersion(String groupId, String artifactId) throws IOException {
//        URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
//        if (url == null) {
//            throw new IOException("No Such artifact : " + groupId + ":" + artifactId);
//        }
//        Properties p = new Properties();
//        p.load(url.openStream());
//        return p.getProperty("version");
//    }

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
            if (!StringUtils.isEmpty(version)) {
                return version;
            }
        }
        return "DEV";
    }
}
