package ru.otus;

import java.util.Collections;
import java.util.List;

public class DIYArrayListTest {
    public static void main(String[] args) {
        List<Integer> intArrSrc = new DIYArrayList<>();

        /** Collections.addAll(Collection<? super T> c, T... elements) */
        Collections.addAll(intArrSrc, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 10, 9, 8, 7, 6);
        System.out.println("iArrSrc: " + intArrSrc);

        /** Collections.static <T> void copy(List<? super T> dest, List<? extends T> src) */
        Collections.sort(intArrSrc);
        System.out.println("iArrSrc: " + intArrSrc);

        /** Collections.static <T> void sort(List<T> list, Comparator<? super T> c) */
        List<Integer> intArrDst = new DIYArrayList<>(24);
        Collections.addAll(intArrDst, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        System.out.println("iArrDest before: " + intArrDst);
        try{
            Collections.copy(intArrDst, intArrSrc);
        } catch (IndexOutOfBoundsException e){
            System.out.println(e.getMessage());
        }
        System.out.println("iArrDest  after: " + intArrDst);
    }
}
