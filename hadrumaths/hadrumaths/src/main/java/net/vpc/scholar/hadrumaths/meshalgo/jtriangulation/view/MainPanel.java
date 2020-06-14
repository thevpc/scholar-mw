//package net.vpc.scholar.math.meshalgo.jtriangulation.view;
//
//import javax.swing.*;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
//
//
///**
// * Created by IntelliJ IDEA.
// * User: EZZET
// * Date: 3 avr. 2007
// * Time: 12:03:07
// * To change this template use File | Settings | File Templates.
// */
//public class MainPanel extends JPanel {
//    MeshComponent mesh;
//    int choix,valid,choixoption;
//    double nbr1,nbr2,nbr11,nbr21;
//
//    public static void main(String[] args) {
//        JFrame f=new JFrame();
//        f.add(new MainPanel());
//        f.pack();
//        //f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        ImageIcon image = new ImageIcon("sans.gif","PowerTriangulation");
//        f.setIconImage(image.getImage());
//        f.setTitle("PowerTriangulation");
//        f.setVisible(true);
//
//
//    }
//    public MainPanel() {
//        mesh=new MeshComponent();
//        valid=0;
//        choixoption=0;
//
//        setLayout(new BorderLayout());
//        JPanel nord=new JPanel();
//        nord.setLayout(new BorderLayout());
//        nord.add(createMenu(),BorderLayout.PAGE_START);
//        nord.add(createToolbar(),BorderLayout.PAGE_END);
//        add(nord,BorderLayout.PAGE_START);
//
//        add(createOptions(),BorderLayout.LINE_END);
//        JPanel center=new JPanel();
//        center.setBorder(BorderFactory.createEtchedBorder());
//        center.add(createCenter());
//        add(center,BorderLayout.CENTER);
//        add(createLog(),BorderLayout.PAGE_END);
//        center.setBackground(Color.white);
//
//
//    }
//
//    public JComponent createCenter(){
//        return mesh;
//    }
//    public JComponent createMenu(){
//        JMenuBar menu=new JMenuBar();
//        JMenu file=new JMenu("File");
//        JMenuItem exit=new JMenuItem("Exit");
//        JMenuItem newFile=new JMenuItem("new");
//        JMenu insert=new JMenu("Insert");
//        JMenu edition=new JMenu("Edition");
//        JMenuItem effacer=new JMenuItem("Effacer");
//        JMenuItem basepolygone=new JMenuItem("Polygone de base");
//        JMenuItem pricisepolygone=new JMenuItem("Zone plus maill�es");
//        exit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                int ret = JOptionPane.showConfirmDialog(null, "Etes vous sur de vouloir quitter?", "Attention", JOptionPane.YES_NO_CANCEL_OPTION);
//                if(ret==JOptionPane.OK_OPTION){
//                    System.exit(0);
//                }
//            }
//        });
//
//        file.add(exit);
//        file.add(newFile);
//        menu.add(file);
//
//        basepolygone.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                mesh.insertZoneBase();
//            }
//        });
//               pricisepolygone.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                mesh.insertZonePlusMailles();
//            }
//        });
//        effacer.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                mesh.delete();
//                valid=0;
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        JMenu help=new JMenu("?");
//        JMenuItem about=new JMenuItem("About");
//
//        edition.add(effacer);
//        menu.add(edition);
//        insert.add(basepolygone);
//        insert.add(pricisepolygone);
//        menu.add(insert);
//        help.add(about);
//        menu.add(help);
//
//
//        return menu;
//    }
//
//    public JComponent createLog(){
//        JTextArea a=new JTextArea();
//        a.setEnabled(false);
//        a.setText("Introduisez vos surfaces de base ainsi que les les zones les plus maill�es puis triangulez  ");
//        a.setDisabledTextColor(Color.BLACK);
//        JScrollPane p=new JScrollPane(a);
//        return p;
//    }
//    private JComponent createToolbar(){
//        JToolBar t=new JToolBar();
//        JProgressBar progressBar;
//        t.setBackground(Color.getHSBColor(120,92,112));
//        t.setBorder(BorderFactory.createLoweredBevelBorder());
//        final JButton button1=new JButton("Trianguler");
//        t.add(button1);
//
//       button1.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("nbr1test="+nbr1);
//                System.out.println("nbr2test="+nbr2);
//           for(int i=0;i<mesh.getMeshComponentModel().getBasePolygons().get(0).xpoints.length;i++){
//            if((mesh.getMeshComponentModel().getBasePolygons().get(0).xpoints[i]!=0)||(mesh.getMeshComponentModel().getBasePolygons().get(0).ypoints[i]!=0)){
//                valid=valid+1;
//            }
//        }
//
//               if(valid<3){
//                    int ret = JOptionPane.showConfirmDialog(null, "Introduisez vos polygones de base", "Attention", JOptionPane.WARNING_MESSAGE);
//                }else{
//                if(choix==1&(choixoption!=0)){
//                mesh.triangulter1();
//                }else{
//                    if(choix==2&(choixoption!=0)){
//                 mesh.triangulter();
//                    }else{
//                        if(choix==0&choixoption==0){
//                        int ret = JOptionPane.showConfirmDialog(null, "Choisissez un algorithme de triangulation", "Attention", JOptionPane.WARNING_MESSAGE);
//                        }
//                        }
//                      }
////                        if(choix!=2||choix!=1){
////                        int ret = JOptionPane.showConfirmDialog(null, "Choisissez un algorithme de triangulation", "Attention", JOptionPane.WARNING_MESSAGE);
////                        }
//                    if((choixoption==0&choix==2)||(choixoption==0&choix==1)){
//                    int ret = JOptionPane.showConfirmDialog(null, "Vous devrez valider le choix des options", "Attention", JOptionPane.WARNING_MESSAGE);
//                }
//
//
//
//               }
//            }
//        });
//
//
//        return t;
//    }
//
//    public JComponent createJTabbedPane(){
//        JTabbedPane tabbedpane = new JTabbedPane();
//        JPanel optionsPanel1=new JPanel();
//
//        optionsPanel1.setLayout(new GridBagLayout());
//        GridBagConstraints c=new GridBagConstraints();
//        JPanel optionsPanel2=new JPanel();
//        optionsPanel2.setLayout(new GridBagLayout());
//        optionsPanel1.setBackground(Color.WHITE);
//        optionsPanel2.setBackground(Color.WHITE);
//
//
//        JLabel option1Label=new JLabel("Aire maximale:");
//        JLabel option13Label=new JLabel("Zones plus maill�es:");
//        final JSpinner option=new JSpinner(new SpinnerNumberModel(1000,100,10000,1));
//        final JSpinner optionZ=new JSpinner(new SpinnerNumberModel(100,50,10000,1));
//        final Checkbox optionChoix1=new Checkbox("Valider ce choix");
//
//
//
//        JLabel option3Label=new JLabel("Finesse des zones:");
//        JLabel option2Label=new JLabel("Nombre de triangles:");
//        JLabel Labelchoix2=new JLabel("Valider ce choix");
//        final JSpinner option11=new JSpinner(new SpinnerNumberModel(100,50,100,1));
//        final JSpinner option12=new JSpinner(new SpinnerNumberModel(10,10,1000000,1));
//        final Checkbox optionChoix2=new Checkbox("Valider ce choix");
//                mesh.setNconsdes(((Number)option.getValue()).doubleValue());
//                mesh.setMconsdes(((Number)optionZ.getValue()).doubleValue());
//
//
//
//
//        option.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setNconsdes(((Number)option.getValue()).doubleValue());
//                nbr1=(((Number)option.getValue()).doubleValue());
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//        optionZ.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setMconsdes(((Number)optionZ.getValue()).doubleValue());
//                nbr2=(((Number)optionZ.getValue()).doubleValue());
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//
//        option11.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setMconsdes(((Number)option11.getValue()).doubleValue());
//                nbr11=(((Number)option11.getValue()).doubleValue());
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//        option12.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setNconsdes(((Number)option12.getValue()).doubleValue());
//                nbr21=(((Number)option12.getValue()).doubleValue());
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//                optionChoix1.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                      if(optionChoix1.getState()){
//                nbr1=(((Number)option.getValue()).doubleValue());
//                nbr2=(((Number)optionZ.getValue()).doubleValue());
//                          mesh.setNconsdes(nbr1);
//                          mesh.setMconsdes(nbr2);
//                          choixoption=1;
//                          mesh.setTypeoption(0);
//        }
//
//                      if(!optionChoix1.getState()){
//                          choixoption=0;
//                      }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        optionChoix2.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                      if(optionChoix2.getState()){
//                nbr11=(((Number)option11.getValue()).doubleValue());
//                nbr21=(((Number)option12.getValue()).doubleValue());
//                              choixoption=1;
//                              mesh.setNconsdes(nbr21);
//                              mesh.setMconsdes(nbr11);
//                          mesh.setTypeoption(1);
//
//                      }
//                if(!optionChoix2.getState()){
//                          choixoption=0;
//                      }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//
//
//        optionChoix2.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if(optionChoix2.getState()){
//                optionChoix1.setEnabled(false);
//                option.setEnabled(false);
//                optionZ.setEnabled(false);
//                optionChoix1.setState(false);
//                }else{
//                optionChoix1.setEnabled(true);
//                option.setEnabled(true);
//                optionZ.setEnabled(true);
//                }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        optionChoix1.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if(optionChoix1.getState()){
//                optionChoix2.setEnabled(false);
//                option11.setEnabled(false);
//                option12.setEnabled(false);
//                optionChoix2.setState(false);
//                }else{
//                optionChoix2.setEnabled(true);
//                option11.setEnabled(true);
//                option12.setEnabled(true);
//                }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        c.gridx=0;
//        c.gridy=0;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(option1Label,c);
//
//        c.gridx=1;
//        c.gridy=0;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(option,c);
//
//        c.gridx=0;
//        c.gridy=1;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(option13Label,c);
//
//        c.gridx=1;
//        c.gridy=1;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(optionZ,c);
//
//        c.gridx=0;
//        c.gridy=2;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(optionChoix1,c);
//
//
//        //***************************************************
//
//        c.gridx=0;
//        c.gridy=0;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option3Label,c);
//
//        c.gridx=1;
//        c.gridy=0;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option11,c);
//
//        c.gridx=0;
//        c.gridy=1;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option2Label,c);
//
//        c.gridx=1;
//        c.gridy=1;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option12,c);
//
//        c.gridx=0;
//        c.gridy=2;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(optionChoix2,c);
//
//
//
//        tabbedpane.add("Aire du triangle",optionsPanel1);
//        tabbedpane.add("Nombre de triangle",optionsPanel2);
//        return tabbedpane;
//    }
//
//    public JComponent createJTabbedPane1(){
//        JTabbedPane tabbedpane = new JTabbedPane();
//        JPanel optionsPanel1=new JPanel();
//        optionsPanel1.setLayout(new GridBagLayout());
//        GridBagConstraints c=new GridBagConstraints();
//        JPanel optionsPanel2=new JPanel();
//        optionsPanel2.setLayout(new GridBagLayout());
//        optionsPanel1.setBackground(Color.white);
//        optionsPanel2.setBackground(Color.white);
//
//        JLabel option1Label=new JLabel("Aire maximale:");
//        JLabel option13Label=new JLabel("Zones plus maill�es:");
//        final JSpinner option=new JSpinner(new SpinnerNumberModel(1000,10,10000,1));
//        final JSpinner optionZ=new JSpinner(new SpinnerNumberModel(100,50,10000,1));
//        final Checkbox optionChoix1=new Checkbox("Valider ce choix");
//
//
//
//        JLabel option3Label=new JLabel("Finesse des zones:");
//        JLabel option2Label=new JLabel("Max Longueur");
//        JLabel Labelchoix2=new JLabel("Valider ce choix");
//        final JSpinner option11=new JSpinner(new SpinnerNumberModel(1000,10,10000,1));
//        final JSpinner option12=new JSpinner(new SpinnerNumberModel(300,300,10000,1));
//        final Checkbox optionChoix2=new Checkbox("Valider ce choix");
//
//                mesh.setNflip(((Number)option.getValue()).doubleValue());
//                mesh.setMflip(((Number)optionZ.getValue()).doubleValue());
//
//
//
//
//        option.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setNflip(((Number)option.getValue()).doubleValue());
//                nbr1=(((Number)option.getValue()).doubleValue());
//                System.out.println("nbr1test="+nbr1);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//        optionZ.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setMflip(((Number)optionZ.getValue()).doubleValue());
//                nbr2=(((Number)optionZ.getValue()).doubleValue());
//                System.out.println("nbr2test="+nbr2);
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//
//        option11.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setMflip(((Number)option11.getValue()).doubleValue());
//                nbr11=(((Number)option11.getValue()).doubleValue());
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//        option12.addChangeListener(new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                mesh.setNflip(((Number)option12.getValue()).doubleValue());
//                nbr21=(((Number)option12.getValue()).doubleValue());
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//                optionChoix1.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                      if(optionChoix1.getState()){
//               nbr1=(((Number)option.getValue()).doubleValue());
//                nbr2=(((Number)optionZ.getValue()).doubleValue());
//                          mesh.setNflip(nbr1);
//                          mesh.setMflip(nbr2);
//                          choixoption=1;
//                          mesh.setTypeoption(0);
//        }
//
//                      if(!optionChoix1.getState()){
//                          choixoption=0;
//                      }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        optionChoix2.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                      if(optionChoix2.getState()){
//                              choixoption=1;
//                nbr11=(((Number)option11.getValue()).doubleValue());
//                nbr21=(((Number)option12.getValue()).doubleValue());
//                              mesh.setNflip(nbr21);
//                              mesh.setMflip(nbr11);
//                          mesh.setTypeoption(1);
//
//                      }
//                if(!optionChoix2.getState()){
//                          choixoption=0;
//                      }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//
//
//        optionChoix2.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if(optionChoix2.getState()){
//                optionChoix1.setEnabled(false);
//                option.setEnabled(false);
//                optionZ.setEnabled(false);
//                optionChoix1.setState(false);
//                }else{
//                optionChoix1.setEnabled(true);
//                option.setEnabled(true);
//                optionZ.setEnabled(true);
//                }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        optionChoix1.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if(optionChoix1.getState()){
//                optionChoix2.setEnabled(false);
//                option11.setEnabled(false);
//                option12.setEnabled(false);
//                optionChoix2.setState(false);
//                }else{
//                optionChoix2.setEnabled(true);
//                option11.setEnabled(true);
//                option12.setEnabled(true);
//                }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });
//
//        c.gridx=0;
//        c.gridy=0;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(option1Label,c);
//
//        c.gridx=1;
//        c.gridy=0;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(option,c);
//
//        c.gridx=0;
//        c.gridy=1;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(option13Label,c);
//
//        c.gridx=1;
//        c.gridy=1;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(optionZ,c);
//
//        c.gridx=0;
//        c.gridy=2;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel1.add(optionChoix1,c);
//
//
//        //***************************************************
//
//        c.gridx=0;
//        c.gridy=0;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option3Label,c);
//
//        c.gridx=1;
//        c.gridy=0;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option11,c);
//
//        c.gridx=0;
//        c.gridy=1;
//        c.fill=GridBagConstraints.none;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option2Label,c);
//
//        c.gridx=1;
//        c.gridy=1;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(option12,c);
//
//        c.gridx=0;
//        c.gridy=2;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel2.add(optionChoix2,c);
//
//
//
//        tabbedpane.add("Aire du triangle",optionsPanel1);
//        tabbedpane.add("Longueur cot�",optionsPanel2);
//        return tabbedpane;
//    }
//
//    public JComponent createOptions(){
//        JPanel optionsPanel=new JPanel();
//        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
//        GridBagLayout bg=new GridBagLayout();
//        optionsPanel.setLayout(bg);
//        JLabel option1LabeAlgo=new JLabel("Options de triangulation");
//        option1LabeAlgo.setFont(getFont().deriveFont(Font.ITALIC|Font.BOLD,24f));
//        option1LabeAlgo.setBorder(BorderFactory.createRaisedBevelBorder());
//        option1LabeAlgo.setForeground(Color.blue);
//        optionsPanel.setBackground(Color.getHSBColor(155,189,230));
//        JLabel option1Label=new JLabel("Algorithme de triangulation:");
//        JLabel vide=new JLabel("");
//        vide.setVisible(true);
//        JLabel vide1=new JLabel("");
//        vide1.setVisible(true);
//        final JLabel option5Label=new JLabel("Chois du type de Precision:");
//        final JComboBox option2=new JComboBox(new Object[]{"","Construction-Destruction","Flip"});
//        final JComponent chose1=createJTabbedPane();
//        final JComponent chose2=createJTabbedPane1();
//
//        chose1.setVisible(false);
//        chose2.setVisible(false);
//        option5Label.setVisible(false);
//
//        GridBagConstraints c=new GridBagConstraints();
//
//        c.weighty=1;
//        c.gridx=0;
//        c.gridy=0;
//        c.fill=GridBagConstraints.VERTICAL;
//        c.anchor=GridBagConstraints.NORTH;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(option1LabeAlgo);
//
//        c.weighty=0;
//        c.gridx=0;
//        c.gridy=1;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.CENTER;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(new Label(""),c);
//
//        c.weighty=0;
//        c.gridx=0;
//        c.gridy=2;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.CENTER;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(option1Label,c);
//
//        c.weighty=0;
//        c.gridx=0;
//        c.gridy=3;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(option2,c);
//
//        c.weighty=0;
//        c.gridx=0;
//        c.gridy=4;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.NORTH;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(vide,c);
//
//        c.weighty=0;
//        c.gridx=0;
//        c.gridy=5;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.NORTH;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(vide1,c);
//
//        c.weighty=0;
//        c.gridx=0;
//        c.gridy=6;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.NORTH;
//        c.gridwidth=1;
//        c.gridheight=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add(option5Label,c);
//
//        c.gridx=0;
//        c.gridy=7;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=10;
//        c.gridheight=20;
//        c.weightx=2;
//        c.weighty=2;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add( chose1,c);
//        optionsPanel.add( chose2,c);
//
//        c.gridx=0;
//        c.gridy=8;
//        c.fill=GridBagConstraints.BOTH;
//        c.anchor=GridBagConstraints.LINE_START;
//        c.gridwidth=10;
//        c.gridheight=20;
//        c.weightx=1;
//        c.weighty=1;
//        c.insets=new Insets(10,10,10,10);
//        optionsPanel.add( new JLabel(""),c);
//
//        option2.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//            if(option2.getSelectedItem()=="Flip"){
//            choix=1;
//            choixoption=0;
//            option5Label.setVisible(true);
//            chose1.setVisible(false);
//            chose2.setVisible(true);
//        }else{
//            if(option2.getSelectedItem()==""){
//                choix=0;
//                choixoption=0;
//            chose1.setVisible(false);
//            chose2.setVisible(false);
//            option5Label.setVisible(false);
//        }else{
//                choix=2;
//               choixoption=0;
//            option5Label.setVisible(true);
//            chose1.setVisible(true);
//            chose2.setVisible(false);
//            }
//        }
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        } );
//
//
//
////        optionsPanel.setBorder(BorderFactory.createRaisedBevelBorder());
//
//        return optionsPanel;
//    }
//
//}
