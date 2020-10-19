package ru.otus;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DIYArrayList<T> implements List<T> {

    private final static Logger log = Logger.getLogger(DIYArrayList.class.getName());

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};
    private Object[] _array;
    private int size;

    public DIYArrayList() {
        this._array = EMPTY_ELEMENTDATA;
    }

    public DIYArrayList(int capacity) {
        if (capacity > 0) {
            this._array = new Object[capacity];
        } else if (capacity == 0) {
            this._array = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: " + capacity);
        }
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    public boolean add(T t) {
        if (size == _array.length) {
            int newCapacity = (int) (1.5 * _array.length);
            log.fine("1.5 * _array.length: " + newCapacity);
            newCapacity = Math.max(newCapacity, DEFAULT_CAPACITY);
            log.fine("max(newCapacity, DEFAULT_CAPACITY): " + newCapacity);
            this._array = Arrays.copyOf(_array, newCapacity);
        }
        this._array[this.size] = t;
        this.size++;
        return true;
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public T get(int index) {
        throw new UnsupportedOperationException();
    }

    public T set(int index, T element) {
        if (index < 0 || index >= _array.length) {
            throw new IllegalArgumentException("Index: " + index + ", size: " + size);
        }
        T oldValue = (T) _array[index];
        _array[index] = element;
        return oldValue;
    }

    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    public ListIterator<T> listIterator() {
//        throw new UnsupportedOperationException();
        return new ListItr();
    }

    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        if (this.size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(_array[0].toString());
        for (int i = 1; i < this.size; i++) {
            sb.append(", ");
            sb.append(_array[i].toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) _array, 0, size, c);
    }

    private class ListItr implements ListIterator<T> {

        int cursor = 0;   // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        public T next() {
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            cursor = i + 1;
            return (T) DIYArrayList.this._array[lastRet = i];
        }

        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        public T previous() {
            throw new UnsupportedOperationException();
        }

        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void forEachRemaining(Consumer<? super T> action) {
            throw new UnsupportedOperationException();
        }

        public void set(T t) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            DIYArrayList.this.set(lastRet, t);
        }

        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }
}