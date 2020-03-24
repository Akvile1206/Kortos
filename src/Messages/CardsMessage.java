package Messages;

import java.util.ArrayList;

public class CardsMessage extends Message {
    private static final long serialVersionUID = 1L;
    public ArrayList<String> cards;
    public  String header;

    public CardsMessage(ArrayList<String> cards, String header) {
        super();
        this.cards = cards;
        this.header = header;
    }
}
