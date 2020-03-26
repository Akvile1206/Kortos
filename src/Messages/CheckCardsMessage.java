package Messages;

public class CheckCardsMessage extends Message {
    public boolean isPublic;

    public int deck; //0 - table, 1 - taken, 2 - buffer

    public CheckCardsMessage(boolean isPublic, int deck) {
        this.deck = deck;
        this.isPublic = isPublic;
    }

}
