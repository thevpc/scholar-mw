package net.vpc.scholar.hadrumaths.srv;

public class FSConstants {
    public static final byte TYPE_NOT_FOUND =0;
    public static final byte TYPE_FILE =1;
    public static final byte TYPE_FOLDER =2;
    public static final byte TYPE_OTHER =3;
    public enum Command {
        STAT,
        GET,
        LIST,
    };
}
