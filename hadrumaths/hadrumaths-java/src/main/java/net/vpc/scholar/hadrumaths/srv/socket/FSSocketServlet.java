package net.vpc.scholar.hadrumaths.srv.socket;

import net.vpc.common.io.IOUtils;
import net.vpc.scholar.hadrumaths.io.HFile;
import net.vpc.scholar.hadrumaths.io.HFileSystem;
import net.vpc.scholar.hadrumaths.srv.FSConstants;
import net.vpc.scholar.hadrumaths.srv.FSServlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FSSocketServlet extends AbstractHSocketServlet {

    public FSSocketServlet(FSServlet id) {
        super(id);
    }

    public HFileSystem getFileSystem() {
        FSServlet baseServlet = (FSServlet) getBaseServlet();
        return baseServlet.getFileSystem();
    }

    @Override
    public void service(DataInputStream in, DataOutputStream out) throws IOException {
        FSConstants.Command call = FSConstants.Command.values()[in.readByte()];
//        System.out.println("CALL "+call);
        switch (call) {
            case STAT: {
                String path = in.readUTF();
                HFile file = getFileSystem().get(path);
                success(out);
                if (file == null) {
                    out.writeByte(FSConstants.TYPE_NOT_FOUND);
                } else {
                    if (file.isFile()) {
                        out.writeByte(FSConstants.TYPE_FILE);
                        out.writeLong(file.length());
                    } else if (file.isDirectory()) {
                        out.writeByte(FSConstants.TYPE_FOLDER);
                        HFile[] files = file.listFiles();
                        out.writeInt(files == null ? 0 : files.length);
                    } else {
                        out.writeByte(FSConstants.TYPE_OTHER);
                    }
                }
                break;
            }
            case GET: {
                String path = in.readUTF();
                HFile file = getFileSystem().get(path);
                if (file == null) {
                    error("File not found", out);
                } else {
                    if (file.isFile()) {
                        success(out);
                        out.writeLong(file.length());
                        try (InputStream is = file.getInputStream()) {
                            IOUtils.copy(is, out, 1024 * 1024);
                        }
                    } else {
                        error("Not a folder", out);
                    }
                }
                break;
            }
            case LIST: {
                String path = in.readUTF();
//                System.out.println("path "+path);
                HFile file = getFileSystem().get(path);
                if (file == null) {
                    error("invalid path : " + path, out);
                } else {
                    if (!file.exists()) {
                        error("invalid path : " + path, out);
                    } else if (file.isDirectory()) {
                        success(out);
                        HFile[] files = file.listFiles();
                        int v = files == null ? 0 : files.length;
//                        System.out.println(v+" files");
                        out.writeInt(v);
                        if (files != null) {
                            for (HFile ff : files) {
//                                System.out.println("\t "+ff.getName());
                                out.writeUTF(ff.getPath());
                            }
                        }
                    } else {
                        error("Not a folder : " + path, out);
                    }
                }
                break;
            }
            default: {
                error("Invalid Command " + call, out);
                break;
            }
        }
    }
}
