package net.vpc.scholar.hadruplot.util;

import net.vpc.common.strings.StringUtils;
import net.vpc.scholar.hadruplot.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class DefaultValuesPlotModelPopupFactory implements PlotModelPopupFactory {
    @Override
    public void preparePopup(final PlotModelPopupFactoryContext context) {
        final ValuesPlotModel model = (ValuesPlotModel) context.getModel();
        final PlotType t = model.getPlotType();

        if (context.getViewMenu() != null) {
            ButtonGroup group = new ButtonGroup();
            for (PlotType plotType : PlotType.values()) {
                if (!plotType.equals(PlotType.ALL) && !plotType.equals(PlotType.AUTO)) {
                    JCheckBoxMenuItem f = new JCheckBoxMenuItem(new Plot.PlotTypeAction(context.getModelProvider(), StringUtils.toCapitalized(plotType.name()), plotType));
                    f.setEnabled(PlotBackendLibraries.isSupported(plotType));
                    PlotSwingUtils.setOnRefreshComponent(f, new OnRefreshComponent() {
                        @Override
                        public void onRefreshComponent(Component component) {
                            JCheckBoxMenuItem f = (JCheckBoxMenuItem) component;
                            Plot.PlotTypeAction a = (Plot.PlotTypeAction) f.getAction();
                            PlotType pt = a.getPlotType();
                            f.setEnabled(PlotBackendLibraries.isSupported(pt));
                        }
                    });
                    f.setSelected(t == plotType);
                    group.add(f);
                    context.getViewMenu().add(f);
                }
            }
        }
        if (context.getLibrariesMenu() != null) {
            PlotSwingUtils.setOnRefreshComponent(context.getLibrariesMenu(), new OnRefreshComponent() {
                @Override
                public void onRefreshComponent(Component component) {
                    final JMenu menu=(JMenu) component;
                    PlotLibrary[] allLibraries = PlotBackendLibraries.getLibraries();
                    final Set<String> availableLibs=new LinkedHashSet<>();
                    for (PlotLibrary allLibrary : allLibraries) {
                        availableLibs.add(allLibrary.getName());
                    }
                    final Map<String,JCheckBoxMenuItem> visitedLibraries=new HashMap<>();
                    ButtonGroup group = null;
                    for (Component menuComponent : menu.getComponents()) {
                        JCheckBoxMenuItem b=(JCheckBoxMenuItem)menuComponent;
                        String libName=(String)b.getClientProperty("LibraryName");
                        if(group==null) {
                            group = (ButtonGroup) b.getClientProperty("ButtonGroup");
                        }
                        if(!availableLibs.contains(libName)){
                            b.setEnabled(false);
                        }
                        visitedLibraries.put(libName,b);
                    }
                    SimpleProducer<Set<String>> libProducer = new SimpleProducer<Set<String>>() {
                        @Override
                        public Set<String> produce() {
                            Set<String> all = new LinkedHashSet<>();
                            for (Component menuComponent : menu.getComponents()) {
                                JCheckBoxMenuItem b = (JCheckBoxMenuItem) menuComponent;
                                String libName = (String) b.getClientProperty("LibraryName");
                                if (libName != null) {
                                    all.add(libName);
                                }
                            }
                            return all;
                        }
                    };
                    for (PlotLibrary library : allLibraries) {
                        if(!visitedLibraries.containsKey(library.getName())){
                            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(new Plot.PlotLibraryAction(context.getModelProvider(), library.getName(), libProducer));
                            menuItem.putClientProperty("LibraryName",library.getName());
                            menu.add(menuItem);
                        }
                    }
                }
            });
        }
    }
}
