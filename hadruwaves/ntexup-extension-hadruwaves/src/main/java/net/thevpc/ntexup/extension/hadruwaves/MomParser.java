package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.document.node.NTxNode;
import net.thevpc.ntexup.api.renderer.NTxNodeRendererContext;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElement;
import net.thevpc.nuts.util.NBlankable;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.BoxSpace;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsBuilder;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsFactory;
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions;

import java.util.Objects;

public class MomParser {
    static MomStructure createMomStructure(NTxNode node, NTxNodeRendererContext rendererContext) {
        //h will populate from node later
        MomStructure str = new MomStructure();
        String geometryId = rendererContext.evalExpression(node.getPropertyValue("geometry").orNull(), node).flatMap(NElement::asStringValue).orNull();
        if (geometryId == null) {
            return null;
        }
        NTxNode otherNode = rendererContext.findNodeByProperty("id", geometryId, node).orNull();
        if (otherNode == null || !Objects.equals(otherNode.type(), "scene3d")) {
            return null;
        }

        int modes = rendererContext.evalExpression(node.getPropertyValue("modes").orNull(), node).flatMap(NElement::asIntValue).orElse(1000);
        NObjectElement boundaries = rendererContext.evalExpression(node.getPropertyValue("boundaries").orNull(), node).flatMap(NElement::toObject).orNull();
        if (boundaries != null) {
            String lat = boundaries.getStringValue("lateral").orNull();
            str.setBorders(WallBorders.of(lat));
            BoxSpace bottom = deserializeBoxSpace(boundaries.get("bottom").orNull());
            if (bottom == null) {
                bottom = BoxSpace.shortCircuit(Material.PEC, 1E-10);
            }
            str.setFirstBoxSpace(bottom);
            BoxSpace top = deserializeBoxSpace(boundaries.get("top").orNull());
            if (top == null) {
                top = BoxSpace.shortCircuit(Material.PEC, 1E-10);
            }
            str.setSecondBoxSpace(top);
        }
        str.setModeFunctions(new BoxModeFunctions().setSize(modes));
        TestFunctionsBuilder b = TestFunctionsFactory.createBuilder();
        for (NTxNode child : otherNode.children()) {
            if ("box".equals(child.type())) {
//                child.get
            }
            String material = rendererContext.evalExpression(child.getPropertyValue("material").orNull(), node).flatMap(NElement::asStringValue).orNull();
            if (!NBlankable.isBlank(material)) {
                if (material.equalsIgnoreCase("pec")) {

                }
            }
        }
        str.setTestFunctions(b.buildRooftops());
        return str;
    }

    static BoxSpace deserializeBoxSpace(NElement e) {
        if (e == null) {
            return null;
        }
        if (e.isAnyString()) {
            switch (e.asStringValue().orElse("").toLowerCase()) {
                case "matchedload":
                case "matched-load":
                case "matched_load": {
                    return BoxSpace.matchedLoad();
                }
                case "nothing":
                case "none": {
                    return BoxSpace.nothing();
                }
                case "open": {
                    return BoxSpace.openCircuit(Material.VACUUM, 1E-10);
                }
                default: {
                    return BoxSpace.nothing();
                }

            }
//            if (e.isNamedListContainer() && e.isListContainer()) {
//                String name = e.asNamed().get().name().orNull();
//                List<NElement> children = e.asListContainer().get().children();
//                switch (NStringUtils.trim(name).toLowerCase()) {
//                    case "matchedload":
//                    case "matched-load":
//                    case "matched_load": {
//                        for (NElement child : children) {
//                            if (child.isAnyString()) {
//                                switch (child.asStringValue().orElse("").toLowerCase()) {
//                                    case "pec": {
//                                        return BoxSpace.matchedLoad(Material.PEC);
//                                    }
//                                    case "vacuum": {
//                                        return BoxSpace.matchedLoad(Material.VACUUM);
//                                    }
//                                }
//                                return BoxSpace.matchedLoad(Material.VACUUM);
//                            }
//                        }
//                        return BoxSpace.matchedLoad();
//                    }
//                    case "nothing":
//                    case "none": {
//                        return BoxSpace.nothing();
//                    }
//                    case "open": {
//                        return BoxSpace.openCircuit(Material.VACUUM, 1E-10);
//                    }
//                    default: {
//                        return BoxSpace.nothing();
//                    }
//
//                }
//            }
        }
        return BoxSpace.nothing();
    }
}
