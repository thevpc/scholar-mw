package net.vpc.scholar.hadruwaves;

public enum ModeType {
    TEM,TE,TM;
    public ModeIndex index(int m,int n){
        return ModeIndex.mode(this,m,n);
    }
}
