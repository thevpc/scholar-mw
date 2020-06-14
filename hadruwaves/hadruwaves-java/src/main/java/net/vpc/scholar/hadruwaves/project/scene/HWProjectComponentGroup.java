package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruwaves.project.HWProjectComponent;
import net.vpc.common.props.WritablePLMap;

import java.util.List;
import java.util.function.Predicate;

public interface HWProjectComponentGroup extends HWProjectComponent{
    WritablePLMap<String,HWProjectComponent> children();

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
