/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.standalone.editors;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.vpc.scholar.hadruwaves.studio.standalone.MomUIFactory;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;

/**
 *
 * @author vpc
 */
public class SimpleMomUIEditor extends JPanel{

    private ECComboBox typesSelector;
    private CardLayout cardLayout;
    private List<MomUIFactory> values;
    private Hashtable<String,MomUIFactory> mappedValues;
    private JPanel cardPanel;
//    private TMWLabApplication application;
    public SimpleMomUIEditor(MomProjectEditor editor,boolean nullable,List<MomUIFactory> list,String label) {
        super(new BorderLayout());
        this.values=list;
//        this.application=application;
        
        LinkedHashMap titles = new LinkedHashMap();
        cardLayout=new CardLayout();
        cardPanel=new JPanel(cardLayout);
        String defaultSelection=null;
        if(nullable){
            titles.put("", "--NONE--");
            cardPanel.add(new JLabel(),"");
            defaultSelection="";
        }
        mappedValues=new Hashtable<String, MomUIFactory>();
        for (MomUIFactory object : list) {
            MomUIFactory ss = ((MomUIFactory) object).create();
            if(defaultSelection==null){
                defaultSelection=ss.getId();
            }
            mappedValues.put(ss.getId(), ss);
            titles.put(ss.getId(), ss.getId());
            cardPanel.add(ss.getComponent(),ss.getId());
        }

        typesSelector = (ECComboBox) DataTypeEditorFactory.forMap(label, titles, String.class,true);
        typesSelector.getHelper().setDescription(editor.getResources().get(label));
        if(!nullable){
            typesSelector.getHelper().setObject(defaultSelection);
        }
        typesSelector.addPropertyChangeListener(DataTypeEditor.EC_VALUE_PROPERTY,new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String newType=(String)typesSelector.getHelper().getObject();
                cardLayout.show(cardPanel, newType);
                typeChanged(mappedValues.get(newType));
            }
        });
        //types.setObject("");

        ECGroupPanel ecGroupPanel = new ECGroupPanel();
        ecGroupPanel.add(new DataTypeEditor[]{typesSelector}, 1).setBorder(BorderFactory.createTitledBorder(editor.getResources().get("Params")));
        this.add(ecGroupPanel,BorderLayout.PAGE_START);
        this.add(cardPanel,BorderLayout.CENTER);
    }

    public void setSelectedType(String newType) {
        if(newType==null){
            newType="";
        }
        typesSelector.getHelper().setObject(newType);
    }
    
    public String getSelectedType() {
        String vv=(String)getTypesSelector().getHelper().getObject();
        if(vv==null || vv.length()==0){
            return null;
        }
        return vv;
    }
    
    public MomUIFactory getSelectedMomUIFactory() {
        String vv=getSelectedType();
        if(vv==null){
            return null;
        }
        return mappedValues.get(vv);
    }
    
    public ECComboBox getTypesSelector() {
        return typesSelector;
    }
    
    protected void typeChanged(MomUIFactory value){
        
    }
    
    
}
