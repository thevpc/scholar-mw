package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Element3DRenderPrefs;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d.Element3DParallelipiped;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DParallelipipedTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

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
    public NElement toElement() {
        NObjectElementBuilder obj = (NObjectElementBuilder) super.toElement().builder();
        obj.add("faces",
                NElement.ofArrayBuilder().addAll(Arrays.stream(faces)
                        .map(x -> x.toElement()).collect(Collectors.toList()))
                        .build()
        );
        return obj.build();
    }

}
