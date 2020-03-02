package net.vpc.scholar.hadrumaths.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class InternalUnmodifiableArrayList<E> extends AbstractList<E> implements RandomAccess, java.io.Serializable {
    private static final long serialVersionUID = -2764017481108945198L;
    public E[] array;
    private int eagerHashCode = 0;

    public InternalUnmodifiableArrayList(E... array) {
        this.array = array;
    }

    @Override
    public E get(int index) {
        return array[index];
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException();
//        E oldValue = a[index];
//        a[index] = primitiveElement3D;
//        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        E[] a = this.array;
        if (o == null) {
            for (int i = 0; i < a.length; i++)
                if (a[i] == null)
                    return i;
        } else {
            for (int i = 0; i < a.length; i++)
                if (o.equals(a[i]))
                    return i;
        }
        return -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr(array);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof List)) {
            return false;
        }

//        ListIterator<E> e1 = listIterator();
        ListIterator<?> e2 = ((List<?>) o).listIterator();
        int index = 0;
        int mySize = size();
        while (index < mySize && e2.hasNext()) {
            E o1 = array[index];
            Object o2 = e2.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2)))
                return false;
            index++;
        }
        return !(index < mySize || e2.hasNext());
    }

    @Override
    public int hashCode() {
        if (eagerHashCode == 0) {
            int h = 0;
            for (E e : array) {
                h = 31 * h + (e == null ? 0 : e.hashCode());
            }
            eagerHashCode = h;
        }
        return eagerHashCode;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Object[] toArray() {
        return array.clone();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size)
            return Arrays.copyOf(this.array, size,
                    (Class<? extends T[]>) a.getClass());
        System.arraycopy(this.array, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        E[] a = this.array;
        for (E e : a) {
            action.accept(e);
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
//        Objects.requireNonNull(operator);
//        E[] a = this.a;
//        for (int i = 0; i < a.length; i++) {
//            a[i] = operator.apply(a[i]);
//        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        throw new IllegalArgumentException();
        //Arrays.sort(array, c);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(array, Spliterator.ORDERED);
    }

    private class Itr implements Iterator<E> {
        private final E[] a;
        private int cursor = 0;

        public Itr(E[] a) {
            this.a = a;
        }

        public boolean hasNext() {
            return cursor != size();
        }

        public E next() {
            try {
                int i = cursor;
                E next = get(i);
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new IllegalArgumentException("Not Modifiable");
        }
    }


}
