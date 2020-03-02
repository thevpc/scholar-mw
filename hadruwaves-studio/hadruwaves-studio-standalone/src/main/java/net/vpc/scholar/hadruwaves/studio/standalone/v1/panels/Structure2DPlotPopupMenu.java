package net.vpc.scholar.hadruwaves.studio.standalone.v1.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import net.vpc.scholar.hadruwaves.mom.project.common.Area;


/**
 * Created by IntelliJ IDEA.
 * User: TAHA
 * Date: 10 avr. 2004
 * Time: 23:54:50
 * To change this template use File | Settings | File Templates.
 */
public class Structure2DPlotPopupMenu extends JPopupMenu {
    private MomProject2DPlotEditor schemaPanel;
    JMenuItem configGrid;
    JMenuItem selectAreas;
    JMenuItem pin;
    JMenuItem delete;
    JMenuItem edit;
    JMenuItem gridSplit;
    JMenuItem userSplit;
    JMenuItem addSource;
    JMenuItem addMetal;
    JMenuItem addZs;
    JMenuItem zoomIn;
    JMenuItem zoomOut;

    public Structure2DPlotPopupMenu(MomProject2DPlotEditor theSchemaPanel) {
        this.schemaPanel = theSchemaPanel;

        addSource = new JMenuItem("Ajouter une source");
        addSource.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.addSource();
            }
        });
        add(addSource);

        addMetal = new JMenuItem("Ajouter un metal");
        addMetal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.addMetal();
            }
        });
        add(addMetal);

        addZs = new JMenuItem("Ajouter une Imp\u009Edance de surface");
        addZs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.addImpedanceSurface();
            }
        });
        add(addZs);

        delete = new JMenuItem("Modifier");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.selectedAreasDoOpenEditors();
            }
        });
        add(delete);

        delete = new JMenuItem("Supprimer");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.selectedAreasDoDelete();
            }
        });
        add(delete);

        gridSplit = new JMenuItem("Subdiviser selon la grille");
        gridSplit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.selectedAreasDoSplit();
            }
        });
        add(gridSplit);

        userSplit = new JMenuItem("Subdiviser selon la grille manuellement");
        userSplit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.showSplitSelectedAreas();
            }
        });
        add(userSplit);

        addSeparator();
        pin = new JMenuItem("Aligner sur la grille");
        pin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.selectedAreasEnsurePin();
            }
        });
        add(pin);


        selectAreas = new JMenuItem("Selectionner manuellement...");
        selectAreas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.showSelectDialog();
            }
        });
        add(selectAreas);

        configGrid = new JMenuItem("Configurer la grille");
        configGrid.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.configure();
            }
        });
        add(configGrid);
        addSeparator();
        zoomIn = new JMenuItem("Zoom +");
        zoomIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.zoomIn();
            }
        });
        add(zoomIn);
        zoomOut = new JMenuItem("Zoom -");
        zoomOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                schemaPanel.zoomOut();
            }
        });
        add(zoomOut);
        addSeparator();

//        JMenuItem zoomIn=new JMenuItem("Zoom In...");
//        zoomIn.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
//                double x=schemaPanel.getZoomFactor();
//                if(x<1){
//                    x*=2;
//                }else{
//                    x++;
//                }
//                schemaPanel.setZoomFactor(x);
//            }
//        });
//        add(zoomIn);
//
//        JMenuItem zoomOut=new JMenuItem("Zoom Out...");
//        zoomOut.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
//                double x=schemaPanel.getZoomFactor();
//                if(x>1){
//                    x--;
//                }else{
//                    x=x/2;
//                }
//                schemaPanel.setZoomFactor(x);
//            }
//        });
//        add(zoomOut);
//
//        JMenuItem zoomNone=new JMenuItem("No Zoom");
//        zoomNone.addActionListener(new ActionListener() {
//            public void actionPerformedImpl(ActionEvent e) {
//                schemaPanel.setZoomFactor(1);
//            }
//        });
//        add(zoomNone);
    }

    public void configure(Area[] areas) {
        //
    }

}
