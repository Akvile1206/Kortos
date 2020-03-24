package ClientSide;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientGUI extends Application {
    private static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{

        window = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("../resources/scenes/ingame.fxml"));

        Scene menu = new Scene(root);

        setWindowTitle("Card Game");
        window.setScene(menu);
        window.show();
    }

    void setWindowTitle(String name) {
        window.setTitle(name);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
