package chess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TendongChess extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // where all the chess components(piece, board, game, ...) are being ported on
        Parent root = FXMLLoader.load(getClass().getResource("view/ChessBoard.fxml"));
        primaryStage.setTitle("TendongChess");
        primaryStage.setScene(new Scene(root, 915, 725));
        primaryStage.setMinWidth(930);
        primaryStage.setMinHeight(765);
        primaryStage.setMaxWidth(930);
        primaryStage.setMaxHeight(765);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
