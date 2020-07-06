package chess;

import chess.view.ChessController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TendongChess extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/ChessBoard.fxml"));
        // System.out.println(fxmlLoader.getLocation());
        Parent root = fxmlLoader.load();
        ChessController chessCtrl = fxmlLoader.getController();
        chessCtrl.setChessApp(this);
        primaryStage.setTitle("TendongChess");
        primaryStage.setScene(new Scene(root, 915, 725));
        primaryStage.setResizable(false);
        /*
        primaryStage.setMinWidth(930);
        primaryStage.setMinHeight(765);
        primaryStage.setMaxWidth(930);
        primaryStage.setMaxHeight(765);
         */
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
