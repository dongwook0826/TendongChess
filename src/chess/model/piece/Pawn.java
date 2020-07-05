package chess.model.piece;

import chess.model.*;
import chess.model.board.*;

// import java.util.Arrays;
import java.util.HashSet;

public class Pawn extends Piece {

    public static final int INDICATOR = 1;

    private boolean twoSquareMovable = true;
    private boolean leftEnPassantAvailable = false;
    private boolean rightEnPassantAvailable = false;
    private boolean promoted = false;

    public Pawn(ChessGame game, PieceColor pieceColor){
        super(game, pieceColor);
    }

    public Pawn(ChessGame game, PieceColor pieceColor, String pieceId){
        super(game, pieceColor, pieceId);
    }

    /*
    public boolean isLeftEnPassantAvailable(){
        return leftEnPassantAvailable;
    }

    public boolean isRightEnPassantAvailable(){
        return rightEnPassantAvailable;
    }
     */
    public void setTwoSquareMovable(){
        twoSquareMovable = true;
    }

    public void setLeftEnPassantAvailable(){
        leftEnPassantAvailable = true;
    }

    public void setRightEnPassantAvailable(){
        rightEnPassantAvailable = true;
    }

    public boolean isPromoted(){
        return promoted;
    }

    @Override
    public int indicator(){
        return INDICATOR * color.toInteger();
    }

    @Override
    public void setMovableTakeable(){
        movable = new HashSet<>();
        takeable = new HashSet<>();
        int rank = square.rank();
        int file = square.file();
        int col = color.toInteger();
        // white : 6, 7 / black : 0, 1
        if(game.squareOn(file, rank-col).isEmpty()){
            if(isMovableTo(file, rank-col))
                movable.add(game.squareOn(file, rank-col));
            // two square move
            if(twoSquareMovable && game.squareOn(file, rank-col-col).isEmpty()
                    && isMovableTo(file, rank-col-col)){
                movable.add(game.squareOn(file, rank-col-col));
            }
        }
        // capture
        for(int d=-1; d<=1; d+=2){
            if(!game.inRange(file+d) || !game.inRange(rank-col)) continue;
            if(game.squareOn(file+d, rank-col).isEmpty()) continue;
            Piece tgp = game.squareOn(file+d, rank-col).getPiece();
            if(/* tgp != null && */ tgp.color() != color && isMovableTo(file+d, rank-col)){
                movable.add(game.squareOn(file+d, rank-col));
                takeable.add(game.squareOn(file+d, rank-col));
            }
        }
        // en passant
        if(leftEnPassantAvailable){
            int[][] board = game.getBoard();
            int tmp = board[rank][file-1];
            board[rank][file-1] = 0;
            if(isMovableTo(file-1, rank-col)){
                movable.add(game.squareOn(file-1, rank-col));
                takeable.add(game.squareOn(file-1, rank-col));
            }else{
                leftEnPassantAvailable = false;
            }board[rank][file-1] = tmp;
        }else if(rightEnPassantAvailable){
            int[][] board = game.getBoard();
            int tmp = board[rank][file+1];
            board[rank][file+1] = 0;
            if(isMovableTo(file+1, rank-col)){
                movable.add(game.squareOn(file+1, rank-col));
                takeable.add(game.squareOn(file+1, rank-col));
            }else{
                rightEnPassantAvailable = false;
            }board[rank][file+1] = tmp;
        }
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
        if(twoSquareMovable && Math.abs(pRank - sRank) == 2){
            // first move, two-square maneuver
            if(sFile > 0){
                Square sq = game.squareOn(sFile-1, sRank);
                if(sq.isFilled()){
                    Piece leftPiece = sq.getPiece();
                    if(leftPiece.color() != color && leftPiece instanceof Pawn){
                        ((Pawn)leftPiece).setRightEnPassantAvailable();
                    }
                }
            }
            if(sFile < ChessGame.DIM - 1){
                Square sq = game.squareOn(sFile+1, sRank);
                if(sq.isFilled()){
                    Piece rightPiece = sq.getPiece();
                    if(rightPiece.color() != color && rightPiece instanceof Pawn){
                        ((Pawn)rightPiece).setLeftEnPassantAvailable();
                    }
                }
            }
        }else if(leftEnPassantAvailable && pFile - sFile == 1){
            // left en passant
            board[pRank][pFile-1] = 0;
            move.thisIsEnPassant(game.squareOn(pFile-1, pRank).getPiece());
        }else if(rightEnPassantAvailable && sFile - pFile == 1){
            // right en passant
            board[pRank][pFile+1] = 0;
            move.thisIsEnPassant(game.squareOn(pFile+1, pRank).getPiece());
        }
        noMoreTwoSquareMove();
        noMoreLeftEnPassant();
        noMoreRightEnPassant();
        game.setPawnsTwoSquareMoveCount(color, pieceId);
    }

    public void promoteTo(Move move, Square square, int pieceIndic){
        // pieceIndic == 2, 3, 4, 5 for Rook, Knight, Bishop, Queen
        assert movable.contains(square) : "Impossible move, I told you!";
        int[][] board = game.getBoard();
        int pRank = rank(), pFile = file();
        int sRank = square.rank(), sFile = square.file();
        board[sRank][sFile] = pieceIndic * color.toInteger();
        board[pRank][pFile] = 0;
        square.setPiece(this);
        Piece promotedPiece = switch(pieceIndic){
            case 2 -> new Rook(game, color, pieceId+"=R");
            case 3 -> new Knight(game, color, pieceId+"=N");
            case 4 -> new Bishop(game, color, pieceId+"=B");
            /* case 5 */
            default -> new Queen(game, color, pieceId+"=Q");
        };
        game.getPieces(color).put(promotedPiece.toString(), promotedPiece);
        promotedPiece.thisIsMyKing();
        square.setPiece(promotedPiece);
        promoted = true;
        movable = null;
        takeable = null;
        move.thisIsPromotion(promotedPiece);
        // no worry for other boolean member values (since they must be all false already)
        game.setPawnsTwoSquareMoveCount(color, pieceId);
    }

    private void noMoreTwoSquareMove(){
        if(twoSquareMovable){
            twoSquareMovable = false;
        }
    }

    private void noMoreLeftEnPassant(){
        if(leftEnPassantAvailable){
            leftEnPassantAvailable = false;
        }
    }

    private void noMoreRightEnPassant(){
        if(rightEnPassantAvailable){
            rightEnPassantAvailable = false;
        }
    }

    public void undoPromotion(){
        promoted = false;
    }
}
