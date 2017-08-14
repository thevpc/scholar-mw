package net.vpc.scholar.hadrumaths;

import java.io.Serializable;

public class DBLargeMatrixId implements Serializable {
    static final String SEP = ":\0";
    private String type;
    private String login;
    private String password;
    private String url;
    private String driver;
    private boolean sparse;
    private Complex defaultValue;

    public DBLargeMatrixId(String type, String login, String password, String driver, String url, boolean sparse, Complex defaultValue) {
        this.type = type==null?"":type;
        this.login = login==null?"":login;
        this.password = password==null?"":password;
        this.url = url==null?"":url;
        this.driver = driver==null?"":driver;
        this.defaultValue = defaultValue;
        this.sparse = sparse;
    }

    public String getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public boolean isSparse() {
        return sparse;
    }

    public Complex getDefaultValue() {
        return defaultValue;
    }

    public String toString(){
        return "largeMatrix"+ SEP+type+ SEP +login+SEP +password+SEP +driver+SEP +url+SEP +sparse+SEP +(defaultValue==null?"":defaultValue.toString())+SEP+".";
    }

    public static DBLargeMatrixId parse(String s){
        if(s==null){
            return null;
        }
        try {
            String[] split = s.split(SEP);
            String largeMatrix = split[0];
            if (!"largeMatrix".equals(largeMatrix)) {
                return null;
            }
            String type = split[1];
            String login = split[2];
            String password = split[3];
            String driver = split[4];
            String url = split[5];
            String sparse = split[6];
            String defaultValue = split[7];
            return new DBLargeMatrixId(type, login, password, driver, url,Boolean.valueOf(sparse),defaultValue.isEmpty()?null:Complex.valueOf(defaultValue));
        }catch (Exception e){
            return null;
        }
    }
}
