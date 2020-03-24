package ServerSide;

import java.util.ArrayList;
import java.util.LinkedList;

public class SafeMessageQueue<T> {
    private static class Link<L> {
        L val;
        Link<L> next;

        Link(L val) {
            this.val = val;
            this.next = null;
        }
    }

    private Link<T> first = null;
    private Link<T> last = null;

    public synchronized ArrayList<T> getAll() {
        ArrayList<T> all = new ArrayList<>();
        Link<T> current = first;
        while (current != null) {
            all.add(current.val);
            current = current.next;
        }
        return all;
    }

    public synchronized void remove(T v) {
        Link<T> prev = null;
        Link<T> current = first;
        while (current != null) {
            if(current.val.equals(v)) {
                if(prev == null) {
                    first = current.next;
                    return;
                }
                prev.next = current.next;
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public synchronized void flush() {
        first = null;
        last = null;
    }

    public synchronized void put(T val) {
        //      given a new "val", create a new Link<T>
        //      element to contain it and update "first" and
        //      "last" as appropriate
        Link<T> current = new Link<>(val);

        if(first == null){
            first = current;
            last = current;
            this.notify();
            return;
        }

        last.next = current;
        last = current;
        this.notify();
    }

    public synchronized T take() {
        while (first == null) { // use a loop to block thread until data is available
            try {
                this.wait();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                return null;
                // Ignored exception
                //       what causes this exception to be thrown? and what should
                //       you do with it ideally?
                // This exception is thrown when a method checks thred.interupted flag.
                // A method then should just throw this exception immediately. Ideally,
                // after catching this exception we would clean everything and stop.
            }
        }
        //      retrieve "val" from "first", update "first" to refer
        //      to next element in list (if any). Return "val"
        T val = first.val;
        first = first.next;
        return val;
    }
}
