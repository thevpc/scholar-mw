package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import net.thevpc.common.swing.button.JDropDownButton;
import net.thevpc.scholar.hadrumaths.CharactersTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * https://www.fileformat.info/info/unicode/category/Sm/list.htm
 */
public class CharactersTableComponent extends JPanel {
    private java.util.List<CharTableListener> listeners = new ArrayList<>();

    public CharactersTableComponent() {
        super(new GridLayout(-1, 1));
    }

    public static java.util.List<JComponent> createComponents(CharTableListener areaCharTableListener, Family... families) {
        EnumSet<Family> a = EnumSet.noneOf(Family.class);
        if (families != null) {
            for (Family family : families) {
                if (family != null) {
                    a.add(family);
                }
            }
        }
        if (a.isEmpty()) {
            a.addAll(Arrays.asList(Family.values()));
        }
        java.util.List<JComponent> toolbar = new ArrayList<>();
        if (a.contains(Family.GREEK_LOWER) || a.contains(Family.GREEK_UPPER)) {
            String title="";
            if(a.contains(Family.GREEK_LOWER) && a.contains(Family.GREEK_UPPER)) {
                title = CharactersTable.alpha+CharactersTable.OMEGA;
            }else if(a.contains(Family.GREEK_LOWER)){
                title = CharactersTable.alpha + CharactersTable.gamma;
            }else{
                title = CharactersTable.ALPHA + CharactersTable.OMEGA;
            }
            JDropDownButton b = new JDropDownButton(title);
            b.setQuickActionDelay(0);
            b.setPaintHandle(false);
            b.setToolTipText("Greek Symbols");
            CharactersTableComponent cht = new CharactersTableComponent();
            if (a.contains(Family.GREEK_LOWER)) {
                cht.addGreekLowerCase();
            }
            if (a.contains(Family.GREEK_UPPER)) {
                cht.addGreekUpperCase();
            }
            cht.addCharTableListener(areaCharTableListener);
            b.add(cht);
            toolbar.add(b);
        }

        if (a.contains(Family.OPERATORS)) {
            JDropDownButton b = new JDropDownButton("\u21D4");
            b.setToolTipText("Simple Operators");
            b.setQuickActionDelay(0);
            b.setPaintHandle(false);
            CharactersTableComponent cht = new CharactersTableComponent();
            cht.addOperators1();
            cht.addCharTableListener(areaCharTableListener);
            b.add(cht);
            toolbar.add(b);
        }

        if (a.contains(Family.SYMBOLS)) {
            JDropDownButton b = new JDropDownButton("\u2207");
            b.setToolTipText("Math Symbols");
            b.setQuickActionDelay(0);
            b.setPaintHandle(false);
            CharactersTableComponent cht = new CharactersTableComponent();
            cht.addOperators2();
            cht.addCharTableListener(areaCharTableListener);
            b.add(cht);
            toolbar.add(b);
        }
        if (a.contains(Family.CONSTANTS)) {
            JDropDownButton b = new JDropDownButton("C");
            b.setToolTipText("Constants");
            b.setQuickActionDelay(0);
            b.setPaintHandle(false);
            CharactersTableComponent cht = new CharactersTableComponent();
            cht.addConstants();
            cht.addCharTableListener(areaCharTableListener);
            b.add(cht);
            toolbar.add(b);
        }
        if (a.contains(Family.TRIGO)) {
            JDropDownButton b = new JDropDownButton("sin");
            b.setToolTipText("Trogonometric Functions");
            b.setQuickActionDelay(0);
            b.setPaintHandle(false);
            CharactersTableComponent cht = new CharactersTableComponent();
            cht.addTrigo();
            cht.addCharTableListener(areaCharTableListener);
            b.add(cht);
            toolbar.add(b);
        }
        return toolbar;
    }

    public static void addSupport(JToolBar toolbar, CharTableListener areaCharTableListener, Family families) {
        for (JComponent component : createComponents(areaCharTableListener,families)) {
            toolbar.add(component);
        }
    }

    public void build() {
        addGreek();
        addOperators1();
        addOperators2();
        addConstants();
        addTrigo();
    }

    public CharToolBar addBar() {
        CharToolBar b = new CharToolBar();
        add(b.comp);
        return b;
    }

    public void addGreek() {
        addGreekLowerCase();
        addGreekUpperCase();
    }

    public void addOperators1() {
        CharactersTableComponent.CharToolBar ops = addBar();
        ops.add(new CharCommand("+", "+", "plus", "+", null));
        ops.add(new CharCommand("-", "-", "minus", "-", null));
        ops.add(new CharCommand("+-", "\u00B1", "plus/minus", "\u00B1", null));
        ops.add(new CharCommand("*", "*", "muliply", "*", null));
        ops.add(new CharCommand("/", "/", "divide", "/", null));
        ops.add(new CharCommand("<", "<", "Less Than", "<", null));
        ops.add(new CharCommand(">", ">", "Greater than", ">", null));
        ops.add(new CharCommand("=", "=", "Equal To", "=", null));
        ops.add(new CharCommand("NOT EQUAL TO", "\u2260", "Not Equal To", "\u2260", null));
        ops.add(new CharCommand("LTE", "\u2264", "Less than or Equal", "<=", null));
        ops.add(new CharCommand("GTE", "\u2265", "Greater than or Equal", "<=", null));
        ops.add(new CharCommand("<-", "\u2190", "\u2190", "\u2190", null));
        ops.add(new CharCommand("->", "\u2192", "\u2192", "\u2192", null));
        ops.add(new CharCommand("<-", "\u2190", "\u2190", "\u2190", null));
        ops.add(new CharCommand("|^", "\u2191", "\u2191", "\u2191", null));
        ops.add(new CharCommand("^|", "\u2193", "\u2193", "\u2193", null));
        ops.add(new CharCommand("||^", "\u2194", "\u2194", "\u2194", null));
        ops.add(new CharCommand("=>", "\u21D2", "\u21D2", "\u21D2", null));
        ops.add(new CharCommand("<=>", "\u21D4", "\u21D4", "\u21D4", null));
        ops.add(new CharCommand("ELEMENT OF", "\u2208", "Element Of", "\u2208", null));
        ops.add(new CharCommand("NOT AN ELEMENT OF", "\u2209", "Not Element Of", "\u2209", null));
        ops.add(new CharCommand("SMALL ELEMENT OF", "\u220A", "Small Element Of", "\u220A", null));
        ops.add(new CharCommand("CONTAINS AS MEMBER", "\u220B", "Contains as Member", "\u220B", null));
    }

    public void addOperators2() {
        CharactersTableComponent.CharToolBar other = addBar();
        other.add(new CharCommand("NABLA", "\u2207", "Nabla", "\u2207", null));
        other.add(new CharCommand("N-ARY PRODUCT", "\u220F", "N-ary Product", "\u220F", null));
        other.add(new CharCommand("N-ARY COPRODUCT", "\u2210", "N-ary Co-Product", "\u2210", null));
        other.add(new CharCommand("N-ARY SUMMATION", "\u2211", "N-ary Summation", "\u2211", null));
        other.add(new CharCommand("PARTIAL DIFFERENTIAL\t", "\u2202", "Partial Differential", "\u2202", null));
        other.add(new CharCommand("INTEGRAL", "\u222B", "Integral", "\u222B", null));
        other.add(new CharCommand("DOUBLE INTEGRAL", "\u222C", "Double Integral", "\u222C", null));
        other.add(new CharCommand("TRIPLE INTEGRAL", "\u222D", "Triple Integral", "\u222D", null));
        other.add(new CharCommand("CONTOUR INTEGRAL", "\u222E", "Contour Integral", "\u222E", null));
        other.add(new CharCommand("SURFACE INTEGRAL", "\u222F", "Surface Integral", "\u222F", null));
        other.add(new CharCommand("VOLUME INTEGRAL", "\u2230", "Volume Integral", "\u2230", null));
        other.add(new CharCommand("SQRT", "\u221A", "Sqrt", "\u221A", null));
        other.add(new CharCommand("SQRT3", "\u221B", "Sqrt3", "\u221B", null));
        other.add(new CharCommand("SQRT4", "\u221C", "Sqrt4", "\u221C", null));
    }

    public void addConstants() {
        CharactersTableComponent.CharToolBar other = addBar();
        other.add(new CharCommand("infinity", "\u221E", "Infinity", "\u221E", null));
        other.add(new CharCommand("pi", CharactersTable.pi, "Pi", CharactersTable.pi, null));
        other.add(new CharCommand("C", "C", "Light Celerity", "C", null));
        other.add(new CharCommand("eps0", CharactersTable.epsilon+"0", "Vacuum Permettivity", CharactersTable.epsilon+"_0", null));
        other.add(new CharCommand("u0", CharactersTable.mu+"0", "Vacuum Permeability", CharactersTable.mu+"_0", null));
        other.add(new CharCommand("z0", "z0", "Vacuum Impedance", "z_0", null));
        other.add(new CharCommand("z0", "Qe", "Elementary Charge", "Q_e", null));
        other.add(new CharCommand("xmin", "xmin", "Boundary x min", "x_min", null));
        other.add(new CharCommand("xmax", "xmax", "Boundary x max", "x_max", null));
        other.add(new CharCommand("ymin", "ymin", "Boundary y min", "y_min", null));
        other.add(new CharCommand("ymax", "ymax", "Boundary y max", "y_max", null));
        other.add(new CharCommand("zmin", "zmin", "Boundary z min", "z_min", null));
        other.add(new CharCommand("zmax", "zmax", "Boundary z max", "z_max", null));
    }

    public void addGreekLowerCase() {
        CharToolBar bar = addBar();
        for (Field declaredField : CharactersTable.class.getDeclaredFields()) {
            String n = declaredField.getName();
            if (n.length() > 1 && Character.isLowerCase(n.charAt(0))) {
                try {
                    String cc = (String) declaredField.get(null);
                    bar.add(new CharCommand(n, cc, n, cc, null));
                } catch (Exception ex) {
                    //
                }
            }
        }
    }

    public void addGreekUpperCase() {
        CharToolBar bar = addBar();
        for (Field declaredField : CharactersTable.class.getDeclaredFields()) {
            String n = declaredField.getName();
            if (n.length() > 1 && Character.isUpperCase(n.charAt(0))) {
                try {
                    String cc = (String) declaredField.get(null);
                    bar.add(new CharCommand(n, cc, n, cc, null));
                } catch (Exception ex) {
                    //
                }
            }
        }
    }

    public void addCharTableListener(CharTableListener listener) {
        listeners.add(listener);
    }

    protected void fireAction(CharCommand command) {
        for (CharTableListener listener : listeners) {
            listener.onChar(new CharEvent(this, command));
        }
    }

    public void addTrigo() {
        CharactersTableComponent.CharToolBar fcts = addBar();
        for (String s : new String[]{
                "sin", "cos", "tan", "cotan",
                "asin", "acos", "atan", "acotan",
                "sinh", "cosh", "tanh", "cotanh",
                "asinh", "acosh", "atanh", "acotanh",
                "sincard"
        }) {
            if (s.contains("|")) {
                String[] ss = s.split("[|]");
                fcts.add(new CharCommand(ss[1], ss[0], ss[1] + " function", ss[1], null));
            } else {
                fcts.add(new CharCommand(s, s, s + " function", s, null));
            }
        }
    }

    public enum Family {
        GREEK_LOWER,
        GREEK_UPPER,
        OPERATORS,
        SYMBOLS,
        TRIGO,
        CONSTANTS,
    }

    public class CharToolBar {
        JToolBar comp = new JToolBar();

        public CharToolBar() {
            comp.setOpaque(false);
            comp.setFloatable(false);
        }

        public void add(CharCommand command) {
            comp.add(createButton(command));
        }

        private JButton createButton(CharCommand command) {
            JButton b = new JButton(command.getName());
            b.setOpaque(false);
            b.setToolTipText(command.getDescription());
            b.putClientProperty("CharCommand", command);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton b = (JButton) e.getSource();
                    CharCommand command = (CharCommand) b.getClientProperty("CharCommand");
                    fireAction(command);
                }
            });
            return b;
        }

    }
}
