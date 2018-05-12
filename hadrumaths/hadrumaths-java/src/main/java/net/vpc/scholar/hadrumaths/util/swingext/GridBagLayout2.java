package net.vpc.scholar.hadrumaths.util.swingext;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 5 mai 2006 00:52:57
 */
public class GridBagLayout2 extends GridBagLayout {
    private static final long serialVersionUID = 1L;
    private String pattern;
    private boolean nonameAdded = false;
    int nonameIndex = 1;
    int x = 0;
    int y = 0;
    private Map<String, GridBagConstraints> constraintsMap = new HashMap<String, GridBagConstraints>();

    public GridBagLayout2() {
    }

    public GridBagLayout2(String pattern) {
        setPattern(pattern);
    }

    public GridBagLayout2 addLine(String pattern){
        append(pattern+"\n");
        return this;
    }


    public GridBagConstraints getConstraints(String name) {
        GridBagConstraints constraints = constraintsMap.get(name);
        if (constraints == null) {
            throw new NoSuchElementException(name);
        }
        return constraints;
    }

    public String getPattern() {
        return pattern;
    }


    public void reset(){
        x = 0;
        y = 0;
        constraintsMap.clear();
        nonameAdded = false;
        nonameIndex = 1;
        this.pattern=null;
    }

    public GridBagLayout2 setPattern(String pattern) {
        reset();
        append(pattern);
        return this;
    }

    public GridBagLayout2 append(String pattern) {
        this.pattern = (this.pattern==null?"":this.pattern)+(pattern==null?"":pattern);
        int i = 0;
        while (i < pattern.length()) {
            if (pattern.charAt(i) == '[') {
                int j = pattern.indexOf(']', i + 1);
                if (j < 0) {
                    throw new IllegalArgumentException("Expected [");
                }
                String tagAndName = pattern.substring(i + 1, j);
                StringBuilder name = new StringBuilder();
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.gridx = x;
                constraints.gridy = y;
                x++;
                i = j + 1;
                boolean lastConfig = true;
                boolean config = false;
                for (int k = 0; k < tagAndName.length(); k++) {
                    char c = tagAndName.charAt(k);
                    switch (c) {
                        case '-': {
                            config = true;
                            lastConfig = true;
                            constraints.fill = GridBagConstraints.HORIZONTAL;
                            break;
                        }
                        case '|': {
                            config = true;
                            lastConfig = true;
                            constraints.fill = GridBagConstraints.VERTICAL;
                            break;
                        }
                        case '+': {
                            config = true;
                            lastConfig = true;
                            constraints.fill = GridBagConstraints.BOTH;
                            break;
                        }
                        case '*': {
                            config = true;
                            lastConfig = true;
                            constraints.gridheight++;
                            break;
                        }
                        case ':': {
                            config = true;
                            lastConfig = true;
                            constraints.gridwidth ++;
                            x++;
                            break;
                        }
                        case '.': {
                            config = true;
                            lastConfig = true;
                            constraints.gridwidth = GridBagConstraints.REMAINDER;
                            break;
                        }
                        case '<': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.PAGE_START) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                            } else if (constraints.anchor == GridBagConstraints.PAGE_END) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_START;
                            } else {
                                constraints.anchor = GridBagConstraints.LINE_START;
                            }
                            break;
                        }
                        case '>': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.PAGE_START) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                            } else if (constraints.anchor == GridBagConstraints.PAGE_END) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_END;
                            } else {
                                constraints.anchor = GridBagConstraints.LINE_END;
                            }
                            break;
                        }
                        case '_': {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.LINE_START) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_START;
                            } else if (constraints.anchor == GridBagConstraints.LINE_END) {
                                constraints.anchor = GridBagConstraints.LAST_LINE_END;
                            } else {
                                constraints.anchor = GridBagConstraints.PAGE_END;
                            }
                            break;
                        }
                        case '~':
                        case '^': 
                        {
                            config = true;
                            lastConfig = true;
                            if (constraints.anchor == GridBagConstraints.LINE_START) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_START;
                            } else if (constraints.anchor == GridBagConstraints.LINE_END) {
                                constraints.anchor = GridBagConstraints.FIRST_LINE_END;
                            } else {
                                constraints.anchor = GridBagConstraints.PAGE_START;
                            }
                            break;
                        }
                        case '=': {
                            config = true;
                            lastConfig = true;
                            constraints.weightx++;
                            break;
                        }
                        case '$': {
                            config = true;
                            lastConfig = true;
                            constraints.weighty++;
                            break;
                        }
                        case ' ': {
                            //do nothing just allowed for formatting
                            lastConfig = true;
                            break;
                        }
                        default : {
                            if (Character.isLetter(c) || Character.isDigit(c)) {
                                if (name.length() > 0 && config && lastConfig) {
                                    throw new IllegalArgumentException("name is already specified : got '" + c + "' , name is '" + name.toString() + "'");
                                }
                                name.append(c);
                            } else {
                                throw new IllegalArgumentException("Unexpected token " + c);
                            }
                            lastConfig = false;
                            break;
                        }
                    }
                }
                String key = name.toString();
                if (name.length() == 0) {
                    name.append("$" + nonameIndex);
                    key= name.toString();
                    nonameIndex++;
                }
                GridBagConstraints oldConstraints = constraintsMap.get(key);
                if (oldConstraints != null) {
                    if (config) {
                        throw new IllegalArgumentException("you cannot override constraints for " + name);
                    } else {
                        oldConstraints.gridwidth = constraints.gridx - oldConstraints.gridx + 1;
                        oldConstraints.gridheight = constraints.gridy - oldConstraints.gridy + 1;
                    }
                } else {
                    constraintsMap.put(key, constraints);
                }
            } else if (pattern.charAt(i) == '\n') {
                y++;
                x = 0;
                i++;
            } else if (pattern.charAt(i) == ':' || pattern.charAt(i) == ' ') {
                i++;
            } else {
                throw new IllegalArgumentException("Expected [");
            }
        }
        return this;
    }

    public GridBagLayout2 setInsets(String regexp, Insets insets) {
        Pattern p = Pattern.compile(regexp);
        for (Map.Entry<String, GridBagConstraints> entry : constraintsMap.entrySet()) {
            Matcher m = p.matcher(entry.getKey());
            if (m.matches()) {
                entry.getValue().insets = insets;
            }
        }
        return this;
    }

    public GridBagLayout2 setIpadx(String regexp, int ipadx) {
        Pattern p = Pattern.compile(regexp);
        for (Map.Entry<String, GridBagConstraints> entry : constraintsMap.entrySet()) {
            Matcher m = p.matcher(entry.getKey());
            if (m.matches()) {
                entry.getValue().ipadx = ipadx;
            }
        }
        return this;
    }

    public GridBagLayout2 setIpady(String regexp, int ipady) {
        Pattern p = Pattern.compile(regexp);
        for (Map.Entry<String, GridBagConstraints> entry : constraintsMap.entrySet()) {
            Matcher m = p.matcher(entry.getKey());
            if (m.matches()) {
                entry.getValue().ipady = ipady;
            }
        }
        return this;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof String) {
            super.setConstraints(comp, getConstraints((String) constraints));
        } else {
            super.setConstraints(comp, (GridBagConstraints) constraints);
        }
    }

    public void compileWhite(Container comp){
        if (!nonameAdded) {
            nonameAdded = true;
            for (int i = 1; i < nonameIndex; i++) {
                JLabel empty = new JLabel("");
                if(Boolean.getBoolean("debug:GridBagLayout2")){
                    empty.setBorder(BorderFactory.createEtchedBorder());
                }
                comp.add(empty, "$" + i);
            }
        }
    }
}
