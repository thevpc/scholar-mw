package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.thevpc.echo.AppIconResolver;

import java.io.File;

public class HWAppIconResolver implements AppIconResolver {

    @Override
    public String iconIdForFile(File f, boolean selected, boolean expanded) {
        if(f==null){
            return null;
        }
        if(f.isFile()) {
            return iconIdForFileName(f.getName(), selected, expanded);
        }else{
            if(f.getPath().equals(System.getProperty("user.home"))){
                return "FolderHome";
            }
            if(new File(f,"pom.xml").isFile()){
                return "FolderExperiment";
            }
            if(HWUtils.getSolutionFiles(f).length>0){
                return "Solution";
            }
            if(HWUtils.getProjectFiles(f).length>0){
                return "Project";
            }
            if(HWUtils.getProjectFiles(f.getParentFile()).length>0) {
                if (f.getName().equals("build")) {
                    return "FolderBuild";
                }
                if (f.getName().equals("code")) {
                    return "FolderExperiment";
                }
                if (f.getName().equals("results")) {
                    return "FolderResults";
                }
            }
            return (expanded ? "OpenFolder" : "Folder");
        }
    }

    @Override
    public String iconIdForFileName(String n, boolean selected, boolean expanded) {
        String[] splitted = n.split("[.]");
        for (int i = 0; i < splitted.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < splitted.length; j++) {
                if (sb.length() > 0) {
                    sb.append(".");
                }
                sb.append(splitted[i]);
            }
            String icon = iconForFileExtension(sb.toString());
            if (!"File".equals(icon)) {
                return icon;
            }
        }
        return "File";
    }

    public String iconForFileExtension(String suffix) {
        switch (suffix.toLowerCase()) {
            case "java":
                return "Java";
            case "scala":
                return "Scala";
            case "Main.hl":
            case "hl":
                return "Hadra";
            case "txt":
            case "properties":
                return "Text";
            case "pom.xml":
            case "xml":
                return "Xml";
            case "tson":
                return "Tson";
            case "json":
                return "Json";
            case "hwp.tson":
                return "Project";
            case "hws.tson":
                return "Solution";
            case "png":
            case "jpg":
            case "jpeg":
            case "gif":
            case "bmp":
                return "Image";
            case "jfb":
            case "hwr":
            case "hwr.tson":
                return "FileResults";
            case "exe":
            case "bin":
            case "sh":
            case "bat":
            case "py":
            case "pl":
                return "FileExe";
            case "jar":
                return "Java";
            case "war":
            case "zip":
            case "ear":
            case "rar":
            case "tar":
            case "tar.gz":
            case "gz":
            case "7z":
            case "ar":
            case "iso":
            case "bz2":
            case "lz4":
            case "z":
            case "Z":
            case "xz":
            case "apk":
            case "arj":
            case "cab":
            case "dmg":
            case "pak":
            case "xar":
            case "zpaq":
            case "zz":
                return "FileZip";
            case "doc":
            case "docx":
            case "odt":
                return "FileWriter";
            case "xls":
            case "xlsx":
            case "ods":
                return "FileXls";
            case "ppt":
            case "pptx":
            case "odp":
                return "FilePpt";
            case "csv":
                return "FileCsv";
            case "pdf":
                return "FilePdf";
            case "dll":
            case "so":
                return "FileBin";
        }
        return "File";
    }


}
