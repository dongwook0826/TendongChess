package chess.model.piece;

import chess.model.*;
import chess.model.board.*;

// import java.util.Arrays;
import java.util.HashSet;

public class Bishop extends Piece {

    public static final int INDICATOR = 4;

    private static final int[][] MOVABLE_DIRECTION = {
            {1,1},{-1,1},{-1,-1},{1,-1}
    };

    public Bishop(ChessGame game, PieceColor pieceColor){
        super(game, pieceColor);
    }

    public Bishop(ChessGame game, PieceColor pieceColor, String pieceId){
        super(game, pieceColor, pieceId);
    }

    public static int[][] getMovableDirection(){
        return MOVABLE_DIRECTION;
    }

    @Override
    public int indicator(){
        return INDICATOR * color.toInteger();
    }

    @Override
    public void setMovableTakeable(){
        movable = new HashSet<>();
        takeable = new HashSet<>();
        int[][] board = game.getBoard();
        int rank = square.rank(), file = square.file();
        int col = color.toInteger();
        for (int[] dir : MOVABLE_DIRECTION) {
            int df = dir[0];
            int dr = dir[1];
            for (int f = file + df, r = rank + dr; game.inRange(f) && game.inRange(r); f += df, r += dr) {
                if (isMovableTo(f, r)) {
                    Square sq = game.squareOn(f, r);
                    movable.add(sq);
                    if (board[r][f]*col < 0) {
                        takeable.add(sq);
                    }
                }if(board[r][f] != 0){
                    break;
                }
            }
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
    }
}
