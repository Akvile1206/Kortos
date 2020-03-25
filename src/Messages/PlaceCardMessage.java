package Messages;

public class PlaceCardMessage extends Message {
    private static final long serialVersionUID = 1L;

    public String card;

    public boolean faceUp;

    public int deck; //0 - table, 1 - taken, 2 - buffer

    public PlaceCardMessage(String card, boolean faceUp, int deck) {
        super();
        this.card = card;
        this.faceUp = faceUp;
        this.deck = deck;
    }
}
