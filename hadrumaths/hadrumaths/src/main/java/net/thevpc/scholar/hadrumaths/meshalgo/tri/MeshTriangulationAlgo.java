package net.thevpc.scholar.hadrumaths.meshalgo.tri;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.util.NAssert;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.util.List;
import java.util.stream.Collectors;

public class MeshTriangulationAlgo implements MeshAlgo, Cloneable {

    private static final long serialVersionUID = 1L;
    private MeshTriangulationOptions option = new MeshTriangulationOptions();

    public MeshTriangulationAlgo(int maxTriangles) {
        this(new MeshTriangulationOptions().setMaxCount(maxTriangles));
    }

    public MeshTriangulationAlgo(MeshTriangulationOptions options) {
        this();
        this.option = NAssert.requireNamedNonNull(options,"options");
    }

    public MeshTriangulationAlgo() {
    }

    //    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("options", option);
//        return h.toString();
//    }
    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("options", NElementHelper.elem(option));
        return sb.build();
    }

    @Override
    public List<MeshZone> meshPolygon(HGeometry polygon) {
        MeshRefinement r = new MeshRefinement()
                .maxTriangles(option.getMaxCount())
                .maxSurface(option.getMaxArea())
                .maxIterations(option.getMaxIterations())
                .maxWidth(option.getMaxEdgeLength())
                ;
        List<HTriangle> triangles = MeshRefinementHelper.triangulate(polygon);
        for (HGeometry local : option.getLocals()) {
            triangles=MeshRefinementHelper.refineLocal(local,triangles, r);
        }
        triangles = MeshRefinementHelper.refineTriangles(triangles,r);
        return triangles.stream().map(x -> new MeshZone(x)).collect(Collectors.toList());
    }



    public MeshTriangulationOptions getOption() {
        return option;
    }

    public void setOption(MeshOptions op) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setOption(MeshTriangulationOptions op) {
        this.option = op;
    }

    public MeshTriangulationAlgo clone() {
        try {
            return (MeshTriangulationAlgo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Never");
        }
    }
}
