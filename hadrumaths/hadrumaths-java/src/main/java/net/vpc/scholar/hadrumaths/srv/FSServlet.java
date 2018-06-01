package net.vpc.scholar.hadrumaths.srv;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.io.HFile;
import net.vpc.scholar.hadrumaths.io.HFileSystem;
import net.vpc.scholar.hadrumaths.io.IOUtils;
import net.vpc.scholar.hadrumaths.srv.rmi.FSRemoteImpl;
import net.vpc.scholar.hadrumaths.srv.socket.AbstractHSocketServlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class FSServlet implements HadrumathsServlet {
    private String id;
    public FSServlet(String id) {
        this.id=id;
    }

    @Override
    public String getId() {
        return id;
    }

    public HFileSystem getFileSystem() {
        return Maths.Config.getCacheFileSystem();
    }

}
