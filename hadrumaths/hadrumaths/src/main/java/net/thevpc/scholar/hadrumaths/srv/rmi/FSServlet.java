//package net.thevpc.scholar.hadrumaths.srv.rmi;
//
//import net.thevpc.scholar.hadrumaths.Maths;
//import net.thevpc.scholar.hadrumaths.io.HFile;
//import net.thevpc.scholar.hadrumaths.io.HFileSystem;
//import net.thevpc.scholar.hadrumaths.io.IOUtils;
//import net.thevpc.scholar.hadrumaths.srv.socket.AbstractHSocketServlet;
//import net.thevpc.scholar.hadrumaths.srv.FSConstants;
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.rmi.Remote;
//import java.rmi.RemoteException;
//
//public class FSServlet extends AbstractHSocketServlet {
//
//    public FSServlet(String id) {
//        super(id);
//    }
//
//    public HFileSystem getFileSystem() {
//        return Maths.Config.getCacheFileSystem();
//    }
//
//    @Override
//    public void service(DataInputStream in, DataOutputStream out) throws IOException {
//        FSConstants.Command call = FSConstants.Command.values()[in.readByte()];
////        System.out.println("CALL "+call);
//        switch (call) {
//            case STAT: {
//                String path = in.readUTF();
//                HFile file = getFileSystem().get(path);
//                success(out);
//                if (file == null) {
//                    out.writeByte(FSConstants.TYPE_NOT_FOUND);
//                } else {
//                    if (file.isFile()) {
//                        out.writeByte(FSConstants.TYPE_FILE);
//                        out.writeLong(file.length());
//                    } else if (file.isDirectory()) {
//                        out.writeByte(FSConstants.TYPE_FOLDER);
//                        HFile[] files = file.listFiles();
//                        out.writeInt(files == null ? 0 : files.length);
//                    } else {
//                        out.writeByte(FSConstants.TYPE_OTHER);
//                    }
//                }
//                break;
//            }
//            case GET: {
//                String path = in.readUTF();
//                HFile file = getFileSystem().get(path);
//                if (file == null) {
//                    error("File not found", out);
//                } else {
//                    if (file.isFile()) {
//                        success(out);
//                        out.writeLong(file.length());
//                        try (InputStream is = file.getInputStream()) {
//                            IOUtils.copy(is, out, 1024 * 1024);
//                        }
//                    } else {
//                        error("Not a folder", out);
//                    }
//                }
//                break;
//            }
//            case LIST: {
//                String path = in.readUTF();
////                System.out.println("path "+path);
//                HFile file = getFileSystem().get(path);
//                if (file == null) {
//                    error("invalid path : " + path, out);
//                } else {
//                    if (!file.exists()) {
//                        error("invalid path : " + path, out);
//                    } else if (file.isDirectory()) {
//                        success(out);
//                        HFile[] files = file.listFiles();
//                        int v = files == null ? 0 : files.length;
////                        System.out.println(v+" files");
//                        out.writeInt(v);
//                        if (files != null) {
//                            for (HFile ff : files) {
////                                System.out.println("\t "+ff.getName());
//                                out.writeUTF(ff.getPath());
//                            }
//                        }
//                    } else {
//                        error("Not a folder : " + path, out);
//                    }
//                }
//                break;
//            }
//            default: {
//                error("Invalid Command " + call, out);
//                break;
//            }
//        }
//    }
//
//    @Override
//    public Remote export() {
//        try {
//            return new FSRemoteImpl(this);
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public interface FSInputStream extends Remote {
//        byte[] read(int max) throws RemoteException;
//    }
//
//}
