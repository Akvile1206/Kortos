package ServerSide;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class MultiQueue<T> {

    private Set<SafeMessageQueue<T>> outputs = new HashSet<>();

    public void register(SafeMessageQueue<T> q) {
        synchronized (this) {
            outputs.add(q);
        }
    }

    public void deregister(SafeMessageQueue<T> q) {
        synchronized (this) {
            outputs.remove(q);
        }
    }

    public void put(T message) {
        // copy "message" to all elements in "outputs"
        synchronized (this) {
            for (SafeMessageQueue<T> o : outputs) {
                o.put(message);
            }
        }
    }

    public int getNumberOfPlayers() {
        return outputs.size();
    }
}