package net.vpc.scholar.hadruwaves.mom.sources.modal;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 8 juin 2007 21:16:30
 */
public class UserModalSources extends AbstractPropagatingModalSources {
//    public static void main(String[] args) {
//        System.out.println(new UserModalSources(ModeIndex.mode(ModeType.TE, 1, 0), ModeIndex.mode(ModeType.TM, 3, 0)).dump());
//    }

    private ModeIndex[] userDefined;
    private Map<ModeIndex, Integer> modeToIndexMap;

    public UserModalSources(ModeIndex... userDefined) {
        this(userDefined, userDefined.length);
    }

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount) {
        ModeIndex[] all = new ModeIndex[propagtiveModesCount];
        for (int i = 0; i < all.length; i++) {
            all[i] = allModes[i].getMode();

        }
        return all;
    }

    public UserModalSources(ModeIndex[] userDefined, int defaultSourceCount, int... sourceCountPerDimension) {
        super(defaultSourceCount, sourceCountPerDimension);
        this.userDefined = userDefined;
        if (defaultSourceCount > this.userDefined.length) {
            throw new IllegalArgumentException("Too few modes defined : " + defaultSourceCount + ">" + this.userDefined.length);
        }
        modeToIndexMap = new HashMap<ModeIndex, Integer>(userDefined.length);
        for (int i = 0; i < userDefined.length; i++) {
            modeToIndexMap.put(userDefined[i], i);
        }
    }

    public int getPropagatingIndex(ModeInfo index) {
        Integer p = modeToIndexMap.get(index.getMode());
        return p == null ? -1 : p;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("modes",context.elem(userDefined));
        return h.build();
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserModalSources that = (UserModalSources) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(userDefined, that.userDefined)) return false;
        return modeToIndexMap != null ? modeToIndexMap.equals(that.modeToIndexMap) : that.modeToIndexMap == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(userDefined);
        result = 31 * result + (modeToIndexMap != null ? modeToIndexMap.hashCode() : 0);
        return result;
    }
}
