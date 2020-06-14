/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.extension;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vpc
 */
public class ClassResolvers<T> {

    private List<ClassResolver<T>> all = new ArrayList<>();

    public void add(ClassResolver<T> a, Class forClass) {
        all.add(a);
    }

    public T resolve(Object o) {
        if (o == null) {
            return null;
        }
        for (ClassResolver<T> r : all) {
            T t = r.resolve(o);
            if (t != null) {
                return t;
            }
        }
        return null;
    }
}
