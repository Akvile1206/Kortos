package Messages;

import java.io.Serializable;

public class ShuffleMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public int from;

    public ShuffleMessage(int from) {
        super();
        this.from = from;
    }
}
