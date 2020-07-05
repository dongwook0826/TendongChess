package chess.view;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;

public class AppInfoController {

    private final String githubRepoURL = "https://github.com/dongwook0826/TendongChess";

    private Application chessApp;

    @FXML private Hyperlink githubRepoLink;

    @FXML
    private void initialize(){
        githubRepoLink.setOnAction(e -> chessApp.getHostServices().showDocument(githubRepoURL));
    }

    public void setChessApp(Application chessApp){
        this.chessApp = chessApp;
    }
}
