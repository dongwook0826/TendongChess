package chess.model.piece;

import chess.model.*;
import chess.model.board.*;

import java.util.HashSet;

public class King extends Piece {

    public static final int INDICATOR = 6;
    private boolean KingsideCastlingAvailable = true;
    private boolean QueensideCastlingAvailable = true;

    public King(ChessGame game, PieceColor pieceColor){
        super(game, pieceColor);
    }

    public King(ChessGame game, PieceColor pieceColor, String pieceId){
        super(game, pieceColor, pieceId);
    }

    @Override
    public int indicator(){
        return INDICATOR * color.toInteger();
    }

    @Override
    public void setMovableTakeable() {
        movable = new HashSet<>();
        takeable = new HashSet<>();
        int rank = rank();
        int file = file();
        for(int r=rank-1; r<=rank+1; r++){
            if(!game.inRange(r)) continue;
            for(int f=file-1; f<=file+1; f++){
                if(!game.inRange(f)) continue;
                Square sq = game.squareOn(f, r);
                if(game.squareOn(f, r).isFilled()){
                    Piece piece = game.squareOn(f, r).getPiece();
                    if(piece.color() == this.color) continue;
                    if(!sq.isAttacked()){
                        movable.add(sq);
                        takeable.add(sq);
                    }
                }else{
                    if(!sq.isAttacked())
                        movable.add(sq);
                }
            }
        }
        // castling
        if(!square.isAttacked()){ // this king is not in check now
            if(KingsideCastlingAvailable){
                Square sq = game.squareOn(file+1, rank);
                if(sq.isEmpty() && !sq.isAttacked()){
                    sq = game.squareOn(file+2, rank);
                    if(sq.isEmpty() && !sq.isAttacked()){
                        movable.add(sq);
                        // System.out.println("Kingside open");
                    }
                }
            }if(QueensideCastlingAvailable){
                Square sq = game.squareOn(file-1, rank);
                if(sq.isEmpty() && !sq.isAttacked()){
                    sq = game.squareOn(file-2, rank);
                    if(sq.isEmpty() && !sq.isAttacked()) {
                        sq = game.squareOn(file-3, rank);
                        if (sq.isEmpty()) {
                            movable.add(game.squareOn(file-2, rank));
                            // System.out.println("Queenside open");
                        }
                    }
                }
            }
        }
    }

    public void setKingsideCastlingAvailable(){
        KingsideCastlingAvailable = true;
    }

    public void setQueensideCastlingAvailable(){
        QueensideCastlingAvailable = true;
    }

    @Override
    public void makeMove(Move move, Square square){
        assert movable.contains(square) : "Impossible move, I told you!";
        int[][] board = game.getBoard();
        int pRank = rank(), pFile = file();
        int sRank = square.rank(), sFile = square.file();
        board[sRank][sFile] = board[pRank][pFile];
        board[pRank][pFile] = 0;
        square.setPiece(this);
        if(KingsideCastlingAvailable && pFile - sFile == -2){
            // Kingside castling
            completeCastling(true);
            board[pRank][5] = board[pRank][7];
            board[pRank][7] = 0;
            move.thisIsCastling(true);
        }else if(QueensideCastlingAvailable && pFile - sFile == 2){
            // Queenside castling
            completeCastling(false);
            board[pRank][3] = board[pRank][0];
            board[pRank][0] = 0;
            move.thisIsCastling(false);
        }
        noMoreKingsideCastling();
        noMoreQueensideCastling();
        game.setNoMoreCastlingMoveCount(color, 2);
    }

    void noMoreKingsideCastling(){
        if(KingsideCastlingAvailable){
            KingsideCastlingAvailable = false;
        }
    }

    void noMoreQueensideCastling(){
        if(QueensideCastlingAvailable) {
            QueensideCastlingAvailable = false;
        }
    }

    private void completeCastling(boolean isKingside){
        // king's move is already done;
        // complete rook's auxiliary move
        if(isKingside){
            game.getPieces(color).get("KR").setSquare(game.squareOn(file()-1, rank()));
        }else{
            game.getPieces(color).get("QR").setSquare(game.squareOn(file()+1, rank()));
        }
    }
}
