package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.Element3DRenderPrefs;
import net.vpc.scholar.hadruplot.libraries.calc3d.vpc.element3d.Element3DParallelipiped;
import net.vpc.scholar.hadruwaves.Material;
import net.vpc.scholar.hadruwaves.project.scene.elem.Element3DParallelipipedTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruwaves.Boundary;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class HWProjectBrick extends AbstractHWProjectComponentMaterial implements HWProjectElementMaterialVolume {

    public static enum Face {
        NORTH, EAST, SOUTH, WEST, BOTTOM, TOP
    }
    private HWProjectElementBrickFace[] faces;

    public HWProjectBrick(String name, HWMaterialTemplate material, Element3DParallelipipedTemplate geometry) {
        super(name, material, geometry);
        this.faces = new HWProjectElementBrickFace[Face.values().length];
        for (int i = 0; i < this.faces.length; i++) {
            Face f = Face.values()[i];
            this.faces[i] = new HWProjectElementBrickFace(null, f, f.name() + " Face", this);
        }
    }

    public HWProjectElementBrickFace[] faces() {
        return Arrays.copyOf(faces, faces.length);
    }

    public HWProjectElementBrickFace face(Face face) {
        return faces[face.ordinal()];
    }

    @Override
    public List<Element3D> toElements3DImpl(HWConfigurationRun configuration) {
        List<Element3D> e = new ArrayList<>();
        Element3DParallelipiped geo = (Element3DParallelipiped) geometry().get().toElements3D(configuration).copy();
        Material m = material().get().eval(configuration);
        preparePrefs(m, geo.prefs(), selected().get());
        for (int i = 0; i < this.faces.length; i++) {
            Face f = Face.values()[i];
            Boundary fm = face(f).boundary().eval(configuration);
            if (fm != null) {
                boolean boundarySelectd = selected().get();
                Element3DRenderPrefs p = geo.prefs().copy();
                preparePrefs(fm, p, boundarySelectd);
                if (!face(f).visible().get()) {
                    p.setVisible(false);
                }
                geo.setFacePrefs(i, p);
            }
        }
        e.add(geo);
        for (Annotation3D annotation : annotations()) {
            for (Element3D element3D : annotation.toElements3D()) {
                if (element3D != null) {
                    e.add(element3D);
                }
            }
        }
        return e;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder obj = (TsonObjectBuilder) super.toTsonElement(context).builder();
        obj.add("faces",
                Tson.array().addAll(Arrays.stream(faces)
                        .map(x -> x.toTsonElement(context)).collect(Collectors.toList()))
        );
        return obj.build();
    }

}
