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

    public PlayerHandler(Socket s, MultiQueue<Message> q, CardDeck c) {
        socket = s;
        multiQueue = q;
        cards = c;
        clientMessages = new SafeMessageQueue<>();
        playerCards = new SafeMessageQueue<>();
        multiQueue.register(clientMessages);
        cards.register(playerCards);

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
        Thread getCards = new Thread(){
            @Override
            public  void  run() {
                while (true) {
                    try {
                        cards.getCards();
                        ArrayList<String> hand = playerCards.getAll();
                        if(hand.size()>0) {
                            clientMessages.put(new CardsMessage(hand, "Your Cards:"));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        getCards.start();


    }

    private void disconnect() {
        multiQueue.deregister(clientMessages);
        cards.deregister(playerCards);
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
                cards.shuffle(((ShuffleMessage) received).from, multiQueue);
            } else if (received instanceof PlaceCardMessage) {
                PlaceCardMessage pcm = (PlaceCardMessage) received;
                StringBuilder sb = new StringBuilder();
                sb.append(nickname);
                sb.append(" has placed ");
                sb.append(pcm.card);
                sb.append(" on the table.");
                cards.placeCard(pcm.card);
                playerCards.remove(pcm.card);
                multiQueue.put(new StatusMessage(sb.toString()));
                multiQueue.put(new CardsMessage(cards.getTable(), "Cards on the table:"));
                clientMessages.put(new CardsMessage(playerCards.getAll(), "Your Cards:"));
            } else if (received instanceof TakeCardsMessage) {
                cards.takeTable(nickname);
                multiQueue.put(new StatusMessage(nickname + " has taken the cards from the table."));
            } else if (received instanceof CheckCardsMessage) {
                multiQueue.put(new StatusMessage(nickname + " has checked all the taken cards."));
                HashMap<String, ArrayList<String>> taken = cards.getTaken();
                for(Map.Entry<String, ArrayList<String>> e: taken.entrySet()) {
                    multiQueue.put(new CardsMessage((ArrayList<String>) e.getValue().clone(), e.getKey()+ " has:"));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
