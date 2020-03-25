package ClientSide;

import Messages.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class KortosClient implements Initializable {

    private String server = "127.0.0.1";
    private int port = 8000;
    private final double epsilon = 0.00001;

    private ArrayList<String> cards = null;
    private ArrayList<String> table = null;
    private final Socket s = createConnection(server, port);
    private ObjectOutputStream oos;
    private Thread outputThread = null;
    private long vvalLastChange = 0;
    private double vvalBeforeChange = 1.0;
    private SimpleDoubleProperty scrollBarWidthProperty = new SimpleDoubleProperty();

    @FXML private ScrollPane root;
    @FXML private Text consoleText;
    @FXML private Text inputLine;
    @FXML private TextField inputText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                KortosClient.this.readUserInput();
                ev.consume();
            }
        });
        root.addEventHandler(MouseEvent.MOUSE_PRESSED, Event::consume);
        root.setVvalue(1.0);

        if (root.getSkin() == null) {
            // Skin is not yet attached, wait until skin is attached to access the scroll bars
            ChangeListener<Skin<?>> skinChangeListener = new ChangeListener<Skin<?>>() {
                @Override
                public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
                    root.skinProperty().removeListener(this);
                    accessScrollBar(root);
                }
            };
            root.skinProperty().addListener(skinChangeListener);
        } else {
            // Skin is already attached, just access the scroll bars
            accessScrollBar(root);
        }

        consoleText.wrappingWidthProperty().bind(root.widthProperty().subtract(scrollBarWidthProperty.multiply(1.5)));
        inputText.prefWidthProperty().bind(root.widthProperty().subtract(inputLine.getLayoutBounds().getWidth())
                                                               .subtract(scrollBarWidthProperty.multiply(1.5)));
        inputText.requestFocus();
        root.vvalueProperty().addListener((observableValue, oldVal, newVal) -> {
            vvalLastChange = System.currentTimeMillis();
            vvalBeforeChange = oldVal.doubleValue();
        });
        ((VBox) root.getContent()).heightProperty().addListener((observableValue, oldVal, newVal) -> {
            if ((System.currentTimeMillis() - vvalLastChange < 5) && (vvalBeforeChange + epsilon > 1.0)) {
                root.setVvalue(1.0);
            }
        });

        if (outputThread == null) {
            outputThread = initialiseOutputThread();
            outputThread.start();
        }

        try {
            assert s != null;
            OutputStream outputStream = s.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private void accessScrollBar(ScrollPane scrollPane) {
        for (Node node : scrollPane.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation() == Orientation.VERTICAL) {
                    scrollBarWidthProperty.bind(scrollBar.widthProperty());
                }

            }
        }
    }

    private void printCards(ArrayList<String> cards) {
        for(int i=0; i<6; i++) {
            for(String c:cards) {
                displayText(CardDisplay.CardASCII.get(c)[i] + " ");
            }
            displayLine();
        }
        displayLine();
        int i=0;
        for(String c:cards) {
            StringBuilder sb = new StringBuilder();
            if(i<=9) {
                sb.append("(").append(i).append(") ");
            } else {
                sb.append("(").append(i).append(")");
            }
            if(c.length()==2) {
                sb.append(c).append(" ");
            } else {
                sb.append(c);
            }
            displayText(sb.toString() + " ");
            i++;
        }
        displayLine();
    }

    private void displayLine() {
        Platform.runLater(() -> consoleText.setText(consoleText.getText() + "\n"));
    }

    private void displayLine(String line) {
        Platform.runLater(() -> consoleText.setText(consoleText.getText() + line + "\n"));
    }

    private void displayText(String line) {
        Platform.runLater(() -> consoleText.setText(consoleText.getText() + line));
    }

    private void getMessage(ObjectInputStream ois) {
        try {
            Object received = ois.readObject();
            if (received instanceof RelayMessage) {
                RelayMessage relay = (RelayMessage) received;
                displayLine(getFormattedTime() + " [" + relay.getFrom() + "] " + relay.getMessage());
            } else if (received instanceof StatusMessage) {
                displayLine(getFormattedTime() + " [Server] " + ((StatusMessage) received).getMessage());
            } else if (received instanceof CardsMessage) {
                CardsMessage cm = (CardsMessage) received;
                displayLine(cm.header);
                printCards(cm.cards);
                displayLine();
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

    private void sendMessage(String out, ObjectOutputStream oos) {
        try {
            if (out.startsWith("\\")) {
                String[] split = out.split(" ");

                int deck;
                switch (split[0]) {
                    case "\\nick":
                        ChangeNickMessage cnm = new ChangeNickMessage(split[1]);
                        oos.writeObject(cnm);
                        break;
                    case "\\shuffle":
                        int from = 9; //default - suitable for King 3 players
                        int initial = 8; //default
                        try {
                            if (split.length > 1) {
                                from = Integer.parseInt(split[1]);
                            }
                            if (split.length > 2) {
                                initial = Integer.parseInt(split[2]);
                            }
                        } catch (NumberFormatException e) {
                            displayLine("Usage: \\shuffle [starting card] [initial cards per player]");
                            return;
                        }
                        ShuffleMessage sm = new ShuffleMessage(from, initial);
                        oos.writeObject(sm);
                        break;
                    case "\\cards": {
                        if (cards == null) {
                            displayLine("You have no cards yet!");
                            return;
                        }
                        displayLine("Your Cards: ");
                        int i = 0;
                        for (String c : cards) {
                            displayText("(" + i + ")" + c + " ");
                            i++;
                        }
                        displayLine();

                        break;
                    }
                    case "\\place":
                        String cardCode = split[1].toUpperCase();
                        if (!cards.contains(cardCode)) {
                            displayLine("You don't have this card. Type \\cards to see your cards and their code.");
                            return;
                        }
                        boolean faceUp = true; //default
                        deck = 0; //0 - table, 1 - taken, 2 - buffer
                        if(split.length>2) {
                            switch (split[2]) {
                                case "0":
                                    faceUp = false;
                                    break;
                                case "1":
                                    faceUp = true;
                                    break;
                                default:
                                    displayLine("Usage: \\place [card code] [face up?] [destination code]");
                                    return;
                            }
                        }
                        if(split.length>3) {
                            switch (split[3]) {
                                case "0":
                                    deck = 0;
                                    break;
                                case "1":
                                    deck = 1;
                                    break;
                                case "2":
                                    deck = 2;
                                    break;
                                default:
                                    displayLine("Usage: \\place [card code] [face up?] [destination code]");
                                    return;
                            }
                        }
                        PlaceCardMessage pcm = new PlaceCardMessage(cardCode, faceUp, deck);
                        oos.writeObject(pcm);
                        cards.remove(cardCode);
                        break;
                    case "\\table": {
                        if (cards == null) {
                            displayLine("There are no cards on the table yet!");
                            return;
                        }
                        displayLine("Cards on the table: ");
                        int i = 0;
                        for (String c : table) {
                            displayText("(" + i + ")" + c + " ");
                            i++;
                        }
                        displayLine();

                        break;
                    }
                    case "\\take":
                        deck = 2; //0 - table, 1 - taken, 2 - bargain
                        int number = 1; //default
                        if(split.length>1) {
                            switch (split[1]) {
                                case "0":
                                    deck = 0;
                                    break;
                                case "1":
                                    deck = 1;
                                    break;
                                case "2":
                                    deck = 2;
                                    break;
                                default:
                                    displayLine("Usage: \\take [source code] [number]");
                                    return;
                            }
                        }
                        try {
                            if (split.length > 2) {
                                number = Integer.parseInt(split[2]);
                            }
                        } catch (NumberFormatException e) {
                            displayLine("Usage: \\take [source code] [number]");
                            return;
                        }
                        oos.writeObject(new TakeCardsMessage(deck,number));
                        break;
                    case "\\checkTaken":
                        boolean isPublic = true; //default
                        if(split.length > 1) {
                            switch (split[1]) {
                                case "1":
                                    isPublic = true;
                                    break;
                                case "0":
                                    isPublic = false;
                                    break;
                                default:
                                    displayLine("Usage: \\checkTaken [isPublic]");
                                    return;
                            }
                        }
                        oos.writeObject(new CheckCardsMessage(isPublic));
                        break;
                    default:
                        String command = split[0].substring(1);
                        displayLine(getFormattedTime() + " [Client] Unknown command \"" + command + "\"");
                        break;
                }
            }
            else {
                ChatMessage cm = new ChatMessage(out);
                oos.writeObject(cm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        return sdf.format(now);
    }

    private Socket createConnection(String server, int port) {
        final Socket s;
        try {
            s = new Socket(server, port);
            displayLine(getFormattedTime() + " [Client] Connected to " + server
                    + " on port " + port + ".");
        } catch (IOException e) {
            System.err.println("Cannot connect to " + server + " on port " + port);
            return null;
        }
        return s;
    }

    private Thread initialiseOutputThread() {
        Thread output =
                new Thread(() -> {
                    // read bytes from the socket, interpret them as string data and
                    // print the resulting string data to the screen.
                    assert s != null;
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

        return output;
    }

    @FXML
    private void readUserInput(){
        assert s != null;
        sendMessage(inputText.getText(), oos);
        inputText.setText("");
    }
}
