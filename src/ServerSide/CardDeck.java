package ServerSide;

import Messages.Message;

import java.util.*;

public class CardDeck {
    private Set<SafeMessageQueue<String>> hands = new HashSet<>();
    private SafeMessageQueue<String> table = new SafeMessageQueue<>();
    private HashMap<String, ArrayList<String>> taken = new HashMap<>();

    public HashMap<String, ArrayList<String>> getTaken() {
        return (HashMap<String, ArrayList<String>>)taken.clone();
    }

    public void takeTable(String nick) {
        ArrayList<String> buffer = taken.getOrDefault(nick, new ArrayList<>());
        buffer.addAll(table.getAll());
        table.flush();
        taken.put(nick, buffer);
    }

    public ArrayList<String> getTable() {
        return table.getAll();
    }

    public void placeCard(String card) {
        table.put(card);
    }

    public void register(SafeMessageQueue<String> q) {
        synchronized (this) {
            hands.add(q);
        }
    }

    public void deregister(SafeMessageQueue<String> q) {
        synchronized (this) {
            hands.remove(q);
        }
    }

    public synchronized void getCards() throws InterruptedException {
        this.wait();
    }

    public synchronized void shuffle(int from, MultiQueue<Message> players) {
        taken = new HashMap<>();
        table.flush();
        for(SafeMessageQueue<String> h : hands) {
            h.flush();
        }

        LinkedList<String> cards = new LinkedList<>();
        LinkedList<String> suits = new LinkedList<String>();
        suits.add("S");
        suits.add("H");
        suits.add("D");
        suits.add("C");

        for(String s : suits) {
            for(int i = from; i<=10; i++) {
                cards.add(i+s);
            }
            cards.add("J"+s);
            cards.add("Q"+s);
            cards.add("K"+s);
            cards.add("A"+s);
        }

        Collections.shuffle(cards);

        int cardsPerPlayer = cards.size()/players.getNumberOfPlayers();
        for(SafeMessageQueue<String> h : hands) {
            for(int i=0; i<cardsPerPlayer; i++) {
                h.put(cards.pop());
            }
        }

        this.notifyAll();
    }

}
