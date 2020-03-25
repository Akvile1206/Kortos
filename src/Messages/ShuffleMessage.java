package Messages;

import java.io.Serializable;

public class ShuffleMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public int from; //number from 2 to 10

    public int initial; //initial number of cards per player

    public ShuffleMessage(int from, int initial) {
        super();
        this.from = from;
        this.initial = initial;
    }
}
