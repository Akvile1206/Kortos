package ClientSide;

import java.util.HashMap;

class CardDisplay {

    static HashMap<String, String[]> CardASCII = new HashMap<>();
    static {
        CardASCII.put("9S", new String[]{
                " _____ ",
                "|9    |",
                "|♠ ♠ ♠|",
                "|♠ ♠ ♠|",
                "|♠ ♠ ♠|",
                "|____9|"
        });
        CardASCII.put("10S", new String[]{
                " _____ ",
                "|10 ♠ |",
                "|♠ ♠ ♠|",
                "|♠ ♠ ♠|",
                "|♠ ♠ ♠|",
                "|___10|"
        });
        CardASCII.put("JS", new String[]{
                " _____ ",
                "|J  __|",
                "| ^ {)|",
                "|(.)% |",
                "| | % |",
                "|__%%J|"
        });
        CardASCII.put("QS", new String[]{
                " _____ ",
                "|Q  ww|",
                "| ^ {(|",
                "|(.)%%|",
                "| |%%%|",
                "|_%%%Q|"
        });
        CardASCII.put("KS", new String[]{
                " _____ ",
                "|K  ww|",
                "| ^ {)|",
                "|(.)%%|",
                "| |%%%|",
                "|_%%%K|"
        });
        CardASCII.put("AS", new String[]{
                " _____ ",
                "|A .  |",
                "| /.\\ |",
                "|(_._)|",
                "|  |  |",
                "|____A|"
        });
        CardASCII.put("9C", new String[]{
                " _____ ",
                "|9    |",
                "|♣ ♣ ♣|",
                "|♣ ♣ ♣|",
                "|♣ ♣ ♣|",
                "|____9|"
        });
        CardASCII.put("10C", new String[]{
                " _____ ",
                "|10 ♣ |",
                "|♣ ♣ ♣|",
                "|♣ ♣ ♣|",
                "|♣ ♣ ♣|",
                "|___10|"
        });
        CardASCII.put("JC", new String[]{
                " _____ ",
                "|J  __|",
                "| o {)|",
                "|o o% |",
                "| | % |",
                "|__%%J|"
        });
        CardASCII.put("QC", new String[]{
                " _____ ",
                "|Q  ww|",
                "| o {(|",
                "|o o%%|",
                "| |%%%|",
                "|_%%%Q|"
        });
        CardASCII.put("KC", new String[]{
                " _____ ",
                "|K  ww|",
                "| o {)|",
                "|o o%%|",
                "| |%%%|",
                "|_%%%K|"
        });
        CardASCII.put("AC", new String[]{
                " _____ ",
                "|A _  |",
                "| ( ) |",
                "|(_'_)|",
                "|  |  |",
                "|____A|"
        });
        CardASCII.put("9H", new String[]{
                " _____ ",
                "|9    |",
                "|♥ ♥ ♥|",
                "|♥ ♥ ♥|",
                "|♥ ♥ ♥|",
                "|____9|"
        });
        CardASCII.put("10H", new String[]{
                " _____ ",
                "|10 ♥ |",
                "|♥ ♥ ♥|",
                "|♥ ♥ ♥|",
                "|♥ ♥ ♥|",
                "|___10|"
        });
        CardASCII.put("JH", new String[]{
                " _____ ",
                "|J  __|",
                "|   {)|",
                "|(v)% |",
                "| Y % |",
                "|__%%J|"
        });
        CardASCII.put("QH", new String[]{
                " _____ ",
                "|Q  ww|",
                "|   {(|",
                "|(v)%%|",
                "| Y%%%|",
                "|_%%%Q|"
        });
        CardASCII.put("KH", new String[]{
                " _____ ",
                "|K  ww|",
                "|   {)|",
                "|(v)%%|",
                "| Y%%%|",
                "|_%%%K|"
        });
        CardASCII.put("AH", new String[]{
                " _____ ",
                "|A_ _ |",
                "|( v )|",
                "| \\ / |",
                "|  .  |",
                "|____A|"
        });
        CardASCII.put("9D", new String[]{
                " _____ ",
                "|9    |",
                "|♦ ♦ ♦|",
                "|♦ ♦ ♦|",
                "|♦ ♦ ♦|",
                "|____9|"
        });
        CardASCII.put("10D", new String[]{
                " _____ ",
                "|10 ♦ |",
                "|♦ ♦ ♦|",
                "|♦ ♦ ♦|",
                "|♦ ♦ ♦|",
                "|___10|"
        });
        CardASCII.put("JD", new String[]{
                " _____ ",
                "|J  __|",
                "| /\\{)|",
                "| \\/% |",
                "|   % |",
                "|__%%J|"
        });
        CardASCII.put("QD", new String[]{
                " _____ ",
                "|Q  ww|",
                "| /\\{(|",
                "| \\/%%|",
                "|  %%%|",
                "|_%%%Q|"
        });
        CardASCII.put("KD", new String[]{
                " _____ ",
                "|K  ww|",
                "| /\\{)|",
                "| \\/%%|",
                "|  %%%|",
                "|_%%%K|"
        });
        CardASCII.put("AD", new String[]{
                " _____ ",
                "|A ^  |",
                "| / \\ |",
                "| \\ / |",
                "|  .  |",
                "|____A|"
        });
    }
}