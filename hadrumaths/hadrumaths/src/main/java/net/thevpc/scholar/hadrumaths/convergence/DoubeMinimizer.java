/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths.convergence;

/**
 *
 * @author vpc
 */
public class DoubeMinimizer<T> {

    private boolean empty = true;
    private double value;
    private T userValue;

    public double getValue() {
        return value;
    }

    public T getUserValue() {
        return userValue;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean accept(double newValue, T userValue) {
        if (accept(newValue)) {
            this.userValue = userValue;
            return true;
        }
        return false;
    }

    public boolean accept(double newValue) {
        if (empty) {
            empty = false;
            value = newValue;
            return true;
        } else {
            if (Double.isNaN(value)) {
                value = newValue;
                return true;
            } else if (!Double.isNaN(newValue) && newValue < value) {
                value = newValue;
                return true;
            }
        }
        return false;
    }
}
