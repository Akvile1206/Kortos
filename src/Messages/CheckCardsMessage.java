package Messages;

public class CheckCardsMessage extends Message {
    public boolean isPublic;

    public CheckCardsMessage(boolean isPublic) {
        this.isPublic = isPublic;
    }

}
