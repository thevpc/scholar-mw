package net.vpc.scholar.hadrumaths.util.config;

import java.io.File;
import java.io.IOException;

public class LoadMetaDataHandler implements MetaDataHandler {
    public boolean handleMetaData(Properties2 props, String command, String metadata) {
        if ("load".equals(command)) {
            Properties2 p2 = new Properties2();
            try {
                p2.load(new File(props.getLoadingFile() == null ? null : props.getLoadingFile().getParentFile(), metadata.substring(4).trim()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            props.putAll(p2);
            return true;
        }
        return false;
    }
}
