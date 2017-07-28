package net.vpc.scholar.hadrumaths.plot.console;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.util.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * @author : vpc
 * @creationtime 08 10 2006
 * je reviens  � la structure
 * .....###.....
 * .....###.....
 * .....###.....
 * .....###.....
 * .....###.....
 * pour faire mon �tude car la matrice que j'ai est bizarre
 */
public abstract class PlotConsoleProjectTemplate {

    private ConsoleAwareObject modeledStructure;
    private ConsoleAwareObject referenceStructure;
    private String studyName;
    private PlotConsole plotConsole;

    private String defaultStudyName;
    private File folder;

    public PlotConsoleProjectTemplate() {
        this(null);
    }

    public PlotConsoleProjectTemplate(File folder) {
        super();
        this.folder = new File(Maths.Config.getCacheFolder(folder == null ? ("plot") : folder.getPath()));
        setStudyName("Simple Study");
        resetParams0();
    }

    public static void runProject(PlotConsoleProjectTemplate program) {
        try {
            program.start();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void resetParams() {
        resetParams0();
    }

    protected boolean showSelectDialog(PlotChoiceList list) {
        final ArrayList<JCheckBox> autoAll = new ArrayList<JCheckBox>();
        File file = new File(System.getProperty("user.home") + "/.java/vpc/these/PlotConsoleProjectTemplate/" + list.getTemplate().getClass().getSimpleName() + ".xml");
        Properties props = new Properties();
        try {
            props = IOUtils.loadXMLProperties(file);
        } catch (IOException e) {
            //
        }
        Box verticalBox = Box.createVerticalBox();
        for (PlotChoice plotChoice : list) {
            JCheckBox box = new JCheckBox(plotChoice.getTitle());
            box.setName(plotChoice.getTitle());
            plotChoice.setEnabled(!"false".equals(props.get(box.getName())));
            box.setSelected(plotChoice.isEnabled());
//            box.setSelected(!"false".equals(props.getProperty(box.getName())));
            box.putClientProperty("PlotChoice", plotChoice);
            box.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox b = (JCheckBox) e.getSource();
                    PlotChoice plotChoice = (PlotChoice) b.getClientProperty("PlotChoice");
                    plotChoice.setEnabled(b.isSelected());
                }
            });
            verticalBox.add(box);
            autoAll.add(box);
        }
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton all = new JButton("select All");
        all.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : autoAll) {
                    jCheckBox.setSelected(true);
                }
            }
        });
        JButton none = new JButton("select None");
        none.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox jCheckBox : autoAll) {
                    jCheckBox.setSelected(false);
                }
            }
        });
        toolBar.add(all);
        toolBar.add(none);
        JScrollPane jsp = new JScrollPane(verticalBox);
        JPanel p = new JPanel(new BorderLayout());
        p.add(toolBar, BorderLayout.NORTH);
        p.add(jsp, BorderLayout.CENTER);
        if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null, p, "Choose Studies...", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null)) {
            file.getParentFile().mkdirs();
            for (JCheckBox jCheckBox : autoAll) {
                props.put(jCheckBox.getName(), jCheckBox.isSelected() ? "true" : "false");
            }
            try {
                IOUtils.storeXMLProperties(file, props, null);
            } catch (IOException e) {
                //
            }
            return true;
        }
        return false;
    }

    protected void runPlotChoiceList() {
        runPlotChoiceList(new PlotChoiceList(this));
    }

    protected void runPlotChoiceList(PlotChoiceList list) {
        if (!list.iterator().hasNext()) {
            throw new NoSuchElementException("Empty PlotChoiceList");
        } else if (list.size() == 1) {
            for (PlotChoice plotChoice : list) {
                this.setDefaultStudyName(plotChoice.getTitle());
                this.newStudy();
                plotChoice.runStudy(this);
            }
        } else {
            if (showSelectDialog(list)) {
                for (PlotChoice plotChoice : list) {
                    if (plotChoice.isEnabled()) {
                        this.setDefaultStudyName(plotChoice.getTitle());
                        this.newStudy();
                        plotChoice.runStudy(this);
                    }
                }
            }
        }
    }

    protected void resetParams0() {
//
    }

    public void runStudy(ConsoleAxisList list) {
        if (list.size() > 1) {
            if (getPlotConsole().isDisposing()) {
                return;
            }
        }
        getPlotConsole().setWindowTitle(getStudyName());
        ArrayList<ParamSet> p = getParams();
        PlotData plotData = new PlotData(
                getStudyName(), getReferenceStructure(), getModeledStructure(), list,
                p.toArray(new ParamSet[p.size()])
        );
        getPlotConsole().run(plotData);
    }

    public abstract ArrayList<ParamSet> getParams();

    public ConsoleAwareObject getModeledStructure() {
        return modeledStructure;
    }

    public void setModeledStructure(ConsoleAwareObject modele) {
        this.modeledStructure = modele;
    }

    public ConsoleAwareObject getReferenceStructure() {
        return referenceStructure;
    }

    public void setReferenceStructure(ConsoleAwareObject referenceStructure) {
        this.referenceStructure = referenceStructure;
    }


    public void start() {
        resetParams();
        getPlotConsole().start();
        try {
            run();
        } catch (ConsoleDisposingException e) {
            //dispose is called by user
            return;
        }
        getPlotConsole().dispose();
    }

    public void run() {
        runPlotChoiceList();
    }

    protected void newStudy(String title, String prefix, String suffix) {
        newStudy((prefix == null ? "" : prefix) + title + (suffix == null ? "" : suffix));
    }

    protected void newStudy() {
        newStudy(getDefaultStudyName());
    }

    protected void newStudy(String title) {
        getPlotConsole().getLog().trace("newStudy " + title);
        resetParams();
        setStudyName(title);
    }

    public void newPlotConsole() {
        setPlotConsole(null);
    }

    public synchronized PlotConsole getPlotConsole() {
        if (plotConsole == null) {
            plotConsole = new PlotConsole(getClass().getSimpleName(), folder);
            plotConsole.setAutoSavingFilePattern(getClass().getSimpleName() + "-{date}");
        }
        return plotConsole;
    }

    public void setPlotConsole(PlotConsole plotConsole) {
        this.plotConsole = plotConsole;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getDefaultStudyName() {
        return defaultStudyName;
    }

    public void setDefaultStudyName(String defaultStudyName) {
        this.defaultStudyName = defaultStudyName;
    }


}
