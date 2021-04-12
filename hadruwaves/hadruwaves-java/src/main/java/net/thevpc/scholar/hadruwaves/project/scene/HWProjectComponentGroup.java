package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.common.props.WritableLiMap;

import java.util.List;
import java.util.function.Predicate;

public interface HWProjectComponentGroup extends HWProjectComponent{
    WritableLiMap<String,HWProjectComponent> children();

    //    @Override
    HWProjectComponent findElement(String path);

    //    @Override
    HWProjectComponent findElement(String path, boolean createFolder);

    //    @Override
    HWProjectComponent remove(String path);

    //    @Override
    void add(HWProjectComponent element, String path);

    //    @Override
    List<HWProjectComponent> removeDeepComponents(Predicate<HWProjectComponent> filter, boolean prune);
}
