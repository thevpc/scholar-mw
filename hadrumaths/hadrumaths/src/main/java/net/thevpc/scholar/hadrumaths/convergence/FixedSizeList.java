/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.thevpc.scholar.hadrumaths.convergence;

import java.util.Arrays;

/**
 *
 * @author vpc
 */
public class FixedSizeList<T> {

    private int count;
    private int start;
    private int end;
    private Object[] buffer;

    public FixedSizeList(int max) {
        this.buffer = new Object[max];
    }

    public int capacity() {
        return buffer.length;
    }

    public boolean isFull() {
        return count == buffer.length;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public T last() {
        return get(count - 1);
    }

    public T get(int i) {
        int ii = start + i;
        int max = buffer.length;
        if (ii >= max) {
            ii -= max;
        }
        return (T)buffer[ii];
    }

    public void add(T d) {
        buffer[end++] = d;
        int length = buffer.length;
        if (count < length) {
            count++;
        }
        if (end >= length) {
            end = 0;
        } else if (count >= length && end >= start) {
            start++;
            if (start >= length) {
                start = 0;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (count > 0) {
            sb.append(get(0));
        }
        for (int i = 1; i < count; i++) {
            sb.append(",");
            sb.append(get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    public String toDump() {
        StringBuilder sb = new StringBuilder("[");
        if (count > 0) {
            sb.append(get(0));
        }
        for (int i = 1; i < count; i++) {
            sb.append(",");
            sb.append(get(i));
        }
        sb.append("] ");
        sb.append(", buffer: ").append(Arrays.toString(buffer));
        sb.append(", count: ").append(count);
        sb.append(", start: ").append(start);
        sb.append(", end: ").append(end);
        return sb.toString();
    }

}
