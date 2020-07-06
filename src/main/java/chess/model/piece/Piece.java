package chess.model.piece;

import chess.model.*;
import chess.model.board.*;

import java.util.HashSet;

public abstract class Piece {

    protected ChessGame game;
    protected Square square;
    protected String pieceId;
    protected PieceColor color;
    protected King myKing;
    protected boolean taken = false;
    protected HashSet<Square> movable = new HashSet<>(); // 이 말은 여기로 움직일 수 있음
    protected HashSet<Square> takeable = new HashSet<>(); // 여기에 상대방 말이 있으며 이를 잡을 수 있음; self checking 종속적; 이후 acn disambiguation에도 사용됨

    public Piece(ChessGame game, PieceColor color){
        this.game = game;
        this.color = color;
    }

    /*
    public Piece(Square square, boolean color){
        this(color);
        this.square = square;
    }
     */

    public Piece(ChessGame game, PieceColor color, String pieceId){
        this(game, color);
        this.pieceId = pieceId;
    }

    /*
    public void setPieceId(String pieceId){
        this.pieceId = pieceId;
    }
     */

    public void setSquare(Square square){
        if(this.square != null){
            this.square.setNullPiece();
        }
        this.square = square;
        if(square != null && square.getPiece() != this) square.setPiece(this);
    }

    public Square getSquare(){
        return square;
    }

    public void thisIsMyKing(){
        myKing = (King)game.getPieces(color).get("K");
    }

    /*
    public void thisIsMyKing(King myKing){
        assert myKing != null : "no king instance";
        this.myKing = myKing;
    }
     */

    public int file() { return square.file(); }

    public int rank() { return square.rank(); }

    @Override
    public String toString(){
        return pieceId;
    }

    public PieceColor color() { return color; }

    public void setTaken(){
        taken = true;
        movable = new HashSet<>();
        takeable = new HashSet<>();
        game.onePieceTaken(color);
    }

    public void undoTaken(){
        taken = false;
        game.onePieceTaken(color, true);
    }

    public boolean isTaken(){
        return taken;
    }

    public boolean isMovableTo(int file, int rank){
        if(!(game.inRange(file) && game.inRange(rank))) return false;
        // assert game.inRange(file) && game.inRange(rank) : "square index out of range";
        int[][] board = new int[ChessGame.DIM][];
        for(int i=0; i<ChessGame.DIM; i++){
            board[i] = game.getBoard()[i].clone();
        }
        // try the move on board
        if(board[rank][file] * color.toInteger() > 0) return false;
        int pRank = rank();
        int pFile = file();
        board[rank][file] = board[pRank][pFile];
        board[pRank][pFile] = 0;
        int kf, kr;
        if(this instanceof King){
            kf = file; kr = rank;
        }else{
            kf = myKing.file(); kr = myKing.rank();
        }

        // pawn check
        int oc = color.opponent().toInteger();
        if(game.inRange(kr+oc)){
            if(game.inRange(kf+1) && board[kr+oc][kf+1] == Pawn.INDICATOR*oc){
                return false;
            }else if(game.inRange(kf-1) && board[kr+oc][kf-1] == Pawn.INDICATOR*oc){
                return false;
            }
        }
        // knight check
        for (int[] nm : Knight.getMovableSquares()) {
            if (!(game.inRange(kf + nm[0]) && game.inRange(kr + nm[1]))) continue;
            if (board[kr + nm[1]][kf + nm[0]] == Knight.INDICATOR * oc) {
                return false;
            }
        }
        // vertical move - rook and queen - check
        for(int[] mv : Rook.getMovableDirection()){
            for(int f = kf+mv[0], r = kr+mv[1]; game.inRange(f) && game.inRange(r); f+=mv[0], r+=mv[1]){
                int tind = board[r][f]*oc;
                if(tind < 0) break; // our piece
                else if(tind > 0){ // opponent's piece
                    if(tind == Rook.INDICATOR || tind == Queen.INDICATOR){
                        return false;
                    }else break;
                }
            }
        }
        // diagonal move - bishop and queen - check
        for(int[] mv : Bishop.getMovableDirection()){
            for(int f = kf+mv[0], r = kr+mv[1]; game.inRange(f) && game.inRange(r); f+=mv[0], r+=mv[1]){
                int tind = board[r][f]*oc;
                if(tind < 0) break; // our piece
                else if(tind > 0){ // opponent's piece
                    if(tind == Bishop.INDICATOR || tind == Queen.INDICATOR){
                        return false;
                    }else break;
                }
            }
        }
        return true;
    }

    public abstract void setMovableTakeable();

    public HashSet<Square> getMovable(){
        return movable;
    }

    public HashSet<Square> getTakeable(){
        return takeable;
    }

    public abstract void makeMove(Move move, Square square);

    public abstract int indicator();
}
