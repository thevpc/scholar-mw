/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.util;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

//OUR MAIN CLASS
public class ButtonClumn extends JFrame {

  public ButtonClumn(){
  //FORM TITLE
  super("Button Column Example");

  //DATA FOR OUR TABLE
  Object[][] data=
    {
      {"1","Man Utd",new Integer(2013),"21"},
      {"2","Man City",new Integer(2014),"3"},
      {"3","Chelsea",new Integer(2015),"7"},
      {"4","Arsenal",new Integer(1999),"10"},
      {"5","Liverpool",new Integer(1990),"19"},
      {"6","Everton",new Integer(1974),"1"},
    };

  //COLUMN HEADERS
  String columnHeaders[]={"Position","Team","Last Year Won","Trophies"};
  //CREATE OUR TABLE AND SET HEADER
  JTable table=new JTable(data,columnHeaders);

//  //SET CUSTOM RENDERER TO TEAMS COLUMN
//  table.getColumnModel().getColumn(1).setCellRenderer(new ButtonRenderer());;

  //SET CUSTOM EDITOR TO TEAMS COLUMN
//  table.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor(new JTextField()));
  table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor2(new JTextField(),new JButton("...")));

  //SCROLLPANE,SET SZE,SET CLOSE OPERATION
  JScrollPane pane=new JScrollPane(table);
  getContentPane().add(pane);
  setSize(450,100);

  setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    ButtonClumn bc=new ButtonClumn();
    bc.setVisible(true);
  }

}

//BUTTON RENDERER CLASS
class ButtonRenderer extends JButton implements  TableCellRenderer
{

  //CONSTRUCTOR
  public ButtonRenderer() {
    //SET BUTTON PROPERTIES
    setOpaque(true);
  }
  @Override
  public Component getTableCellRendererComponent(JTable table, Object obj,
      boolean selected, boolean focused, int row, int col) {

    //SET PASSED OBJECT AS BUTTON TEXT
      setText((obj==null) ? "":obj.toString());

    return this;
  }

}

//BUTTON EDITOR CLASS
class ButtonEditor extends DefaultCellEditor
{
  protected JButton btn;
   private String lbl;
   private Boolean clicked;

   public ButtonEditor(JTextField txt) {
    super(txt);

    btn=new JButton();
    btn.setOpaque(true);

    //WHEN BUTTON IS CLICKED
    btn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {

        fireEditingStopped();
      }
    });
  }

   //OVERRIDE A COUPLE OF METHODS
   @Override
  public Component getTableCellEditorComponent(JTable table, Object obj,
      boolean selected, int row, int col) {

      //SET TEXT TO BUTTON,SET CLICKED TO TRUE,THEN RETURN THE BTN OBJECT
     lbl=(obj==null) ? "":obj.toString();
     btn.setText(lbl);
     clicked=true;
    return btn;
  }

  //IF BUTTON CELL VALUE CHNAGES,IF CLICKED THAT IS
   @Override
  public Object getCellEditorValue() {

     if(clicked)
      {
      //SHOW US SOME MESSAGE
        JOptionPane.showMessageDialog(btn, lbl+" Clicked");
      }
    //SET IT TO FALSE NOW THAT ITS CLICKED
    clicked=false;
    return new String(lbl);
  }

   @Override
  public boolean stopCellEditing() {

         //SET CLICKED TO FALSE FIRST
      clicked=false;
    return super.stopCellEditing();
  }

   @Override
  protected void fireEditingStopped() {
    // TODO Auto-generated method stub
    super.fireEditingStopped();
  }
}
