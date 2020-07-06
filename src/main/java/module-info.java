module Chess {
    requires javafx.controls;
    requires javafx.fxml;

    opens chess to javafx.controls, javafx.fxml;
    opens chess.view to javafx.controls, javafx.fxml;
    exports chess;
}