package net.thevpc.scholar.hadruwaves;

import net.thevpc.nuts.elem.NUpletElement;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;

public enum ModeType {
    TEM,TE,TM;
    public ModeIndex index(int m,int n){
        return ModeIndex.mode(this,m,n);
    }

    public static NOptional<ModeType> parse(String s){
        if(s==null){
            return NOptional.ofNamedEmpty("mode-type");
        }
        switch (NNameFormat.LOWER_KEBAB_CASE.format(s)){
            case "tem":return NOptional.of(ModeType.TEM);
            case "te":return NOptional.of(ModeType.TE);
            case "tm":return NOptional.of(ModeType.TM);
        }
        return NOptional.ofNamedError("mode-type");
    }
}
