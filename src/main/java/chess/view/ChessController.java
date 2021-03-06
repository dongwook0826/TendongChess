package chess.view;

import chess.model.Move;
import chess.model.MovePair;
import chess.model.board.Square;
import chess.model.piece.Piece;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import chess.model.ChessGame;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashSet;

public class ChessController {

    private Application chessApp;

    public void setChessApp(Application chessApp){
        this.chessApp = chessApp;
    }

    private final int DIM = ChessGame.DIM;

    // ----------- Squares ---------------
    @FXML private Rectangle backBoard;
    @FXML private Pane boardPane;

    // square_(file/rank), from upper left to lower right
    @FXML private Rectangle square_00;
    @FXML private Rectangle square_01;
    @FXML private Rectangle square_02;
    @FXML private Rectangle square_03;
    @FXML private Rectangle square_04;
    @FXML private Rectangle square_05;
    @FXML private Rectangle square_06;
    @FXML private Rectangle square_07;

    @FXML private Rectangle square_10;
    @FXML private Rectangle square_11;
    @FXML private Rectangle square_12;
    @FXML private Rectangle square_13;
    @FXML private Rectangle square_14;
    @FXML private Rectangle square_15;
    @FXML private Rectangle square_16;
    @FXML private Rectangle square_17;

    @FXML private Rectangle square_20;
    @FXML private Rectangle square_21;
    @FXML private Rectangle square_22;
    @FXML private Rectangle square_23;
    @FXML private Rectangle square_24;
    @FXML private Rectangle square_25;
    @FXML private Rectangle square_26;
    @FXML private Rectangle square_27;

    @FXML private Rectangle square_30;
    @FXML private Rectangle square_31;
    @FXML private Rectangle square_32;
    @FXML private Rectangle square_33;
    @FXML private Rectangle square_34;
    @FXML private Rectangle square_35;
    @FXML private Rectangle square_36;
    @FXML private Rectangle square_37;

    @FXML private Rectangle square_40;
    @FXML private Rectangle square_41;
    @FXML private Rectangle square_42;
    @FXML private Rectangle square_43;
    @FXML private Rectangle square_44;
    @FXML private Rectangle square_45;
    @FXML private Rectangle square_46;
    @FXML private Rectangle square_47;

    @FXML private Rectangle square_50;
    @FXML private Rectangle square_51;
    @FXML private Rectangle square_52;
    @FXML private Rectangle square_53;
    @FXML private Rectangle square_54;
    @FXML private Rectangle square_55;
    @FXML private Rectangle square_56;
    @FXML private Rectangle square_57;

    @FXML private Rectangle square_60;
    @FXML private Rectangle square_61;
    @FXML private Rectangle square_62;
    @FXML private Rectangle square_63;
    @FXML private Rectangle square_64;
    @FXML private Rectangle square_65;
    @FXML private Rectangle square_66;
    @FXML private Rectangle square_67;

    @FXML private Rectangle square_70;
    @FXML private Rectangle square_71;
    @FXML private Rectangle square_72;
    @FXML private Rectangle square_73;
    @FXML private Rectangle square_74;
    @FXML private Rectangle square_75;
    @FXML private Rectangle square_76;
    @FXML private Rectangle square_77;

    // ------------- Piece Images --------------
    private final Image white_King;// = new Image("image/piece/White_King.png", 80.0d, 80.0d, true, true);
    private final Image white_Queen;// = new Image("image/piece/White_Queen.png", 80.0d, 80.0d, true, true);
    private final Image white_Rook;// = new Image("image/piece/White_Rook.png", 80.0d, 80.0d, true, true);
    private final Image white_Knight;// = new Image("image/piece/White_Knight.png", 80.0d, 80.0d, true, true);
    private final Image white_Bishop;// = new Image("image/piece/White_Bishop.png", 80.0d, 80.0d, true, true);
    private final Image white_Pawn;// = new Image("image/piece/White_Pawn.png", 80.0d, 80.0d, true, true);

    private final Image black_King;// = new Image("image/piece/Black_King.png", 80.0d, 80.0d, true, true);
    private final Image black_Queen;// = new Image("image/piece/Black_Queen.png", 80.0d, 80.0d, true, true);
    private final Image black_Rook;// = new Image("image/piece/Black_Rook.png", 80.0d, 80.0d, true, true);
    private final Image black_Knight;// = new Image("image/piece/Black_Knight.png", 80.0d, 80.0d, true, true);
    private final Image black_Bishop;// = new Image("image/piece/Black_Bishop.png", 80.0d, 80.0d, true, true);
    private final Image black_Pawn;// = new Image("image/piece/Black_Pawn.png", 80.0d, 80.0d, true, true);

    // image_(file/rank), from upper left to lower right
    @FXML private ImageView image_00;
    @FXML private ImageView image_01;
    @FXML private ImageView image_02;
    @FXML private ImageView image_03;
    @FXML private ImageView image_04;
    @FXML private ImageView image_05;
    @FXML private ImageView image_06;
    @FXML private ImageView image_07;

    @FXML private ImageView image_10;
    @FXML private ImageView image_11;
    @FXML private ImageView image_12;
    @FXML private ImageView image_13;
    @FXML private ImageView image_14;
    @FXML private ImageView image_15;
    @FXML private ImageView image_16;
    @FXML private ImageView image_17;

    @FXML private ImageView image_20;
    @FXML private ImageView image_21;
    @FXML private ImageView image_22;
    @FXML private ImageView image_23;
    @FXML private ImageView image_24;
    @FXML private ImageView image_25;
    @FXML private ImageView image_26;
    @FXML private ImageView image_27;

    @FXML private ImageView image_30;
    @FXML private ImageView image_31;
    @FXML private ImageView image_32;
    @FXML private ImageView image_33;
    @FXML private ImageView image_34;
    @FXML private ImageView image_35;
    @FXML private ImageView image_36;
    @FXML private ImageView image_37;

    @FXML private ImageView image_40;
    @FXML private ImageView image_41;
    @FXML private ImageView image_42;
    @FXML private ImageView image_43;
    @FXML private ImageView image_44;
    @FXML private ImageView image_45;
    @FXML private ImageView image_46;
    @FXML private ImageView image_47;

    @FXML private ImageView image_50;
    @FXML private ImageView image_51;
    @FXML private ImageView image_52;
    @FXML private ImageView image_53;
    @FXML private ImageView image_54;
    @FXML private ImageView image_55;
    @FXML private ImageView image_56;
    @FXML private ImageView image_57;

    @FXML private ImageView image_60;
    @FXML private ImageView image_61;
    @FXML private ImageView image_62;
    @FXML private ImageView image_63;
    @FXML private ImageView image_64;
    @FXML private ImageView image_65;
    @FXML private ImageView image_66;
    @FXML private ImageView image_67;

    @FXML private ImageView image_70;
    @FXML private ImageView image_71;
    @FXML private ImageView image_72;
    @FXML private ImageView image_73;
    @FXML private ImageView image_74;
    @FXML private ImageView image_75;
    @FXML private ImageView image_76;
    @FXML private ImageView image_77;

    // ------------- file & rank indicator text ---------------
    @FXML private Text file_0;
    @FXML private Text file_1;
    @FXML private Text file_2;
    @FXML private Text file_3;
    @FXML private Text file_4;
    @FXML private Text file_5;
    @FXML private Text file_6;
    @FXML private Text file_7;

    @FXML private Text rank_0;
    @FXML private Text rank_1;
    @FXML private Text rank_2;
    @FXML private Text rank_3;
    @FXML private Text rank_4;
    @FXML private Text rank_5;
    @FXML private Text rank_6;
    @FXML private Text rank_7;

    // ------------- tableview for moves done -------------
    @FXML private TableView<MovePair> moveTable;
    @FXML private TableColumn<MovePair, Integer> moveNumColumn;
    @FXML private TableColumn<MovePair, String> whiteMoveNotationColumn;
    @FXML private TableColumn<MovePair, String> blackMoveNotationColumn;

    // ------------- swapping icon --------------
    // @FXML private ImageView swapIcon;

    private final ContextMenu whitePromotionMenu = new ContextMenu();
    private final MenuItem whitePromToQueen = new MenuItem("Queen");
    private final MenuItem whitePromToRook = new MenuItem("Rook");
    private final MenuItem whitePromToBishop = new MenuItem("Bishop");
    private final MenuItem whitePromToKnight = new MenuItem("Knight");

    private final ContextMenu blackPromotionMenu = new ContextMenu();
    private final MenuItem blackPromToQueen = new MenuItem("Queen");
    private final MenuItem blackPromToRook = new MenuItem("Rook");
    private final MenuItem blackPromToBishop = new MenuItem("Bishop");
    private final MenuItem blackPromToKnight = new MenuItem("Knight");

    private ChessGame chessGame;
    private int[][] currentBoard; // 진행중인 게임의 보드 상태
    private int[][] showingBoard; // GUI에 보여지는 보드 상태

    private Rectangle initialSquare;
    private ImageView holdingPieceImage;
    private Piece holdingPiece;

    private boolean isSwapped = false;

    private int[] holdingIndex = {-1,-1};
    // int[] targetIndex as local variable in finishMovePiece

    private final HashSet<Circle> movableSpots = new HashSet<>();
    private final HashSet<Rectangle> squareHighlights = new HashSet<>();
    private final int[][] squareHighlightsIndex = {{-1,-1},{-1,-1}};

    private Point2D dragOffset = new Point2D(0.0d, 0.0d);

    private int currentShowingMoveInd = -1;
    private boolean allMoveLocked = false;
    private boolean gameFinished = false;

    {
        white_King = new Image(getClass().getResource("/image/piece/White_King.png").toExternalForm(), 80.0d, 80.0d, true, true);
        white_Queen = new Image(getClass().getResource("/image/piece/White_Queen.png").toExternalForm(), 80.0d, 80.0d, true, true);
        white_Rook = new Image(getClass().getResource("/image/piece/White_Rook.png").toExternalForm(), 80.0d, 80.0d, true, true);
        white_Knight = new Image(getClass().getResource("/image/piece/White_Knight.png").toExternalForm(), 80.0d, 80.0d, true, true);
        white_Bishop = new Image(getClass().getResource("/image/piece/White_Bishop.png").toExternalForm(), 80.0d, 80.0d, true, true);
        white_Pawn = new Image(getClass().getResource("/image/piece/White_Pawn.png").toExternalForm(), 80.0d, 80.0d, true, true);

        black_King = new Image(getClass().getResource("/image/piece/Black_King.png").toExternalForm(), 80.0d, 80.0d, true, true);
        black_Queen = new Image(getClass().getResource("/image/piece/Black_Queen.png").toExternalForm(), 80.0d, 80.0d, true, true);
        black_Rook = new Image(getClass().getResource("/image/piece/Black_Rook.png").toExternalForm(), 80.0d, 80.0d, true, true);
        black_Knight = new Image(getClass().getResource("/image/piece/Black_Knight.png").toExternalForm(), 80.0d, 80.0d, true, true);
        black_Bishop = new Image(getClass().getResource("/image/piece/Black_Bishop.png").toExternalForm(), 80.0d, 80.0d, true, true);
        black_Pawn = new Image(getClass().getResource("/image/piece/Black_Pawn.png").toExternalForm(), 80.0d, 80.0d, true, true);

        whitePromToQueen.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/White_Queen.png").toExternalForm(), 30.0d, 30.0d, true, true)));
        whitePromToRook.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/White_Rook.png").toExternalForm(), 30.0d, 30.0d, true, true)));
        whitePromToBishop.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/White_Bishop.png").toExternalForm(), 30.0d, 30.0d, true, true)));
        whitePromToKnight.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/White_Knight.png").toExternalForm(), 30.0d, 30.0d, true, true)));

        blackPromToQueen.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/Black_Queen.png").toExternalForm(), 30.0d, 30.0d, true, true)));
        blackPromToRook.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/Black_Rook.png").toExternalForm(), 30.0d, 30.0d, true, true)));
        blackPromToBishop.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/Black_Bishop.png").toExternalForm(), 30.0d, 30.0d, true, true)));
        blackPromToKnight.setGraphic(new ImageView(
                new Image(getClass().getResource("/image/piece/Black_Knight.png").toExternalForm(), 30.0d, 30.0d, true, true)));

        whitePromotionMenu.getItems().addAll(whitePromToQueen, whitePromToRook, whitePromToBishop, whitePromToKnight);
        blackPromotionMenu.getItems().addAll(blackPromToQueen, blackPromToRook, blackPromToBishop, blackPromToKnight);
    }

    @FXML
    private void initialize(){
        chessGame = new ChessGame();
        currentBoard = chessGame.getBoard();
        showingBoard = new int[DIM][];
        copyToShowingBoard(currentBoard);
        setPieceImages(currentBoard, isSwapped, true);
        moveNumColumn.setCellValueFactory(
                cellData -> cellData.getValue().moveNumProperty().asObject());
        whiteMoveNotationColumn.setCellValueFactory(
                cellData -> cellData.getValue().whiteMoveNotationProperty());
        blackMoveNotationColumn.setCellValueFactory(
                cellData -> cellData.getValue().blackMoveNotationProperty());
        moveNumColumn.setCellFactory(tc -> {
            TableCell<MovePair, Integer> cell = new TableCell<>(){
                @Override
                protected void updateItem(Integer moveNum, boolean empty){
                    super.updateItem(moveNum, empty);
                    setText(empty ? "" : moveNum.toString());
                }
            };
            cell.setOnMouseClicked(evt -> {
                if(!cell.isEmpty()){
                    if(cell.getItem() == 1 && evt.getClickCount() == 2){
                        handleTakebackRequest(null, false);
                    }else{
                        showBoardHistory(cell.getItem()-1, false);
                    }
                }
            });
            return cell;
        });
        whiteMoveNotationColumn.setCellFactory(tc -> {
            TableCell<MovePair, String> cell = new TableCell<>(){
                @Override
                protected void updateItem(String whiteMove, boolean empty){
                    super.updateItem(whiteMove, empty);
                    setText(empty ? "" : whiteMove);
                }
            };
            cell.setOnMouseClicked(evt -> {
                if(!cell.isEmpty()) {
                    MovePair mvpr = cell.getTableRow().getItem();
                    if(evt.getClickCount() == 2 && mvpr.getMoveNum()*2-1 < chessGame.getMoveCount()){
                        // takeback to mvpr.getMoveNum(), BLACK
                        handleTakebackRequest(mvpr, true);
                    }else{
                        showBoardHistory(mvpr.getMoveNum(), true);
                    }
                }
            });
            return cell;
        });
        blackMoveNotationColumn.setCellFactory(tc -> {
            TableCell<MovePair, String> cell = new TableCell<>(){
                @Override
                protected void updateItem(String blackMove, boolean empty){
                    super.updateItem(blackMove, empty);
                    setText(empty ? "" : blackMove);
                }
            };
            cell.setOnMouseClicked(evt -> {
                if(!cell.isEmpty()) {
                    MovePair mvpr = cell.getTableRow().getItem();
                    if(evt.getClickCount() == 2 && mvpr.getMoveNum()*2 < chessGame.getMoveCount()){
                        handleTakebackRequest(mvpr, false);
                    }else{
                        showBoardHistory(cell.getTableRow().getItem().getMoveNum(), false);
                    }
                }
            });
            return cell;
        });
        moveTable.setItems(chessGame.getMoveTableData());
        moveTable.getSelectionModel().setCellSelectionEnabled(true);
        moveTable.setOnKeyPressed(evt -> {
            switch(evt.getCode()){
                case UP, LEFT -> {
                    if (currentShowingMoveInd > -1)
                        showBoardHistory(--currentShowingMoveInd);
                }
                case DOWN, RIGHT -> {
                    if (currentShowingMoveInd < chessGame.getMoveCount() - 1)
                        showBoardHistory(++currentShowingMoveInd);
                }
            }
        });
    }

    private void handleTakebackRequest(MovePair movePair, boolean isWhite){
        Alert takebackAlert = new Alert(Alert.AlertType.CONFIRMATION);
        takebackAlert.setTitle("Takeback request confirmed");
        takebackAlert.setHeaderText("Takeback request confirmed");
        if(movePair == null){
            takebackAlert.setContentText("The game will be taken back to initial position\nAccept it?");
        }else {
            takebackAlert.setContentText(String.format(
                    "The game will be taken back to %d%s\nAccept it?", movePair.getMoveNum(),
                    isWhite ? "."+movePair.getWhiteMoveNotation() : "..."+movePair.getBlackMoveNotation()));
        }
        takebackAlert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                if(movePair == null){
                    chessGame.takeback(1, true);
                }else{
                    chessGame.takeback(movePair.getMoveNum() + (isWhite ? 0 : 1), !isWhite);
                }
                // System.out.printf("%s's turn\n", chessGame.getTurn().toString());
                // chessGame.printBoard(false);
                allMoveLocked = false;
                gameFinished = false;
            }
        });
    }

    // ---------------- menu choice event handler ----------------
    @FXML
    private void gameInitialize(){
        Alert gameInitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        gameInitAlert.setTitle("New game request confirmed");
        gameInitAlert.setHeaderText("Starting a new game");
        gameInitAlert.setContentText("Restart the game?\nThe progress of current game will be abandoned.");
        gameInitAlert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK)
                initialize();
        });
    }

    @FXML
    private void gameExit(){
        Alert gameExitAlert = new Alert(Alert.AlertType.CONFIRMATION);
        gameExitAlert.setTitle("App close request confirmed");
        gameExitAlert.setHeaderText("Closing TendongChess");
        gameExitAlert.setContentText("Close the app?\nThe progress of current game will be abandoned.");
        gameExitAlert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK)
                System.exit(0);
        });
    }

    @FXML
    private void resign(){
        int res = chessGame.getResult();
        if(res >= -1 && res <= 1) return;
        boolean turn = chessGame.getTurn().isWhite();
        Alert resignAlert = new Alert(Alert.AlertType.CONFIRMATION);
        resignAlert.setTitle("Resign");
        resignAlert.setHeaderText(String.format("Resigning as %s", turn ? "white" : "black"));
        resignAlert.setContentText("Are you sure for the resignation?");
        resignAlert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                chessGame.setResult(turn ? -1 : 1, true);
                checkResult();
            }
        });
    }

    @FXML
    private void offerDraw(){
        int res = chessGame.getResult();
        if(res >= -1 && res <= 1) return;
        boolean turn = chessGame.getTurn().isWhite();
        Alert drawAlert = new Alert(Alert.AlertType.CONFIRMATION);
        drawAlert.setTitle("Draw offer");
        drawAlert.setHeaderText(String.format("Offering a draw from %s",
                turn ? "white to black" : "black to white"));
        drawAlert.setContentText("Are you sure for the draw offer?");
        drawAlert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK)
                acceptDraw(turn);
        });
    }

    private void acceptDraw(boolean turn){
        Alert drawAcceptAlert = new Alert(Alert.AlertType.CONFIRMATION);
        drawAcceptAlert.setTitle("Draw acceptance");
        drawAcceptAlert.setHeaderText(String.format("Draw offer from %s", turn ? "white" : "black"));
        drawAcceptAlert.setContentText("Do you accept the draw offer?");
        drawAcceptAlert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                chessGame.setResult(0, true);
                checkResult();
            }
        });
    }


    // @FXML private void forceFinish(){}

    @FXML
    private void showAppInfo() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        // System.out.println(getClass().getResource("/AppInfo.fxml"));
        fxmlLoader.setLocation(getClass().getResource("/AppInfo.fxml"));
        Parent appInfo = fxmlLoader.load(/*getClass().getResource("/AppInfo.fxml").openStream()*/);
        AppInfoController infoCtrl = fxmlLoader.getController();
        infoCtrl.setChessApp(chessApp);
        Stage appInfoStage = new Stage();
        appInfoStage.setTitle("TendongChess Info");
        appInfoStage.setScene(new Scene(appInfo, 500, 250));
        appInfoStage.setResizable(false);
        appInfoStage.show();
    }

    // ---------------- pieces mouse event handler ----------------

    @FXML
    private void startMovePiece(MouseEvent evt){
        clearHighlightSquare();
        holdingIndex = pickIndex(evt);
        highlightSquare(holdingIndex);
        initialSquare = indToSquareMap(holdingIndex[0], holdingIndex[1]);
        holdingPieceImage = indToImageViewMap(holdingIndex[0], holdingIndex[1]);
        assert holdingPieceImage != null;
        if(!holdingPieceImage.isVisible()){
            holdingPieceImage = null;
            evt.consume();
            return;
        }
        holdingPieceImage.toFront();
        holdingPiece = isSwapped ?
                chessGame.squareOn(DIM-1-holdingIndex[1], DIM-1-holdingIndex[0]).getPiece() :
                chessGame.squareOn(holdingIndex[1], holdingIndex[0]).getPiece();
        if(!allMoveLocked && !gameFinished
                && holdingPiece.color() == chessGame.getTurn()){
            double sqWidth = square_00.getWidth();
            for(Square sq : holdingPiece.getMovable()){
                int fl = sq.file(), rk = sq.rank();
                Rectangle rect = isSwapped ? indToSquareMap(DIM-1-rk, DIM-1-fl) : indToSquareMap(rk, fl);
                assert rect != null;
                Circle spot = new Circle(rect.getLayoutX() + sqWidth/2, rect.getLayoutY() + sqWidth/2,
                                        sqWidth/4, Color.YELLOW);
                if(holdingPiece.getTakeable().contains(sq))
                    spot.setFill(Color.RED);
                spot.setOpacity(0.5d);
                boardPane.getChildren().add(spot);
                movableSpots.add(spot);
            }
        }
        dragOffset = new Point2D(evt.getX(), evt.getY());
    }

    @FXML
    private void movePiece(MouseEvent evt){
        if(holdingPieceImage == null){
            evt.consume();
            return;
        }
        Point2D scenePoint = new Point2D(evt.getSceneX(), evt.getSceneY());
        Point2D boardPoint = boardPane.sceneToLocal(scenePoint);
        if(!onBoardRange(boardPoint)){
            evt.consume();
            return;
        }
        holdingPieceImage.relocate(boardPoint.getX() - dragOffset.getX(),
                             boardPoint.getY() - dragOffset.getY());
    }

    private boolean onBoardRange(Point2D scenePt){
        Point2D boardPt = backBoard.sceneToLocal(scenePt);
        return boardPt.getX() >= 0.0d
                && boardPt.getY() >= 0.0d
                && boardPt.getX() <= backBoard.getWidth()
                && boardPt.getY() <= backBoard.getHeight();
    }

    @FXML
    private void finishMovePiece(MouseEvent evt){
        int[] targetIndex = pickIndex(evt);
        Rectangle targetSquare;
        if(targetIndex[0]<0){
            targetSquare = initialSquare;
        }else{
            targetSquare = indToSquareMap(targetIndex[0], targetIndex[1]);
        }
        if(targetSquare != initialSquare){
            StringBuilder uci = moveToUCI(holdingIndex, targetIndex);
            Square tg = isSwapped ?
                    chessGame.squareOn(DIM-1- targetIndex[1], DIM-1- targetIndex[0]) :
                    chessGame.squareOn(targetIndex[1], targetIndex[0]);
            boolean moveValid = !allMoveLocked && !gameFinished
                                && holdingPiece.color() == chessGame.getTurn()
                                && holdingPiece.getMovable().contains(tg);
            if(!moveValid){
                evt.consume();
            }else{
                ImageView targetImage = indToImageViewMap(targetIndex[0], targetIndex[1]);
                targetImage.setImage(holdingPieceImage.getImage());
                if (!targetImage.isVisible()) targetImage.setVisible(true);
                holdingPieceImage.setImage(null);
                holdingPieceImage.setVisible(false);
                // if pawn is on the last rank, choose the material kind to promote to
                if (targetImage.getImage() == white_Pawn
                        && targetIndex[0] == (isSwapped ? 7 : 0)) {
                    // System.out.println("white prom");
                    whitePromotionMenu.show(targetImage, evt.getScreenX(), evt.getScreenY());
                    whitePromToQueen.setOnAction(e -> uci.append('q'));
                    whitePromToRook.setOnAction(e -> uci.append('r'));
                    whitePromToBishop.setOnAction(e -> uci.append('b'));
                    whitePromToKnight.setOnAction(e -> uci.append('n'));
                    whitePromotionMenu.setOnHiding(e -> {
                        if (uci.length() <= 4) uci.append('q');
                        targetImage.setImage(switch (uci.charAt(4)) {
                            case 'r' -> white_Rook;
                            case 'b' -> white_Bishop;
                            case 'n' -> white_Knight;
                            // case 'q'
                            default -> white_Queen;
                        });
                        /*
                        System.out.printf("(%d, %d) -> (%d, %d) :: %s\n",
                                holdingIndex[0], holdingIndex[1], targetIndex[0], targetIndex[1], uci.toString());
                         */
                        chessGame.move(uci.toString());
                        currentShowingMoveInd++;
                        moveTable.getSelectionModel().select(currentShowingMoveInd/2,
                                currentShowingMoveInd%2 == 0 ? whiteMoveNotationColumn : blackMoveNotationColumn);
                        // moveTable.getFocusModel().focus(currentShowingMoveInd/2);
                        checkResult();
                        setPieceImages(currentBoard, isSwapped, false);
                    });
                }else if(targetImage.getImage() == black_Pawn
                        && targetIndex[0] == (isSwapped ? 0 : 7)){
                    // System.out.println("black prom");
                    blackPromotionMenu.show(targetImage, evt.getScreenX(), evt.getScreenY());
                    blackPromToQueen.setOnAction(e -> uci.append('q'));
                    blackPromToRook.setOnAction(e -> uci.append('r'));
                    blackPromToBishop.setOnAction(e -> uci.append('b'));
                    blackPromToKnight.setOnAction(e -> uci.append('n'));
                    blackPromotionMenu.setOnHiding(e -> {
                        if (uci.length() <= 4) uci.append('q');
                        targetImage.setImage(switch (uci.charAt(4)) {
                            case 'r' -> black_Rook;
                            case 'b' -> black_Bishop;
                            case 'n' -> black_Knight;
                            // case 'q'
                            default -> black_Queen;
                        });
                        /*
                        System.out.printf("(%d, %d) -> (%d, %d) :: %s\n",
                                holdingIndex[0], holdingIndex[1], targetIndex[0], targetIndex[1], uci.toString());
                         */
                        chessGame.move(uci.toString());
                        currentShowingMoveInd++;
                        moveTable.getSelectionModel().select(currentShowingMoveInd/2,
                                currentShowingMoveInd%2 == 0 ? whiteMoveNotationColumn : blackMoveNotationColumn);
                        // moveTable.getFocusModel().focus(currentShowingMoveInd/2);
                        checkResult();
                        setPieceImages(currentBoard, isSwapped, false);
                    });
                } else {
                    /*
                    System.out.printf("(%d, %d) -> (%d, %d) :: %s\n",
                            holdingIndex[0], holdingIndex[1], targetIndex[0], targetIndex[1], uci.toString());
                     */
                    chessGame.move(uci.toString());
                    currentShowingMoveInd++;
                    moveTable.getSelectionModel().select(currentShowingMoveInd/2,
                            currentShowingMoveInd%2 == 0 ? whiteMoveNotationColumn : blackMoveNotationColumn);
                    // moveTable.getFocusModel().focus(currentShowingMoveInd/2);
                    checkResult();
                }
                squareHighlightsIndex[0] = holdingIndex;
                squareHighlightsIndex[1] = targetIndex;
                // highlightSquare(squareHighlightsIndex[0]);
                highlightSquare(squareHighlightsIndex[1]);
                setPieceImages(currentBoard, isSwapped, false);
                // chessGame.printBoard(isSwapped);
            }
        }else{
            evt.consume();
        }
        holdingPieceImage.relocate(initialSquare.getLayoutX(), initialSquare.getLayoutY());
        dragOffset = new Point2D(0.0d, 0.0d);
        initialSquare = null;
        for(Circle spot : movableSpots){
            boardPane.getChildren().remove(spot);
        }movableSpots.clear();
    }

    private void checkResult(){
        int result = chessGame.getResult();
        if(result <= -2 || result >= 2) return;
        gameFinished = true;
        Alert gameFinishAlert = new Alert(Alert.AlertType.INFORMATION);
        gameFinishAlert.setTitle("Game finished");
        if(result == 1){
            gameFinishAlert.setHeaderText("White's win by "+chessGame.getResultInfo());
            gameFinishAlert.setContentText("Congratulations!\nBetter next time, Black! :)");
            ImageView icon = new ImageView(white_King);
            icon.setFitHeight(50);
            icon.setFitWidth(50);
            gameFinishAlert.getDialogPane().setGraphic(icon);
        }else if(result == -1){
            gameFinishAlert.setHeaderText("Black's win by "+chessGame.getResultInfo());
            gameFinishAlert.setContentText("Congratulations!\nBetter next time, White! :)");
            ImageView icon = new ImageView(black_King);
            icon.setFitHeight(50);
            icon.setFitWidth(50);
            gameFinishAlert.getDialogPane().setGraphic(icon);
        }else{ // result == 0
            gameFinishAlert.setHeaderText("Draw by "+chessGame.getResultInfo());
            gameFinishAlert.setContentText("Maybe we can break the ties next time!");
        }gameFinishAlert.showAndWait();
    }

    private int[] pickIndex(MouseEvent evt){
        return pickIndex(evt.getSceneX(), evt.getSceneY());
    }

    private int[] pickIndex(double sceneX, double sceneY){
        Point2D mousePoint = new Point2D(sceneX, sceneY);
        int[] ind = {-1, -1};
        rankLoop :
        for(int r=0; r<DIM; r++){
            Rectangle zsq = indToSquareMap(r,0);
            assert zsq != null;
            // System.out.println(zsq.getId());
            double rankY = zsq.sceneToLocal(mousePoint).getY();
            double height = zsq.getHeight();
            if(rankY<0 || rankY>=height) continue;
            for(int f=0; f<DIM; f++){
                Rectangle sq = indToSquareMap(r,f);
                assert sq != null;
                Point2D localPoint = sq.sceneToLocal(mousePoint);
                if(!sq.contains(localPoint)) continue;
                ind = new int[]{r,f};
                break rankLoop;
            }
        }
        // System.out.printf("picked ind (%d,%d)\n", ind[0], ind[1]);
        return ind;
    }

    private StringBuilder moveToUCI(int[] hold, int[] targ){
        assert hold.length == 2 && targ.length == 2;
        StringBuilder sb = new StringBuilder();
        if(isSwapped){
            sb.append((char)('h'-hold[1]));
            sb.append(1+hold[0]);
            sb.append((char)('h'-targ[1]));
            sb.append(1+targ[0]);
        }else{
            sb.append((char)('a'+hold[1]));
            sb.append(8-hold[0]);
            sb.append((char)('a'+targ[1]));
            sb.append(8-targ[0]);
        }return sb;
    }

    // ---------------------- showing piece on board ------------------------

    private void setPieceImages(int[][] board, boolean isSwapped, boolean isInit){
        assert board.length == DIM && board[0].length == DIM;
        if(isSwapped){
            for(int r=0; r<DIM; r++){
                for(int f=0; f<DIM; f++){
                    if(!isInit && board[DIM-1-r][DIM-1-f] == showingBoard[DIM-1-r][DIM-1-f]) continue;
                    Image pimg = intToImageMap(board[DIM-1-r][DIM-1-f]);
                    ImageView imgVw = indToImageViewMap(r,f);
                    imgVw.setImage(pimg);
                    if(pimg == null){
                        imgVw.setVisible(false);
                    }else if(!imgVw.isVisible()){
                        imgVw.setVisible(true);
                    }
                }
            }
        }else{
            for(int r=0; r<DIM; r++){
                for(int f=0; f<DIM; f++){
                    if(!isInit && board[r][f] == showingBoard[r][f]) continue;
                    Image pimg = intToImageMap(board[r][f]);
                    ImageView imgVw = indToImageViewMap(r,f);
                    imgVw.setImage(pimg);
                    if(pimg == null){
                        imgVw.setVisible(false);
                    }else if(!imgVw.isVisible()){
                        imgVw.setVisible(true);
                    }
                }
            }
        }copyToShowingBoard(board);
    }

    @FXML
    private void swap(){
        // swapIcon 클릭하면 180도 돌려서 보여주기
        isSwapped = !isSwapped;
        // file/rank 표시자 재배치
        flipFileRankIndic(isSwapped);
        // 강조된 사각형 재배치
        clearHighlightSquare();
        for(int t=0; t<squareHighlightsIndex.length; t++)
            for(int i=0; i<squareHighlightsIndex[t].length; i++)
                squareHighlightsIndex[t][i] = DIM-1-squareHighlightsIndex[t][i];
        highlightSquare(squareHighlightsIndex[0]);
        highlightSquare(squareHighlightsIndex[1]);
        // 말들 이미지 재배치
        for(int r=0; r<DIM; r++){
            for(int f=0; f<DIM; f++){
                if(currentBoard[DIM-1-r][DIM-1-f] == currentBoard[r][f]) continue;
                Image pimg = intToImageMap(isSwapped ? currentBoard[DIM-1-r][DIM-1-f] : currentBoard[r][f]);
                ImageView imgVw = indToImageViewMap(r,f);
                imgVw.setImage(pimg);
                if(pimg == null){
                    imgVw.setVisible(false);
                }else if(!imgVw.isVisible()){
                    imgVw.setVisible(true);
                }
            }
        }
    }

    private void showBoardHistory(int moveInd){
        showBoardHistory((moveInd+2)/2, moveInd%2==0);
    }

    private void showBoardHistory(int moveNum, boolean isWhite){
        // 0 -> 1T -> 1F -> 2T -> 2F -> 3T -> 3F -> ...
        int moveInd = moveNum == 0 ? -1 : (moveNum*2 - (isWhite ? 2 : 1));
        if(moveNum <= 0){
            moveTable.getSelectionModel().select(0, moveNumColumn);
            // moveTable.getFocusModel().focus(0);
        }else{
            moveTable.getSelectionModel().select(moveNum-1, isWhite ? whiteMoveNotationColumn : blackMoveNotationColumn);
            // moveTable.getFocusModel().focus(moveNum-1);
        }
        if(moveInd >= chessGame.getMoveCount()) moveInd = chessGame.getMoveCount()-1;
        setPieceImages(moveNum == 0 ? chessGame.getInitBoard() :
                chessGame.getMoves().get(moveInd).getBoardHistory(),
                isSwapped, false);
        clearHighlightSquare();
        if(moveInd >= 0){
            Move move = chessGame.getMoves().get(moveInd);
                squareHighlightsIndex[0][0] = move.getFromSquare().rank();
                squareHighlightsIndex[0][1] = move.getFromSquare().file();
                squareHighlightsIndex[1][0] = move.getToSquare().rank();
                squareHighlightsIndex[1][1] = move.getToSquare().file();
            if(isSwapped){
                for(int t=0; t<squareHighlightsIndex.length; t++)
                    for(int i=0; i<squareHighlightsIndex[t].length; i++)
                        squareHighlightsIndex[t][i] = DIM-1-squareHighlightsIndex[t][i];
            }
            highlightSquare(squareHighlightsIndex[0]);
            highlightSquare(squareHighlightsIndex[1]);
        }
        currentShowingMoveInd = moveInd;
        allMoveLocked = moveInd != chessGame.getMoveCount()-1;
    }

    private void copyToShowingBoard(int[][] tgBoard){
        assert tgBoard.length == DIM;
        for(int r=0; r<DIM; r++){
            assert tgBoard[r].length == DIM;
            showingBoard[r] = tgBoard[r].clone();
        }
    }

    private void highlightSquare(int[] ind){
        if(ind.length < 2) return;
        highlightSquare(ind[0], ind[1]);
    }

    private void highlightSquare(int rk, int fl){
        Rectangle piv = indToSquareMap(rk, fl);
        if(piv == null) return;
        Rectangle hli = new Rectangle(piv.getWidth(), piv.getHeight(), Color.YELLOW);
        hli.setX(piv.getLayoutX());
        hli.setY(piv.getLayoutY());
        hli.setOpacity(0.3d);
        squareHighlights.add(hli);
        boardPane.getChildren().add(hli);
    }

    private void clearHighlightSquare(){
        for(Rectangle rect : squareHighlights){
            boardPane.getChildren().remove(rect);
        }squareHighlights.clear();
    }

    // -------------------------- maps -----------------------------

    private void flipFileRankIndic(boolean isSwapped){
        if(isSwapped){
            file_0.setText("h");
            file_1.setText("g");
            file_2.setText("f");
            file_3.setText("e");
            file_4.setText("d");
            file_5.setText("c");
            file_6.setText("b");
            file_7.setText("a");

            rank_0.setText("1");
            rank_1.setText("2");
            rank_2.setText("3");
            rank_3.setText("4");
            rank_4.setText("5");
            rank_5.setText("6");
            rank_6.setText("7");
            rank_7.setText("8");
        }else{
            file_0.setText("a");
            file_1.setText("b");
            file_2.setText("c");
            file_3.setText("d");
            file_4.setText("e");
            file_5.setText("f");
            file_6.setText("g");
            file_7.setText("h");

            rank_0.setText("8");
            rank_1.setText("7");
            rank_2.setText("6");
            rank_3.setText("5");
            rank_4.setText("4");
            rank_5.setText("3");
            rank_6.setText("2");
            rank_7.setText("1");
        }
    }

    private Image intToImageMap(int ind){
        return switch(ind){
            case 1 -> white_Pawn;
            case 2 -> white_Rook;
            case 3 -> white_Knight;
            case 4 -> white_Bishop;
            case 5 -> white_Queen;
            case 6 -> white_King;
            case -1 -> black_Pawn;
            case -2 -> black_Rook;
            case -3 -> black_Knight;
            case -4 -> black_Bishop;
            case -5 -> black_Queen;
            case -6 -> black_King;
            default -> null;
        };
    }

    private Rectangle indToSquareMap(int rk, int fl){
        switch(rk){
            case 0 -> {
                return switch(fl){
                    case 0 -> square_00;
                    case 1 -> square_10;
                    case 2 -> square_20;
                    case 3 -> square_30;
                    case 4 -> square_40;
                    case 5 -> square_50;
                    case 6 -> square_60;
                    case 7 -> square_70;
                    default -> null;
                };
            }
            case 1 -> {
                return switch(fl){
                    case 0 -> square_01;
                    case 1 -> square_11;
                    case 2 -> square_21;
                    case 3 -> square_31;
                    case 4 -> square_41;
                    case 5 -> square_51;
                    case 6 -> square_61;
                    case 7 -> square_71;
                    default -> null;
                };
            }
            case 2 -> {
                return switch(fl){
                    case 0 -> square_02;
                    case 1 -> square_12;
                    case 2 -> square_22;
                    case 3 -> square_32;
                    case 4 -> square_42;
                    case 5 -> square_52;
                    case 6 -> square_62;
                    case 7 -> square_72;
                    default -> null;
                };
            }
            case 3 -> {
                return switch(fl){
                    case 0 -> square_03;
                    case 1 -> square_13;
                    case 2 -> square_23;
                    case 3 -> square_33;
                    case 4 -> square_43;
                    case 5 -> square_53;
                    case 6 -> square_63;
                    case 7 -> square_73;
                    default -> null;
                };
            }
            case 4 -> {
                return switch(fl){
                    case 0 -> square_04;
                    case 1 -> square_14;
                    case 2 -> square_24;
                    case 3 -> square_34;
                    case 4 -> square_44;
                    case 5 -> square_54;
                    case 6 -> square_64;
                    case 7 -> square_74;
                    default -> null;
                };
            }
            case 5 -> {
                return switch(fl){
                    case 0 -> square_05;
                    case 1 -> square_15;
                    case 2 -> square_25;
                    case 3 -> square_35;
                    case 4 -> square_45;
                    case 5 -> square_55;
                    case 6 -> square_65;
                    case 7 -> square_75;
                    default -> null;
                };
            }
            case 6 -> {
                return switch(fl){
                    case 0 -> square_06;
                    case 1 -> square_16;
                    case 2 -> square_26;
                    case 3 -> square_36;
                    case 4 -> square_46;
                    case 5 -> square_56;
                    case 6 -> square_66;
                    case 7 -> square_76;
                    default -> null;
                };
            }
            case 7 -> {
                return switch(fl){
                    case 0 -> square_07;
                    case 1 -> square_17;
                    case 2 -> square_27;
                    case 3 -> square_37;
                    case 4 -> square_47;
                    case 5 -> square_57;
                    case 6 -> square_67;
                    case 7 -> square_77;
                    default -> null;
                };
            }
            default -> {
                return null;
            }
        }
    }

    private ImageView indToImageViewMap(int rk, int fl){
        switch(rk) {
            case 0 -> {
                return switch (fl) {
                    case 0 -> image_00;
                    case 1 -> image_10;
                    case 2 -> image_20;
                    case 3 -> image_30;
                    case 4 -> image_40;
                    case 5 -> image_50;
                    case 6 -> image_60;
                    // case 7
                    default -> image_70;
                };
            }
            case 1 -> {
                return switch (fl) {
                    case 0 -> image_01;
                    case 1 -> image_11;
                    case 2 -> image_21;
                    case 3 -> image_31;
                    case 4 -> image_41;
                    case 5 -> image_51;
                    case 6 -> image_61;
                    // case 7
                    default -> image_71;
                };
            }
            case 2 -> {
                return switch (fl) {
                    case 0 -> image_02;
                    case 1 -> image_12;
                    case 2 -> image_22;
                    case 3 -> image_32;
                    case 4 -> image_42;
                    case 5 -> image_52;
                    case 6 -> image_62;
                    // case 7
                    default -> image_72;
                };
            }
            case 3 -> {
                return switch (fl) {
                    case 0 -> image_03;
                    case 1 -> image_13;
                    case 2 -> image_23;
                    case 3 -> image_33;
                    case 4 -> image_43;
                    case 5 -> image_53;
                    case 6 -> image_63;
                    // case 7
                    default -> image_73;
                };
            }
            case 4 -> {
                return switch (fl) {
                    case 0 -> image_04;
                    case 1 -> image_14;
                    case 2 -> image_24;
                    case 3 -> image_34;
                    case 4 -> image_44;
                    case 5 -> image_54;
                    case 6 -> image_64;
                    // case 7
                    default -> image_74;
                };
            }
            case 5 -> {
                return switch (fl) {
                    case 0 -> image_05;
                    case 1 -> image_15;
                    case 2 -> image_25;
                    case 3 -> image_35;
                    case 4 -> image_45;
                    case 5 -> image_55;
                    case 6 -> image_65;
                    // case 7
                    default -> image_75;
                };
            }
            case 6 -> {
                return switch (fl) {
                    case 0 -> image_06;
                    case 1 -> image_16;
                    case 2 -> image_26;
                    case 3 -> image_36;
                    case 4 -> image_46;
                    case 5 -> image_56;
                    case 6 -> image_66;
                    // case 7
                    default -> image_76;
                };
            }
            // case 7
            default -> {
                return switch (fl) {
                    case 0 -> image_07;
                    case 1 -> image_17;
                    case 2 -> image_27;
                    case 3 -> image_37;
                    case 4 -> image_47;
                    case 5 -> image_57;
                    case 6 -> image_67;
                    // case 7
                    default -> image_77;
                };
            }
        }
    }
}
