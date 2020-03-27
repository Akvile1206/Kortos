package ServerSide;

import Messages.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class PlayerHandler {
    private Socket socket;
    private MultiQueue<Message> multiQueue;
    private String nickname;
    private SafeMessageQueue<Message> clientMessages;
    private CardDeck cards;
    private SafeMessageQueue<String> playerCards;
    private SafeMessageQueue<String> taken;

    public PlayerHandler(Socket s, MultiQueue<Message> q, CardDeck c) {
        socket = s;
        multiQueue = q;
        cards = c;
        clientMessages = new SafeMessageQueue<>();
        playerCards = new SafeMessageQueue<>();
        taken = new SafeMessageQueue<>();
        multiQueue.register(clientMessages);
        cards.register(playerCards, taken);

        StringBuilder sb = new StringBuilder();
        sb.append("Player");
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            sb.append(r.nextInt(9));
        }
        nickname = sb.toString();

        sb = new StringBuilder();
        sb.append(nickname);
        sb.append(" connected from ");
        sb.append(s.getInetAddress().getHostName());
        sb.append('.');
        StatusMessage statusMessage = new StatusMessage(sb.toString());
        multiQueue.put(statusMessage);

        Thread output =
                new Thread(() -> {
                    try (InputStream inputStream = s.getInputStream()) {
                        ObjectInputStream ois = new ObjectInputStream(inputStream);
                        while (true) {
                            getMessage(ois);
                        }
                    } catch (IOException e) {
                        disconnect();
                    }
                });
        output.start();

        Thread input = new Thread() {
            @Override
            public void run() {
                try (OutputStream outputStream = s.getOutputStream()) {
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    while (true) {
                        Message m = clientMessages.take();
                        oos.writeObject(m);
                    }
                } catch (IOException e) {
                    disconnect();
                }
            }
        };
        input.start();
    }

    private void disconnect() {
        multiQueue.deregister(clientMessages);
        cards.deregister(playerCards, taken);
        multiQueue.put(new StatusMessage(nickname + " has disconnected."));
    }


    private void getMessage(ObjectInputStream ois) throws IOException {
        try {
            Object received = ois.readObject();
            if (received instanceof ChangeNickMessage) {
                StringBuilder sb = new StringBuilder();
                sb.append(nickname);
                sb.append(" is now known as ");
                nickname = ((ChangeNickMessage) received).name;
                sb.append(nickname);
                sb.append('.');
                multiQueue.put(new StatusMessage(sb.toString()));
            } else if (received instanceof ChatMessage) {
                multiQueue.put(new RelayMessage(nickname, (ChatMessage)received));
            } else if (received instanceof ShuffleMessage) {
                int b = cards.shuffle((ShuffleMessage) received, multiQueue);
                multiQueue.put(new StatusMessage(nickname + " has shuffled cards. There are "+b+" cards in the bargain."));
            } else if (received instanceof PlaceCardMessage) {
                PlaceCardMessage pcm = (PlaceCardMessage) received;
                playerCards.remove(pcm.card);
                StringBuilder sb = new StringBuilder();
                sb.append(nickname);
                sb.append(" has placed ");
                if(pcm.faceUp) {
                    sb.append(pcm.card);
                } else {
                    sb.append("a card");
                }
                if(pcm.deck == 0) {
                    sb.append(" on the table.");
                    cards.placeCardOnTheTable(pcm.card, pcm.faceUp);
                    multiQueue.put(new StatusMessage(sb.toString()));
                    multiQueue.put(new CardsMessage(cards.getTable(), "Cards on the table:"));
                } else if (pcm.deck == 1) {
                    sb.append(" into their taken card deck.");
                    multiQueue.put(new StatusMessage(sb.toString()));
                    taken.put(pcm.card);
                } else if (pcm.deck == 2) {
                    sb.append(" into the bargain.");
                    multiQueue.put(new StatusMessage(sb.toString()));
                    cards.placeCardIntoTheBargain(pcm.card, pcm.faceUp);
                }
                clientMessages.put(new CardsMessage(playerCards.getAll(), "Your Cards:"));
            } else if (received instanceof TakeCardsMessage) {
                TakeCardsMessage tcm = (TakeCardsMessage) received;
                int n = tcm.number;
                if(tcm.deck == 0) { //cards from table go into taken and are put face up
                    multiQueue.put(new StatusMessage(nickname + " has taken " + n + " cards from the table."));
                    while(n > 0) {
                        n--;
                        taken.put(cards.popTable());
                    }
                    multiQueue.put(new CardsMessage(cards.getTable(), "Cards on the table:"));
                } else if (tcm.deck == 1) { //cards from taken go into the hand
                    multiQueue.put(new StatusMessage(nickname + " has taken " + n + " cards from their taken deck into their hand."));
                    while(n > 0) {
                        n--;
                        playerCards.put(taken.take());
                    }
                    clientMessages.put(new CardsMessage(playerCards.getAll(), "Your Cards:"));
                } else if (tcm.deck == 2) { //cards from the bargain go into the hand and are put face up
                    multiQueue.put(new StatusMessage(nickname + " has taken " + n + " cards from the bargain into their hand."));
                    while(n > 0) {
                        n--;
                        playerCards.put(cards.popBargain());
                    }
                    clientMessages.put(new CardsMessage(playerCards.getAll(), "Your Cards:"));
                }
            } else if (received instanceof CheckCardsMessage) {
                CheckCardsMessage ccm = (CheckCardsMessage) received;

                ArrayList<String> c = new ArrayList<>();
                String header = "";
                if(ccm.deck == 0) {
                    header = "Cards on the table:";
                    c = cards.getTable();
                } else if(ccm.deck == 1) {
                    header = nickname + " has:";
                    multiQueue.put(new StatusMessage(nickname + " has checked their taken cards."));
                    c = taken.getAll();
                } else if (ccm.deck == 2) {
                    header = "Cards in the bargain:";
                    multiQueue.put(new StatusMessage(nickname + " has checked the bargain."));
                    c = cards.getOpenBargain();
                } else if (ccm.deck == 3) {
                    header = "Your cards:";
                    multiQueue.put(new StatusMessage(nickname + " has checked their cards in hand."));
                    c = playerCards.getAll();
                }
                CardsMessage cm = new CardsMessage(c, header);

                if (ccm.isPublic) {
                    multiQueue.put(cm);
                } else {
                    clientMessages.put(cm);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
