package net.vpc.scholar.hadrumaths.cache;

/**
 * Created by vpc on 5/30/14.
 */
public class DumpPath {
    private String dump;
    private String path;

    public DumpPath(String dump) {
        this.dump = dump;

        String hh = Integer.toString(dump.hashCode(), 36).toLowerCase();
        if (hh.startsWith("-")) {
            hh = hh.substring(1);
        }
        StringBuilder sb = new StringBuilder("");
        int j = 0;
        for (int i = 0; i < hh.length(); i++) {
            if (j == 2) {
                sb.append("/");
                j = 0;
            }
            sb.append(hh.charAt(i));
            j++;
        }
        this.path= sb.toString();
    }

    public String getDump() {
        return dump;
    }

    public String getPath() {
        return path;
    }


    @Override
    public String toString() {
        return "DumpPath{" +
                "dump='" + dump + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
