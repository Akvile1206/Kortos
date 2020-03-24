package Messages;

public class PlaceCardMessage extends Message {
    private static final long serialVersionUID = 1L;

    public String card;

    public PlaceCardMessage(String card) {
        super();
        this.card = card;
    }
}
