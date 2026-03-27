package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NListContainerElement;
import net.thevpc.nuts.elem.NPairElement;
import net.thevpc.nuts.math.NDoubleComplex;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.nuts.util.NOptional;
import net.thevpc.nuts.util.NStringUtils;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadruwaves.ModeIndex;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.Source;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;
import net.thevpc.scholar.hadruwaves.mom.sources.modal.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.ExprPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by vpc on 1/25/14.
 */
public class SourceFactory extends AbstractFactory {

    public static PlanarSources createPlanarSources(PlanarSource... sources) {
        return new DefaultPlanarSources(sources);
    }

    public static ModalSources createModalSources(ModeIndex... userDefined) {
        return new UserModalSources(userDefined);
    }

    public static ModalSources createIndexModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new IndexModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSourcesSingleMode(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSourcesSingleMode(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSourcesTE2n0(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSourcesTE2n0(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createCutOffModalSourcesTM02n(int defaultSourceCount, int... sourceCountPerDimension) {
        return new CutOffModalSourcesTM02n(defaultSourceCount, sourceCountPerDimension);
    }

    public static PlanarSources createPlanarSources() {
        return new DefaultPlanarSources();
    }


    public static PlanarSources createPlanarSources(Expr fct, Complex characteristicImpedance) {
        return createPlanarSources().add(createPlanarSource(fct, characteristicImpedance));
    }

    public static PlanarSources createPlanarSources(double value, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return createPlanarSources().add(createPlanarSource((polarization == null || polarization == Axis.X) ? value : 0, (polarization == null || polarization == Axis.Y) ? value : 0, characteristicImpedance, polarization, geometry));
    }

    public static PlanarSources createPlanarSources(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return createPlanarSources().add(createPlanarSource(xvalue, yvalue, characteristicImpedance, polarization, geometry));
    }

    public static PlanarSource createPlanarSource(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return new CstPlanarSource(xvalue, yvalue, characteristicImpedance, polarization, GeometryFactory.createPolygonList(geometry));
    }

    public static PlanarSources createPlanarSources(double value, Complex characteristicImpedance, Axis polarization, Domain geometry) {
        return createPlanarSources(value, characteristicImpedance, polarization, geometry.toGeometry());
    }

    public static PlanarSources createPlanarSources(double xvalue, double yvalue, Complex characteristicImpedance, Axis polarization, Domain geometry) {
        return createPlanarSources(xvalue, yvalue, characteristicImpedance, polarization, geometry.toGeometry());
    }

    public static PlanarSource createPlanarSource(double value, Complex characteristicImpedance, Axis polarization, Domain geometry) {
        return createPlanarSource(value, characteristicImpedance, polarization, geometry.toGeometry());
    }

    public static PlanarSource createPlanarSource(double value, Complex characteristicImpedance, Axis polarization, Geometry geometry) {
        return new CstPlanarSource((polarization == null || polarization == Axis.X) ? value : 0, (polarization == null || polarization == Axis.Y) ? value : 0, characteristicImpedance, polarization, GeometryFactory.createPolygonList(geometry));
    }

    public static PlanarSource createPlanarSource(Expr fct, Complex characteristicImpedance) {
        return createPlanarSource(fct, characteristicImpedance, null);
    }

    public static PlanarSource createPlanarSource(Expr fct, Complex characteristicImpedance, Geometry geometry) {
        return new ExprPlanarSource(fct, characteristicImpedance, geometry);
    }


    public static ModalSources createModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new IndexModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createInitialIndexModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new InitialIndexModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static ModalSources createGroupedCutOffModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        return new GroupedCutOffModalSources(defaultSourceCount, sourceCountPerDimension);
    }

    public static NOptional<Sources> parseSources(NElement element, Function<NElement,Geometry> geometrySupplier) {
        if (element.isNumber()) {
            NOptional<Source> i = parseSource(element, geometrySupplier);
            if(i.isPresent()) {
                Source p = i.get();
                if(p instanceof PlanarSource) {
                    return NOptional.of(createPlanarSources((PlanarSource) p));
                }
            }
            return NOptional.ofNamedError("source");
        }
        if(!element.isNamed() && element.isListContainer()) {
            List<PlanarSource> planarSources = new ArrayList<>();
            for (NElement child : element.asListContainer().get().children()) {
                NOptional<Source> i = parseSource(child, geometrySupplier);
                if(i.isPresent()) {
                    if(i.get() instanceof PlanarSource) {
                        planarSources.add((PlanarSource) i.get());
                    }
                }
            }
            return NOptional.of(createPlanarSources(planarSources.toArray(new PlanarSource[0])));
        }
        if(element.isNamed() && element.isListContainer()) {
            if (element.isNamed() && element.isListContainer()) {
                String name = element.asNamed().get().name().get();
                NListContainerElement body = element.asListContainer().get();
                switch (NNameFormat.LOWER_KEBAB_CASE.format(name)) {
                    case "planars": {
                        List<PlanarSource> ps=new ArrayList<>();
                        for (NElement child : body.children()) {
                            NOptional<Source> p = parseSource(child, geometrySupplier);
                            if(p.isPresent()) {
                                ps.add((PlanarSource) p.get());
                            }else{
                                return NOptional.ofNamedError("source");
                            }
                        }
                        return  NOptional.of(createPlanarSources(ps.toArray(new PlanarSource[0])));
                    }
                    case "planar": {
                        NOptional<Source> i = parseSource(element, geometrySupplier);
                        if(i.isPresent()) {
                            return NOptional.of(createPlanarSources((PlanarSource) i.get()));
                        }
                        List<PlanarSource> ps=new ArrayList<>();
                        for (NElement child : body.children()) {
                            NOptional<Source> p = parseSource(child, geometrySupplier);
                            if(p.isPresent()) {
                                ps.add((PlanarSource) p.get());
                            }else{
                                return NOptional.ofNamedError("source");
                            }
                        }
                        return  NOptional.of(createPlanarSources(ps.toArray(new PlanarSource[0])));
                    }
                    case "modal":{
                        List<ModeIndex> indices=null;
                        for (NElement child : body.children()) {
                            if (child.isNamedPair()) {
                                NPairElement p = child.asPair().get();
                                switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                                    case "modes":
                                    {
                                        if(!p.value().isNamed() && p.value().isListContainer()) {
                                            indices = p.value().asListContainer().get().children().stream().flatMap(x -> ModeIndex.parse(x).stream().stream()).collect(Collectors.toList());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        if(indices!=null){
                            return NOptional.of(createModalSources(indices.toArray(new ModeIndex[0])));
                        }
                        break;
                    }
                }
            }
        }
        return NOptional.ofNamedError("sources");
    }

    public static NOptional<Source> parseSource(NElement element, Function<NElement,Geometry> geometrySupplier) {
        if (element == null || element.isNull()) {
            return NOptional.ofNamedEmpty("source");
        }
        if (element.isNumber()) {
            Geometry g = geometrySupplier.apply(NElement.ofString("source"));
            Domain d = g == null ? null : g.getDomain();
            Expr r = Maths.expr(element);
            if (d != null) {
                r = r.mul(d);
            }
            return NOptional.of(createPlanarSource(r, Complex.of(50)));
        }
        if (element.isNamed() && element.isListContainer()) {
            String name = element.asNamed().get().name().get();
            NListContainerElement body = element.asListContainer().get();
            switch (NNameFormat.LOWER_KEBAB_CASE.format(name)) {
                case "planar": {
                    Complex impedance=Complex.of(50);
                    Expr value=null;
                    String sourceName=null;
                    for (NElement child : body.children()) {
                        if (child.isNamedPair()) {
                            NPairElement p = child.asPair().get();
                            switch (NNameFormat.LOWER_KEBAB_CASE.format(p.key().asStringValue().orElse(""))) {
                                case "z":
                                case "impedance":
                                {
                                    NDoubleComplex cc = p.value().asDoubleComplexValue().orNull();
                                    if (cc != null) {
                                        impedance=Complex.of(cc.realValue(),cc.imagValue());
                                    }
                                    break;
                                }
                                case "value":
                                {
                                    value = Maths.expr(p.value());
                                    break;
                                }
                                case "geometry":
                                {
                                    sourceName = p.value().asStringValue().orElse(null);
                                    break;
                                }
                            }
                        }
                    }
                    if(value==null){
                        value=DoubleExpr.of(1);
                    }
                    Geometry g = geometrySupplier.apply(NElement.ofString(NStringUtils.firstNonBlank(sourceName,"source")));
                    Domain d = g == null ? null : g.getDomain();
                    if (d != null) {
                        value = value.mul(d);
                    }
                    if(impedance==null){
                        impedance=Complex.of(50);
                    }
                    return NOptional.of(createPlanarSource(
                            value, impedance
                    ));
                }
            }
        }
        return  NOptional.ofNamedEmpty("source");
    }
}
