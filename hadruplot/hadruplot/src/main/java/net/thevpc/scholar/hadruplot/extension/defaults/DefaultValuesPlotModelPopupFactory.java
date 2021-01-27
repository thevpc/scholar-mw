package net.thevpc.scholar.hadruplot.extension.defaults;

import net.thevpc.scholar.hadruplot.extension.PlotModelPopupFactory;
import net.thevpc.scholar.hadruplot.model.ValuesPlotModel;
import net.thevpc.common.strings.StringUtils;
import net.thevpc.scholar.hadruplot.*;

import javax.swing.*;
import java.util.stream.Stream;

public class DefaultValuesPlotModelPopupFactory implements PlotModelPopupFactory {
    @Override
    public void preparePopup(final PlotModelPopupFactoryContext context) {
        final ValuesPlotModel model = (ValuesPlotModel) context.getModel();
        final LibraryPlotType t = model.getPlotType();

        if (context.getViewMenu() != null) {
            ButtonGroup group = new ButtonGroup();
            for (PlotType plotType : PlotType.values()) {
                if (!plotType.equals(PlotType.ALL) && !plotType.equals(PlotType.AUTO)) {
                    PlotLibrary[] libraries =
                            Stream.of(PlotBackendLibraries.getLibraries()).filter(x -> x.getSupportLevel(plotType) > 0)
                                    .sorted((x, y) -> Integer.compare(x.getSupportLevel(plotType), y.getSupportLevel(plotType)))
                                    .toArray(PlotLibrary[]::new);
                    if (libraries.length == 0) {
                        LibraryPlotType pt = new LibraryPlotType(plotType, null);
                        JCheckBoxMenuItem f = new JCheckBoxMenuItem(new Plot.PlotTypeAction(context.getModelProvider(), pt.toString(), pt));
                        f.setSelected(matches(t, pt));
                        group.add(f);
                        context.getViewMenu().add(f);
                    } else if (libraries.length == 1) {
                        LibraryPlotType pt = new LibraryPlotType(plotType, libraries[0].getName());
                        JCheckBoxMenuItem f = new JCheckBoxMenuItem(new Plot.PlotTypeAction(context.getModelProvider(), pt.toString(), pt));
                        f.setSelected(matches(t, pt));
                        group.add(f);
                        context.getViewMenu().add(f);
                    } else {
                        JMenu m = new JMenu(StringUtils.toCapitalized(plotType.name()));
                        context.getViewMenu().add(m);
                        for (PlotLibrary lib : libraries) {
                            LibraryPlotType pt = new LibraryPlotType(plotType, lib.getName());

                            JCheckBoxMenuItem f = new JCheckBoxMenuItem(new Plot.PlotTypeAction(context.getModelProvider(), StringUtils.toCapitalized(lib.getName()), pt));
                            f.setSelected(matches(t, pt));
                            group.add(f);
                            m.add(f);
                        }
                    }
                }
            }
        }
//        if (context.getLibrariesMenu() != null) {
//            PlotSwingUtils.setOnRefreshComponent(context.getLibrariesMenu(), new OnRefreshComponent() {
//                @Override
//                public void onRefreshComponent(Component component) {
//                    final JMenu menu = (JMenu) component;
//                    PlotLibrary[] allLibraries = PlotBackendLibraries.getLibraries();
//                    final Set<String> availableLibs = new LinkedHashSet<>();
//                    for (PlotLibrary allLibrary : allLibraries) {
//                        availableLibs.add(allLibrary.getName());
//                    }
//                    final Map<String, JCheckBoxMenuItem> visitedLibraries = new HashMap<>();
//                    ButtonGroup group = null;
//                    for (Component menuComponent : menu.getMenuComponents()) {
//                        JCheckBoxMenuItem b = (JCheckBoxMenuItem) menuComponent;
//                        String libName = (String) b.getClientProperty("LibraryName");
//                        if (group == null) {
//                            group = (ButtonGroup) b.getClientProperty("ButtonGroup");
//                        }
//                        if (!availableLibs.contains(libName)) {
//                            b.setEnabled(false);
//                        }
//                        visitedLibraries.put(libName, b);
//                    }
//                    SimpleProducer<Set<String>> libProducer = new SimpleProducer<Set<String>>() {
//                        @Override
//                        public Set<String> produce() {
//                            Set<String> all = new LinkedHashSet<>();
//                            for (Component menuComponent : menu.getMenuComponents()) {
//                                JCheckBoxMenuItem b = (JCheckBoxMenuItem) menuComponent;
//                                String libName = (String) b.getClientProperty("LibraryName");
//                                if (libName != null && b.isSelected()) {
//                                    all.add(libName);
//                                }
//                            }
//                            return all;
//                        }
//                    };
//                    for (PlotLibrary library : allLibraries) {
//                        if (!visitedLibraries.containsKey(library.getName())) {
//                            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new Plot.PlotLibraryAction(context.getModelProvider(), library.getName(), libProducer));
//                            menuItem.putClientProperty("LibraryName", library.getName());
//                            menu.add(menuItem);
//                        }
//                    }
//                }
//            });
//        }
    }

    private static boolean matches(LibraryPlotType a,LibraryPlotType b){
        if(a.getType()==b.getType()){
            if(a.getLibrary()==null || b.getLibrary()==null || a.getLibrary().equalsIgnoreCase(b.getLibrary())){
                return true;
            }
        }
        return false;
    }
}
