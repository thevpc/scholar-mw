package net.vpc.scholar.hadrumaths.srv;

import java.io.Serializable;

public class FileStat implements Serializable {
    public byte type;
    public long length;

    @Override
    public String toString() {
        String stype = "?";
        switch (type) {
            case FSConstants.TYPE_NOT_FOUND:
                stype = "NOT_FOUND";
                break;
            case FSConstants.TYPE_FILE:
                stype = "FILE     ";
                break;
            case FSConstants.TYPE_FOLDER:
                stype = "FOLDER   ";
                break;
            case FSConstants.TYPE_OTHER:
                stype = "OTHER    ";
                break;
        }
        return "FileStat{" +
                "type=" + stype +
                ", length=" + length +
                '}';
    }
}
