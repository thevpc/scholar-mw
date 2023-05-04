package net.thevpc.scholar.hadruwavesstudio.standalone.v1.buildactions;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 11 mars 2005
 * Time: 22:55:56
 * To change this template use File | Settings | File Templates.
 */
public class BuildCache {
    public File folder;
    public String name="NONAME";
    public double frequence=0;
    public double epsr=0;
    public double thickness=0;
    public int fnTE=0;
    public int fnTM=0;
    public int gn=0;
    public String desc="-";
    public BuildCache(File folder) {
        this.folder = folder;
    }

}
