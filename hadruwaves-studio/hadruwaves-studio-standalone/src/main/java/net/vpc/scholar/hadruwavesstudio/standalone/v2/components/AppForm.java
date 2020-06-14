/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author vpc
 */
public class AppForm extends JPanel {

    private boolean built = false;
    private int idCounter = 0;
    private AppFormBuilder builder;
    private Map<String, AppFormItem> items = new LinkedHashMap<>();

    public AppForm(AppFormBuilder builder) {
        super(new GridBagLayout());
        this.builder = builder;
    }

    public AppFormItem addTitle(String label) {
        return add(new AppFormTitle(genId(), label));
    }

    public AppFormItem addSeparator() {
        return add(new AppFormTitle(genId(), null));
    }

    protected String genId() {
        return "sep-" + (++idCounter);
    }

    public AppFormField addField(String id, String label, Class cls) {
        return (AppFormField)add(builder.create(id, label, cls));
    }

    public AppFormField addField(String id, String label, JComponent component, Class cls) {
        return (AppFormField)add(builder.create(id, label, component, cls));
    }

    public AppFormItem add(AppFormItem item) {
        items.put(item.getId(), item);
        return item;
    }

    public Integer getInt(String name) {
        return (Integer) getField(name).getValue();
    }

    public String getString(String name) {
        return (String) getField(name).getValue();
    }

    public Double getDouble(String name) {
        return (Double) getField(name).getValue();
    }

    public <T> T getValue(String name) {
        return (T) getField(name).getValue();
    }

    public AppFormField getField(String name) {
        return (AppFormField) items.get(name);
    }

    public AppFormItem get(String name) {
        return items.get(name);
    }

    public void build() {
        if (built) {
            throw new IllegalArgumentException("Already built");
        }
        built = true;
        FormBuildHelper bh = new FormBuildHelper();
        for (AppFormItem value : items.values()) {
            bh.skipSet();
            value.build(bh);
        }
    }

    public class FormBuildHelper {

        int gridx = 0;
        int gridy = 0;
        List<List<Boolean>> rowsFilled = new ArrayList<>();

        public boolean isSet(int x, int y) {
            update(x, y);
            return (Boolean) rowsFilled.get(y).get(x);
        }

        public void update(int x, int y) {
            while (rowsFilled.size() < y + 1) {
                rowsFilled.add(new ArrayList<>());
            }
            for (List<Boolean> list : rowsFilled) {
                while (list.size() < x + 1) {
                    list.add(false);
                }
            }
        }

        public void span(int x, int y) {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    set(gridx + i, gridy + j);
                }
            }
        }

        public void set(int x, int y) {
            update(x, y);
            rowsFilled.get(y).set(x, true);
        }

        public void skipSet() {
            while (isSet(gridx, gridy)) {
                gridx++;
            }
        }

        public int gridx() {
            return gridx;
        }

        public int gridy() {
            return gridy;
        }

        public void newLine() {
            gridy++;
            gridx = 0;
        }

        public void addComponent(JComponent component, GridBagConstraints constraints) {
            AppForm.this.add(component, constraints);
        }
    }
}
