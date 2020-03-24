package ClientSide;

import Messages.*;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class KortosClient {

    static ArrayList<String> cards = null;
    static ArrayList<String> table = null;

    private static void getMessage(ObjectInputStream ois) {
        try {
            Object received = ois.readObject();
            if (received instanceof RelayMessage) {
                RelayMessage relay = (RelayMessage) received;
                System.out.println(getFormattedTime() + " [" + relay.getFrom() + "] " + relay.getMessage());
            } else if (received instanceof StatusMessage) {
                System.out.println(getFormattedTime() + " [Server] " + ((StatusMessage) received).getMessage());
            } else if (received instanceof CardsMessage) {
                CardsMessage cm = (CardsMessage) received;
                System.out.println(cm.header);
                int i = 0;
                for(String c : cm.cards) {
                    System.out.print("("+i+")"+c+" ");
                    i++;
                }
                System.out.println();
                if(cm.header.equalsIgnoreCase("Your Cards:")) {
                    cards = cm.cards;
                } else if (cm.header.equalsIgnoreCase("Cards on the table:")) {
                    table = cm.cards;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(String out, ObjectOutputStream oos) {
        try {
            if (out.startsWith("\\nick")) {
                String[] split = out.split(" ");
                ChangeNickMessage cnm = new ChangeNickMessage(split[1]);
                oos.writeObject(cnm);
            } else if(out.startsWith("\\shuffle")){
                ShuffleMessage sm = new ShuffleMessage(9);
                oos.writeObject(sm);
            } else if (out.startsWith("\\cards")) {
                if(cards == null) {
                    System.out.println("You have no cards yet!");
                    return;
                }
                System.out.println("Your Cards: ");
                int i = 0;
                for(String c : cards) {
                    System.out.print("("+i+")"+c+" ");
                    i++;
                }
                System.out.println();

            } else if (out.startsWith("\\place")) {
                String[] split = out.split(" ");
                int index = Integer.parseInt(split[1]);
                if(index > cards.size()) {
                    System.out.println("This card index does not exist. Type \\cards to see your cards and their indexes.");
                    return;
                }
                PlaceCardMessage pcm = new PlaceCardMessage(cards.get(index));
                oos.writeObject(pcm);
                cards.remove(index);
            } else if (out.startsWith("\\table")) {
                if(cards == null) {
                    System.out.println("There are no cards on the table yet!");
                    return;
                }
                System.out.println("Cards on the table: ");
                int i = 0;
                for(String c : table) {
                    System.out.print("("+i+")"+c+" ");
                    i++;
                }
                System.out.println();

            } else if(out.startsWith("\\take")) {
                oos.writeObject(new TakeCardsMessage());
            } else if (out.startsWith("\\checkTaken")) {
                oos.writeObject(new CheckCardsMessage());
            } else if (out.startsWith("\\")) {
                String[] split = out.split(" ");
                String command = split[0].substring(1);
                System.out.println(getFormattedTime() + " [Client] Unknown command \"" + command + "\"");
            } else {
                ChatMessage cm = new ChatMessage(out);
                oos.writeObject(cm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdf.format(now);
    }

    public static void main(String[] args) {

        String server = "localhost";//"131.111.8.60";
        int port = 8000;

        final Socket s; // connect to "server" on "port".
        try {
            s = new Socket(server, port);
            System.out.println(getFormattedTime() + " [Client] Connected to " + server
                    + " on port " + port + ".");
        } catch (IOException e) {
            System.err.println("Cannot connect to " + server + " on port " + port);
            return;
        }
        Thread output =
                new Thread(() -> {
                    // read bytes from the socket, interpret them as string data and
                    // print the resulting string data to the screen.
                    try (InputStream inputStream = s.getInputStream()) {
                        ObjectInputStream ois = new ObjectInputStream(inputStream);
                        while (true) {
                            getMessage(ois);
                        }
                    } catch (IOException e) {
                        System.err.println("Cannot get input");
                    }
                });
        output.setDaemon(true); // allows JVM to exit when this thread is running.
        output.start();

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        try (OutputStream outputStream = s.getOutputStream()) {
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            while (true) {
                // read data from the user, blocking until ready. Convert the
                // string data from the user into an array of bytes and write
                // the array of bytes to "socket".
                try {
                    String out = r.readLine();
                    sendMessage(out, oos);
                } catch (IOException e) {
                    System.err.println("Cannot get output");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
