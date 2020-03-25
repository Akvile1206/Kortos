package Messages;

public class TakeCardsMessage extends Message {
    private static final long serialVersionUID = 1L;

    public int deck; //0 - table, 1 - taken, 2 - buffer

    public int number;

    public TakeCardsMessage(int deck, int number) {
        super();
        this.deck = deck;
        this.number = number;
    }
}
