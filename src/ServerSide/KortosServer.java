package ServerSide;

import Messages.Message;
import Messages.StatusMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class KortosServer {

    public static void main(String[] args) {
        try(ServerSocket ss = new ServerSocket(8000);
        ) {
            MultiQueue<Message> queue = new MultiQueue<Message>();
            CardDeck cards = new CardDeck();
            while(true) {
                Socket socket = ss.accept();
                PlayerHandler handler = new PlayerHandler(socket, queue, cards);

                queue.put(new StatusMessage("Current number of players: " + queue.getNumberOfPlayers()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
