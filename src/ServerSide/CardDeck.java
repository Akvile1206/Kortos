package ServerSide;

import Messages.Message;
import Messages.ShuffleMessage;

import java.util.*;

public class CardDeck {
    private Set<SafeMessageQueue<String>> hands = new HashSet<>();
    private SafeMessageQueue<PublicCard> table = new SafeMessageQueue<>();
    private SafeMessageQueue<PublicCard> bargain = new SafeMessageQueue<>();
    private Set<SafeMessageQueue<String>> taken = new HashSet<>();

    public String popTable() {return table.take().card;}

    public String popBargain() {return bargain.take().card;}

    public ArrayList<String> getTable() {
        ArrayList<PublicCard> cards = table.getAll();
        ArrayList<String> table = new ArrayList<>();
        for(PublicCard c : cards) {
            if(c.faceUp) {
                table.add(c.card);
            } else {
                table.add("??");
            }
        }
        return table;
    }

    public void placeCardOnTheTable(String card, boolean faceUp) {
        table.put(new PublicCard(card, faceUp));
    }

    public void placeCardIntoTheBuffer(String card, boolean faceUp) {
        bargain.put(new PublicCard(card, faceUp));
    }


    public void register(SafeMessageQueue<String> q, SafeMessageQueue<String> t) {
        synchronized (this) {
            hands.add(q);
            taken.add(t);
        }
    }

    public void deregister(SafeMessageQueue<String> q, SafeMessageQueue<String> t) {
        synchronized (this) {
            hands.remove(q);
            taken.remove(t);
        }
    }

    public synchronized void getCards() throws InterruptedException {
        this.wait();
    }

    public synchronized int shuffle(ShuffleMessage m, MultiQueue<Message> players) {

        table.flush();
        for(SafeMessageQueue<String> h : hands) {
            h.flush();
        }
        for(SafeMessageQueue<String> h : taken) {
            h.flush();
        }

        LinkedList<String> cards = new LinkedList<>();
        LinkedList<String> suits = new LinkedList<String>();
        suits.add("S");
        suits.add("H");
        suits.add("D");
        suits.add("C");

        for(String s : suits) {
            for(int i = m.from; i<=10; i++) {
                cards.add(i+s);
            }
            cards.add("J"+s);
            cards.add("Q"+s);
            cards.add("K"+s);
            cards.add("A"+s);
        }

        Collections.shuffle(cards);

        for(SafeMessageQueue<String> h : hands) {
            for(int i=0; i<m.initial; i++) {
                h.put(cards.pop());
            }
        }

        int b = 0;
        while (!cards.isEmpty()) {
            bargain.put(new PublicCard(cards.pop(),false));
            b++;
        }

        this.notifyAll();
        return b;
    }

    private class PublicCard {
        private String card;
        private boolean faceUp;

        PublicCard(String card, boolean fu) {
            this.card = card;
            faceUp = fu;
        }
    }
}
