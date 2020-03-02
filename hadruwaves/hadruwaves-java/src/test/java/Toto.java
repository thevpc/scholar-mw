//package test;
//
//import javax.swing.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
//import java.awt.*;
//
///**
// * Created by IntelliJ IDEA.
// * User: EZZET
// * Date: 24 mars 2007
// * Time: 11:49:38
// * To change this template use File | Settings | File Templates.
// */
//public class Toto {
//    public static void main(String[] args) {
//        JFrame f=new JFrame();
//        final JButton b=new JButton("Yezzi");
//
//        b.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Clicked");
//            }
//        });
//        b.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                b.setForeground(
//                        new Color(
//                                (int) (255*Math.random()),
//                                (int) (255*Math.random()),
//                                (int) (255*Math.random())
//                        )
//                );
//            }
//        });
//
//        b.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                b.setBackground(
//                        new Color(
//                                (int) (255*Math.random()),
//                                (int) (255*Math.random()),
//                                (int) (255*Math.random())
//                        )
//                );
//            }
//        });
//
//        f.add(b);
//        f.pack();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);
//    }
//}
