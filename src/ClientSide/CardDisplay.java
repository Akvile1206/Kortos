package ClientSide;

import java.util.HashMap;

class CardDisplay {

    static HashMap<String, String[]> CardASCII = new HashMap<>();
    static {
        CardASCII.put("??", new String[]{
                " _____ ",
                "|\\ ~ /|",
                "|}}:{{|",
                "|}}:{{|",
                "|}}:{{|",
                "|/_~_\\|"
        });
        CardASCII.put("2S", new String[]{
                " _____ ",
                "|2    |",
                "|  ♠  |",
                "|     |",
                "|  ♠  |",
                "|____2|"
        });
        CardASCII.put("3S", new String[]{
                " _____ ",
                "|3    |",
                "|  ♠  |",
                "|  ♠  |",
                "|  ♠  |",
                "|____3|"
        });
        CardASCII.put("4S", new String[]{
                " _____ ",
                "|4    |",
                "|♠   ♠|",
                "|     |",
                "|♠   ♠|",
                "|____4|"
        });
        CardASCII.put("5S", new String[]{
                " _____ ",
                "|5    |",
                "|♠   ♠|",
                "|  ♠  |",
                "|♠   ♠|",
                "|____5|"
        });
        CardASCII.put("6S", new String[]{
                " _____ ",
                "|6    |",
                "|♠   ♠|",
                "|♠   ♠|",
                "|♠   ♠|",
                "|____6|"
        });
        CardASCII.put("7S", new String[]{
                " _____ ",
                "|7    |",
                "|♠   ♠|",
                "|♠ ♠ ♠|",
                "|♠   ♠|",
                "|____7|"
        });
        CardASCII.put("8S", new String[]{
                " _____ ",
                "|8   ♠|",
                "|♠   ♠|",
                "|♠   ♠|",
                "|♠   ♠|",
                "|♠___8|"
        });
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
                "|J♠ __|",
                "| ^ {)|",
                "|(.)% |",
                "| | % |",
                "|__%♠J|"
        });
        CardASCII.put("QS", new String[]{
                " _____ ",
                "|Q♠ ww|",
                "| ^ {(|",
                "|(.)%%|",
                "| |%%%|",
                "|_%%♠Q|"
        });
        CardASCII.put("KS", new String[]{
                " _____ ",
                "|K♠ ww|",
                "| ^ {)|",
                "|(.)%%|",
                "| |%%%|",
                "|_%%♠K|"
        });
        CardASCII.put("AS", new String[]{
                " _____ ",
                "|A .  |",
                "| /.\\ |",
                "|(_._)|",
                "|  |  |",
                "|____A|"
        });
        CardASCII.put("2C", new String[]{
                " _____ ",
                "|2    |",
                "|  ♣  |",
                "|     |",
                "|  ♣  |",
                "|____2|"
        });
        CardASCII.put("3C", new String[]{
                " _____ ",
                "|3    |",
                "|  ♣  |",
                "|  ♣  |",
                "|  ♣  |",
                "|____3|"
        });
        CardASCII.put("4C", new String[]{
                " _____ ",
                "|4    |",
                "|♣   ♣|",
                "|     |",
                "|♣   ♣|",
                "|____4|"
        });
        CardASCII.put("5C", new String[]{
                " _____ ",
                "|5    |",
                "|♣   ♣|",
                "|  ♣  |",
                "|♣   ♣|",
                "|____5|"
        });
        CardASCII.put("6C", new String[]{
                " _____ ",
                "|6    |",
                "|♣   ♣|",
                "|♣   ♣|",
                "|♣   ♣|",
                "|____6|"
        });
        CardASCII.put("7C", new String[]{
                " _____ ",
                "|7    |",
                "|♣   ♣|",
                "|♣ ♣ ♣|",
                "|♣   ♣|",
                "|____7|"
        });
        CardASCII.put("8C", new String[]{
                " _____ ",
                "|8   ♣|",
                "|♣   ♣|",
                "|♣   ♣|",
                "|♣   ♣|",
                "|♣___8|"
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
                "|J♣ __|",
                "| o {)|",
                "|o o% |",
                "| | % |",
                "|__%♣J|"
        });
        CardASCII.put("QC", new String[]{
                " _____ ",
                "|Q♣ ww|",
                "| o {(|",
                "|o o%%|",
                "| |%%%|",
                "|_%%♣Q|"
        });
        CardASCII.put("KC", new String[]{
                " _____ ",
                "|K♣ ww|",
                "| o {)|",
                "|o o%%|",
                "| |%%%|",
                "|_%%♣K|"
        });
        CardASCII.put("AC", new String[]{
                " _____ ",
                "|A♣_  |",
                "| ( ) |",
                "|(_'_)|",
                "|  |  |",
                "|___♣A|"
        });
        CardASCII.put("2H", new String[]{
                " _____ ",
                "|2    |",
                "|  ♥  |",
                "|     |",
                "|  ♥  |",
                "|____2|"
        });
        CardASCII.put("3H", new String[]{
                " _____ ",
                "|3    |",
                "|  ♥  |",
                "|  ♥  |",
                "|  ♥  |",
                "|____3|"
        });
        CardASCII.put("4H", new String[]{
                " _____ ",
                "|4    |",
                "|♥   ♥|",
                "|     |",
                "|♥   ♥|",
                "|____4|"
        });
        CardASCII.put("5H", new String[]{
                " _____ ",
                "|5    |",
                "|♥   ♥|",
                "|  ♥  |",
                "|♥   ♥|",
                "|____5|"
        });
        CardASCII.put("6H", new String[]{
                " _____ ",
                "|6    |",
                "|♥   ♥|",
                "|♥   ♥|",
                "|♥   ♥|",
                "|____6|"
        });
        CardASCII.put("7H", new String[]{
                " _____ ",
                "|7    |",
                "|♥   ♥|",
                "|♥ ♥ ♥|",
                "|♥   ♥|",
                "|____7|"
        });
        CardASCII.put("8H", new String[]{
                " _____ ",
                "|8   ♥|",
                "|♥   ♥|",
                "|♥   ♥|",
                "|♥   ♥|",
                "|♥___8|"
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
                "|J♥ __|",
                "|   {)|",
                "|(v)% |",
                "| Y % |",
                "|__%♥J|"
        });
        CardASCII.put("QH", new String[]{
                " _____ ",
                "|Q♥ ww|",
                "|   {(|",
                "|(v)%%|",
                "| Y%%%|",
                "|_%%♥Q|"
        });
        CardASCII.put("KH", new String[]{
                " _____ ",
                "|K♥ ww|",
                "|   {)|",
                "|(v)%%|",
                "| Y%%%|",
                "|_%%♥K|"
        });
        CardASCII.put("AH", new String[]{
                " _____ ",
                "|A_ _ |",
                "|( v )|",
                "| \\ / |",
                "|  .  |",
                "|___♥A|"
        });
        CardASCII.put("2D", new String[]{
                " _____ ",
                "|2    |",
                "|  ♦  |",
                "|     |",
                "|  ♦  |",
                "|____2|"
        });
        CardASCII.put("3D", new String[]{
                " _____ ",
                "|3    |",
                "|  ♦  |",
                "|  ♦  |",
                "|  ♦  |",
                "|____3|"
        });
        CardASCII.put("4D", new String[]{
                " _____ ",
                "|4    |",
                "|♦   ♦|",
                "|     |",
                "|♦   ♦|",
                "|____4|"
        });
        CardASCII.put("5D", new String[]{
                " _____ ",
                "|5    |",
                "|♦   ♦|",
                "|  ♦  |",
                "|♦   ♦|",
                "|____5|"
        });
        CardASCII.put("6D", new String[]{
                " _____ ",
                "|6    |",
                "|♦   ♦|",
                "|♦   ♦|",
                "|♦   ♦|",
                "|____6|"
        });
        CardASCII.put("7D", new String[]{
                " _____ ",
                "|7    |",
                "|♦   ♦|",
                "|♦ ♦ ♦|",
                "|♦   ♦|",
                "|____7|"
        });
        CardASCII.put("8D", new String[]{
                " _____ ",
                "|8   ♦|",
                "|♦   ♦|",
                "|♦   ♦|",
                "|♦   ♦|",
                "|♦___8|"
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
                "|J♦ __|",
                "| /\\{)|",
                "| \\/% |",
                "|   % |",
                "|__%♦J|"
        });
        CardASCII.put("QD", new String[]{
                " _____ ",
                "|Q♦ ww|",
                "| /\\{(|",
                "| \\/%%|",
                "|  %%%|",
                "|_%%♦Q|"
        });
        CardASCII.put("KD", new String[]{
                " _____ ",
                "|K♦ ww|",
                "| /\\{)|",
                "| \\/%%|",
                "|  %%%|",
                "|_%%♦K|"
        });
        CardASCII.put("AD", new String[]{
                " _____ ",
                "|A♦^  |",
                "| / \\ |",
                "| \\ / |",
                "|  .  |",
                "|___♦A|"
        });
    }
}